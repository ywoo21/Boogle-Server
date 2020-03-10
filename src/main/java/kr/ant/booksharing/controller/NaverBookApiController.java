package kr.ant.booksharing.controller;

import kr.ant.booksharing.domain.Item;
import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.model.ItemAllRes;
import kr.ant.booksharing.model.ItemRes;
import kr.ant.booksharing.repository.ItemReceivingRepository;
import kr.ant.booksharing.repository.ItemRepository;
import kr.ant.booksharing.repository.SellItemHistoryRepository;
import kr.ant.booksharing.repository.SellItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.json.XML;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class NaverBookApiController {

    static final String clientId = "Uu3ZOIKWPC7H2xIE45QX";//애플리케이션 클라이언트 아이디값";
    static final String clientSecret = "gfEQG2XKng";//애플리케이션 클라이언트 시크릿값";

    private final SellItemRepository sellItemRepository;
    private final ItemRepository itemRepository;
    private final ItemReceivingRepository itemReceivingRepository;
    private final SellItemHistoryRepository sellItemHistoryRepository;

    public NaverBookApiController(final SellItemRepository sellItemRepository,
                                  final ItemRepository itemRepository,
                                  final ItemReceivingRepository itemReceivingRepository,
                                  final SellItemHistoryRepository sellItemHistoryRepository) {
        this.sellItemRepository = sellItemRepository;
        this.itemRepository = itemRepository;
        this.itemReceivingRepository = itemReceivingRepository;
        this.sellItemHistoryRepository = sellItemHistoryRepository;
    }

    @GetMapping("naver/bookApi/buy/title")
    public ResponseEntity getAllRegisteredBuyItemsByTitle(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                          @RequestParam(value = "itemResListSortType", defaultValue = "accuracy") String itemResListSortType,
                                                          @RequestParam(value = "itemNotRegisteredResListSortType", defaultValue = "accuracy") String itemNotRegisteredResListSortType) {
        try {

            List<String> itemIdList = new ArrayList<>();

            List<ItemRes> itemResList = new ArrayList<>();
            List<ItemRes> itemNotRegisteredResList = new ArrayList<>();

           //if (itemRepository.findAllByTitleOrSubjectListOrProfessorListContaining(keyword).isPresent()) {
            if (!itemRepository.findAll().isEmpty()) {
               //List<Item> itemList = itemRepository.findAllByTitleOrSubjectListOrProfessorListContaining(keyword).get();
                List<Item> itemList = itemRepository.findAll();
                for (Item item : itemList) {

                    boolean isKeywordMatched = false;

                    if(item.getTitle().contains(keyword) || keyword.contains(item.getTitle())) isKeywordMatched = true;


                    for(String subject : item.getSubjectList()){
                        if(subject.contains(keyword) || keyword.contains(subject)){
                            isKeywordMatched = true;
                            break;
                        }
                    }

                    for(String professor : item.getProfessorList()){
                        if(professor.contains(keyword) || keyword.contains(professor)){
                            isKeywordMatched = true;
                            break;
                        }
                    }

                    if(isKeywordMatched == false || item.getRegiCount() < 1) continue;

                    ItemRes itemRes = new ItemRes();

                    List<SellItem> sellItemList =
                            sellItemRepository.findAllByItemIdAndIsTraded(item.getItemId(), false).get();

                    SellItem sellItem = sellItemList.get(0);

                    itemIdList.add(sellItem.getItemId());
                    itemRes.setItemId(sellItem.getItemId());
                    itemRes.setPrice(sellItem.getPrice());

                    int minRegiPrice =
                            Integer.parseInt(sellItemList.get(0).getRegiPrice());

                    for (SellItem temp : sellItemList) {
                        if (Integer.parseInt(temp.getRegiPrice()) < minRegiPrice) {
                            minRegiPrice = Integer.parseInt(temp.getRegiPrice());
                        }
                    }

                    itemRes.setRegiPrice(String.valueOf(minRegiPrice));

                    itemRes.setTitle(sellItem.getTitle());
                    itemRes.setPubdate(sellItem.getPubdate());
                    itemRes.setPublisher(sellItem.getPublisher());
                    itemRes.setAuthor(sellItem.getAuthor());
                    itemRes.setImageUrl(sellItem.getImageUrl());
                    itemRes.setRegiCount(item.getRegiCount());
                    itemResList.add(itemRes);
                }
            }

            sortSearchedItemResList(itemResList, itemResListSortType, keyword);
            ItemAllRes itemAllRes =
                    ItemAllRes.builder().itemResList(itemResList).itemNotRegisteredResList(new ArrayList<>()).build();

            List<ItemRes> tempItemList = itemResList;

            if (getAllBooksFromNaverBookApi(keyword).equals("")) {
                return new ResponseEntity<>(itemAllRes, HttpStatus.OK);
            }


            String booksFromNaverBookApi = getAllBooksFromNaverBookApi(keyword);
            JSONParser parser = new JSONParser();

            org.json.simple.JSONArray jsonArray = null;
            Object obj = parser.parse(booksFromNaverBookApi);

            obj = ((org.json.simple.JSONObject) obj).get("rss");
            obj = ((org.json.simple.JSONObject) obj).get("channel");
            obj = ((org.json.simple.JSONObject) obj).get("item");
            jsonArray = (org.json.simple.JSONArray) obj;

            for (int i = 0; i < jsonArray.size(); i++) {
                ItemRes itemRes = new ItemRes();
                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) jsonArray.get(i);

                if (jsonObject.get("isbn").toString().length() > 11) {
                    String itemId = jsonObject.get("isbn").toString().substring(11);
                    boolean isDoubled = false;
                    for (ItemRes tempItemRes : tempItemList) {
                        if (tempItemRes.getItemId().equals(itemId)) {
                            isDoubled = true;
                            break;
                        }
                    }

                    if (isDoubled == true) continue;

                    itemRes.setItemId(itemId);
                }

                if (itemReceivingRepository.findByItemId(itemRes.getItemId()).isPresent()) {
                    itemRes.setItemReceivingRegistered(true);
                } else {
                    itemRes.setItemReceivingRegistered(false);
                }

                itemRes.setTitle(jsonObject.get("title").toString()
                        .replace("<b>", "").replace("</b>", ""));
                itemRes.setAuthor(jsonObject.get("author").toString()
                        .replace("<b>", "").replace("</b>", ""));
                itemRes.setPublisher(jsonObject.get("publisher").toString()
                        .replace("<b>", "").replace("</b>", ""));
                itemRes.setPubdate(jsonObject.get("pubdate").toString());
                itemRes.setImageUrl(jsonObject.get("image").toString());
                itemRes.setPrice(jsonObject.get("price").toString());
                itemRes.setRegiPrice("");

                itemNotRegisteredResList.add(itemRes);
            }

            sortSearchedItemNotRegisteredResList(itemNotRegisteredResList, itemNotRegisteredResListSortType, keyword, true);

            itemAllRes.setItemNotRegisteredResList(itemNotRegisteredResList);

            return new ResponseEntity<>(itemAllRes, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("naver/bookApi/buy/isbn")
    public ResponseEntity getAllRegisteredBuyItemsByIsbn(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        try {

            List<String> itemIdList = new ArrayList<>();
            List<ItemRes> itemResList = new ArrayList<>();

            if (itemRepository.findAllByTitleContaining(keyword).isPresent()) {

                List<Item> itemList = itemRepository.findAllByTitleContaining(keyword).get();

                for (Item item : itemList) {

                    if (item.getRegiCount() < 1) continue;

                    ItemRes itemRes = new ItemRes();

                    List<SellItem> sellItemList =
                            sellItemRepository.findAllByItemIdAndIsTraded(item.getItemId(), false).get();

                    SellItem sellItem = sellItemList.get(0);

                    itemIdList.add(sellItem.getItemId());
                    itemRes.setItemId(sellItem.getItemId());
                    itemRes.setPrice(sellItem.getPrice());

                    int minRegiPrice =
                            Integer.parseInt(sellItemList.get(0).getRegiPrice());

                    for (SellItem temp : sellItemList) {
                        if (Integer.parseInt(temp.getRegiPrice()) < minRegiPrice) {
                            minRegiPrice = Integer.parseInt(temp.getRegiPrice());
                        }
                    }

                    itemRes.setRegiPrice(String.valueOf(minRegiPrice));

                    itemRes.setTitle(sellItem.getTitle());
                    itemRes.setPubdate(sellItem.getPubdate());
                    itemRes.setPublisher(sellItem.getPublisher());
                    itemRes.setAuthor(sellItem.getAuthor());
                    itemRes.setImageUrl(sellItem.getImageUrl());
                    itemRes.setRegiCount(item.getRegiCount());
                    itemResList.add(itemRes);
                }
            }

            List<ItemRes> tempItemList = itemResList;

            if (getAllBooksFromNaverBookApi(keyword).equals("")) {
                return new ResponseEntity<>(itemResList, HttpStatus.OK);
            }

            String booksFromNaverBookApi = getAllBooksFromNaverBookApiAdv(keyword);
            JSONParser parser = new JSONParser();

            org.json.simple.JSONArray jsonArray = null;

            try {

                Object obj = parser.parse(booksFromNaverBookApi);

                obj = ((org.json.simple.JSONObject) obj).get("rss");
                obj = ((org.json.simple.JSONObject) obj).get("channel");
                obj = ((org.json.simple.JSONObject) obj).get("item");
                ItemRes itemRes = new ItemRes();
                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;

                if (jsonObject.get("isbn").toString().length() > 11) {
                    String itemId = jsonObject.get("isbn").toString().substring(11);
                    boolean isDoubled = false;
                    for (ItemRes tempItemRes : tempItemList) {
                        if (tempItemRes.getItemId().equals(itemId)) {
                            isDoubled = true;
                            break;
                        }
                    }

                    itemRes.setItemId(itemId);

                }

                itemRes.setTitle(jsonObject.get("title").toString()
                        .replace("<b>", "").replace("</b>", ""));
                itemRes.setAuthor(jsonObject.get("author").toString()
                        .replace("<b>", "").replace("</b>", ""));
                itemRes.setPublisher(jsonObject.get("publisher").toString()
                        .replace("<b>", "").replace("</b>", ""));
                itemRes.setPubdate(jsonObject.get("pubdate").toString());
                itemRes.setImageUrl(jsonObject.get("image").toString());
                itemRes.setPrice(jsonObject.get("price").toString());
                itemRes.setRegiPrice("");
                itemResList.add(itemRes);
            } catch (ParseException e) {

            }

            return new ResponseEntity<>(itemResList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("naver/bookApi/sell/title")
    public ResponseEntity getAllSearchedSellItemsByTitle(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                         @RequestParam(value = "sortType", defaultValue = "accurate") String sortType) {
        try {

            String booksFromNaverBookApi = getAllBooksFromNaverBookApi(keyword);
            JSONParser parser = new JSONParser();

            org.json.simple.JSONArray jsonArray = null;

            List<ItemRes> itemResList = new ArrayList<>();

            try {
                Object obj = parser.parse(booksFromNaverBookApi);
                obj = ((org.json.simple.JSONObject) obj).get("rss");
                obj = ((org.json.simple.JSONObject) obj).get("channel");
                obj = ((org.json.simple.JSONObject) obj).get("item");
                jsonArray = (org.json.simple.JSONArray) obj;

                for (int i = 0; i < jsonArray.size(); i++) {
                    ItemRes itemRes = new ItemRes();
                    org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) jsonArray.get(i);
                    itemRes.setItemId(jsonObject.get("isbn").toString().substring(11));
                    itemRes.setTitle(jsonObject.get("title").toString()
                            .replace("<b>", "").replace("</b>", ""));
                    itemRes.setAuthor(jsonObject.get("author").toString()
                            .replace("<b>", "").replace("</b>", ""));
                    itemRes.setPublisher(jsonObject.get("publisher").toString()
                            .replace("<b>", "").replace("</b>", ""));
                    itemRes.setPubdate(jsonObject.get("pubdate").toString());
                    itemRes.setImageUrl(jsonObject.get("image").toString());
                    itemRes.setPrice(jsonObject.get("price").toString());
                    itemRes.setRegiPrice("");
                    itemResList.add(itemRes);
                }
                sortSearchedItemNotRegisteredResList(itemResList, sortType, keyword, false);
            } catch (ParseException e) {

            }
            return new ResponseEntity<>(itemResList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("naver/bookApi/sell/isbn")
    public ResponseEntity getAllSearchedSellItemsByIsbn(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        try {

            String booksFromNaverBookApi = getAllBooksFromNaverBookApiAdv(keyword);
            JSONParser parser = new JSONParser();

            List<ItemRes> itemResList = new ArrayList<>();

            try {
                Object obj = parser.parse(booksFromNaverBookApi);
                obj = ((org.json.simple.JSONObject) obj).get("rss");
                obj = ((org.json.simple.JSONObject) obj).get("channel");
                obj = ((org.json.simple.JSONObject) obj).get("item");

                ItemRes itemRes = new ItemRes();
                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
                itemRes.setItemId(jsonObject.get("isbn").toString().substring(11));
                itemRes.setTitle(jsonObject.get("title").toString()
                        .replace("<b>", "").replace("</b>", ""));
                itemRes.setAuthor(jsonObject.get("author").toString()
                        .replace("<b>", "").replace("</b>", ""));
                itemRes.setPublisher(jsonObject.get("publisher").toString()
                        .replace("<b>", "").replace("</b>", ""));
                itemRes.setPubdate(jsonObject.get("pubdate").toString());
                itemRes.setImageUrl(jsonObject.get("image").toString());
                itemRes.setPrice(jsonObject.get("price").toString());
                itemRes.setRegiPrice("");
                itemResList.add(itemRes);

            } catch (ParseException e) {

            }
            return new ResponseEntity<>(itemResList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    public String getAllBooksFromNaverBookApi(final String keyword) {
        try {
            String text = URLEncoder.encode(keyword, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/book.xml?query=" + text + "&display=30"; // json 결과
            //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            JSONObject jsonObject = XML.toJSONObject(response.toString());

            JSONObject rss;
            if (!jsonObject.isNull("rss")) {
                rss = jsonObject.getJSONObject("rss");
            } else {
                return "";
            }
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAllBooksFromNaverBookApiAdv(final String isbn) {
        try {
            String text = URLEncoder.encode(isbn, "UTF-8");
            //String apiURL = "https://openapi.naver.com/v1/search/book.json?query="+ text; // json 결과
            //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
            String apiURL = "https://openapi.naver.com/v1/search/book_adv.xml?d_isbn=" + text + "&display=30";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            JSONObject jsonObject = XML.toJSONObject(response.toString());

            JSONObject rss;
            if (!jsonObject.isNull("rss")) {
                rss = jsonObject.getJSONObject("rss");
            } else {
                return "";
            }

            return jsonObject.toString();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void sortSearchedItemResList(List<ItemRes> itemResList, String sortType, String keyword) {

        // 1) 정확도순 : 일치도 -> 판매량
        if (sortType.equals("accuracy")) {
            Collections.sort(itemResList, (o1, o2) -> {
                if (StringUtils.countMatches(o1.getTitle(), keyword)
                        > StringUtils.countMatches(o2.getTitle(), keyword)) {
                    return -1;
                } else if (StringUtils.countMatches(o1.getTitle(), keyword)
                        == StringUtils.countMatches(o2.getTitle(), keyword)) {
                    if (o1.getRegiCount() >= o2.getRegiCount()) return -1;
                    else return 1;
                }
                return 1;
            });
        }
        // 2) 판매량순 : 판매량 -> 출시일
        else if (sortType.equals("regiCount")) {
            Collections.sort(itemResList, (o1, o2) -> {
                if (o1.getRegiCount() > o2.getRegiCount()) return -1;
                else if (o1.getRegiCount() == o2.getRegiCount()) {
                    if (Integer.parseInt(o1.getPubdate()) >= Integer.parseInt(o2.getPubdate())) {
                        return -1;
                    } else return 1;
                }
                return 1;
            });
        } else if (sortType.equals("pubdate")) {
            Collections.sort(itemResList, (o1, o2) -> {
                if (Integer.parseInt(o1.getPubdate()) > Integer.parseInt(o2.getPubdate())) return -1;
                else if (Integer.parseInt(o1.getPubdate()) == Integer.parseInt(o2.getPubdate())) {
                    if (o1.getRegiCount() >= o2.getRegiCount()) {
                        return -1;
                    } else return 1;
                }
                return 1;
            });
        } else {
            Collections.sort(itemResList, (o1, o2) -> {
                if (Integer.parseInt(o1.getRegiPrice()) < Integer.parseInt(o2.getRegiPrice())) return -1;
                else if (Integer.parseInt(o1.getRegiPrice()) == Integer.parseInt(o2.getRegiPrice())) {
                    if (o1.getRegiCount() >= o2.getRegiCount()) {
                        return -1;
                    } else return 1;
                }
                return 1;
            });
        }
    }

    public void sortSearchedItemNotRegisteredResList(List<ItemRes> itemNotRegisteredResList, String sortType, String keyword, boolean isBuy) {

        // 1) 정확도순 : 일치도 -> 판매량
        if (sortType.equals("accuracy")) {
            if(isBuy){
                Collections.sort(itemNotRegisteredResList, (o1, o2) -> {
                    if (StringUtils.countMatches(o1.getTitle(), keyword)
                            > StringUtils.countMatches(o2.getTitle(), keyword)) {
                        return -1;
                    } else if (StringUtils.countMatches(o1.getTitle(), keyword)
                            == StringUtils.countMatches(o2.getTitle(), keyword)) {
                        if (sellItemHistoryRepository.countByItemId(o1.getItemId()) >=
                                sellItemHistoryRepository.countByItemId(o2.getItemId())) return -1;
                        else return 1;
                    }
                    return 1;
                });
            }
        }
        // 2) 판매량순 : 판매량 -> 출시일
        else if (sortType.equals("regiCount")) {
            Collections.sort(itemNotRegisteredResList, (o1, o2) -> {
                if (sellItemHistoryRepository.countByItemId(o1.getItemId()) >
                        sellItemHistoryRepository.countByItemId(o2.getItemId())) return -1;
                else if (sellItemHistoryRepository.countByItemId(o1.getItemId()) ==
                        sellItemHistoryRepository.countByItemId(o2.getItemId())) {
                    if (Integer.parseInt(o1.getPubdate()) >= Integer.parseInt(o2.getPubdate())) {
                        return -1;
                    } else return 1;
                }
                return 1;
            });
        } else if (sortType.equals("pubdate")) {
            Collections.sort(itemNotRegisteredResList, (o1, o2) -> {
                if (Integer.parseInt(o1.getPubdate()) > Integer.parseInt(o2.getPubdate())) return -1;
                else if (Integer.parseInt(o1.getPubdate()) == Integer.parseInt(o2.getPubdate())) {
                    if (sellItemHistoryRepository.countByItemId(o1.getItemId()) >=
                            sellItemHistoryRepository.countByItemId(o2.getItemId())){
                        return -1;
                    } else return 1;
                }
                return 1;
            });
        } else {
            Collections.sort(itemNotRegisteredResList, (o1, o2) -> {
                if (Integer.parseInt(o1.getPrice()) < Integer.parseInt(o2.getPrice())) return -1;
                else if (Integer.parseInt(o1.getPrice()) == Integer.parseInt(o2.getPrice())) {
                    if (sellItemHistoryRepository.countByItemId(o1.getItemId()) >=
                            sellItemHistoryRepository.countByItemId(o2.getItemId())){
                        return -1;
                    } else return 1;
                }
                return 1;
            });
        }
    }
}

