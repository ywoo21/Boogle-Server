package kr.ant.booksharing.controller;

import kr.ant.booksharing.domain.Item;
import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.model.ItemRes;
import kr.ant.booksharing.repository.ItemRepository;
import kr.ant.booksharing.repository.SellItemRepository;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class NaverBookApiController {

    static final String clientId = "Uu3ZOIKWPC7H2xIE45QX";//애플리케이션 클라이언트 아이디값";
    static final String clientSecret = "gfEQG2XKng";//애플리케이션 클라이언트 시크릿값";

    private final SellItemRepository sellItemRepository;
    private final ItemRepository itemRepository;

    public NaverBookApiController(final SellItemRepository sellItemRepository,
                                  final ItemRepository itemRepository) {
        this.sellItemRepository = sellItemRepository;
        this.itemRepository = itemRepository;
    }

    @GetMapping("naver/bookApi/buy")
    public ResponseEntity getAllSearchedBuyItems(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        try {

            List<String> itemIdList = new ArrayList<>();
            List<ItemRes> itemResList = new ArrayList<>();

            if (itemRepository.findAllByTitleContaining(keyword).isPresent()) {
                List<Item> itemList = itemRepository.findAllByTitleContaining(keyword).get();

                List<SellItem> sellItemList = new ArrayList<>();

                for(Item item : itemList){
                    if(sellItemRepository.findAllByItemId(item.getItemId()).isPresent()){
                        sellItemList.addAll(sellItemRepository.findAllByItemId(item.getItemId()).get());
                    }
                }

                for (SellItem sellItem : sellItemList) {
                    ItemRes itemRes = new ItemRes();
                    itemIdList.add(sellItem.getItemId());
                    itemRes.setItemId(sellItem.getItemId());
                    itemRes.setPrice(sellItem.getPrice());
                    itemRes.setRegiPrice(sellItem.getRegiPrice());
                    itemRes.setTitle(sellItem.getTitle());
                    itemRes.setPubdate(sellItem.getPubdate());
                    itemRes.setPublisher(sellItem.getPublisher());
                    itemRes.setAuthor(sellItem.getAuthor());
                    itemRes.setImageUrl(sellItem.getImageUrl());
                    itemRes.setRegiCount(itemRepository.findByItemId(sellItem.getItemId()).get().getRegiCount());
                    itemResList.add(itemRes);
                }
            }

            String booksFromNaverBookApi = getAllBooksFromNaverBookApi(itemIdList, keyword);
            JSONParser parser = new JSONParser();

            org.json.simple.JSONArray jsonArray = null;

            try {
                if(parser.parse(booksFromNaverBookApi) == null) {
                    return new ResponseEntity<>(itemResList, HttpStatus.OK);
                }
                Object obj = parser.parse(booksFromNaverBookApi);
                obj = ((org.json.simple.JSONObject)obj).get("rss");
                obj = ((org.json.simple.JSONObject)obj).get("channel");
                obj = ((org.json.simple.JSONObject)obj).get("item");
                jsonArray = (org.json.simple.JSONArray)obj;
                for (int i = 0; i < jsonArray.size(); i++) {
                    ItemRes itemRes = new ItemRes();
                    org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject)jsonArray.get(i);
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
            } catch (ParseException e) {

            }
            return new ResponseEntity<>(itemResList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("naver/bookApi/sell")
    public ResponseEntity getAllSearchedSellItems(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        try {

            String booksFromNaverBookApi = getAllBooksFromNaverBookApi(new ArrayList<>(), keyword);
            JSONParser parser = new JSONParser();

            org.json.simple.JSONArray jsonArray = null;

            List<ItemRes> itemResList = new ArrayList<>();

            try {
                Object obj = parser.parse(booksFromNaverBookApi);
                obj = ((org.json.simple.JSONObject)obj).get("rss");
                obj = ((org.json.simple.JSONObject)obj).get("channel");
                obj = ((org.json.simple.JSONObject)obj).get("item");
                jsonArray = (org.json.simple.JSONArray)obj;

                for (int i = 0; i < jsonArray.size(); i++) {
                    ItemRes itemRes = new ItemRes();
                    org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject)jsonArray.get(i);
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
            } catch (ParseException e) {

            }
            return new ResponseEntity<>(itemResList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    public String getAllBooksFromNaverBookApi(final List<String> itemIdList, final String keyword) {
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
            if (jsonObject.getJSONObject("rss") != null) {
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

    @GetMapping("naver/bookApi/adv")
    public ResponseEntity getAllBooksAdv(@RequestParam(value = "isbn", defaultValue = "") String isbn) {
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
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}

