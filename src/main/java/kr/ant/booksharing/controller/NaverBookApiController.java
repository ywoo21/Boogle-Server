package kr.ant.booksharing.controller;

import kr.ant.booksharing.dao.ItemMapper;
import kr.ant.booksharing.model.Book;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.Item.ItemRes;
import kr.ant.booksharing.service.ListService;
import kr.ant.booksharing.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class NaverBookApiController {

    static final String clientId = "Uu3ZOIKWPC7H2xIE45QX";//애플리케이션 클라이언트 아이디값";
    static final String clientSecret = "gfEQG2XKng";//애플리케이션 클라이언트 시크릿값";

    private final ItemMapper itemMapper;

    public NaverBookApiController(final ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }


    @GetMapping("naver/bookApi")
    public ResponseEntity getAllBooks(@RequestParam(value="keyword", defaultValue="") String keyword) {
        try {
            String text = URLEncoder.encode(keyword, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/book.xml?query="+ text+"&display=30"; // json 결과
            //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
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
            if(jsonObject.getJSONObject("rss") != null){
                rss = jsonObject.getJSONObject("rss");
            }
            else{
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            JSONArray jsonArray =
                    rss.getJSONObject("channel").getJSONArray("item");

            for(int i = 0; i < jsonArray.length(); i++){
                String isbnOfSearchedResult = jsonArray.getJSONObject(i)
                        .getString("isbn").split(" ")[1];
                String lowestPrice = "";
                int registeredCount = 0;
                int itemId = 0;

                if(itemMapper.findAllByIsbn(isbnOfSearchedResult).isPresent()){
                    ItemRes itemRes = itemMapper.findAllByIsbn(isbnOfSearchedResult).get();
                    lowestPrice = itemRes.getLowestPrice();
                    registeredCount = itemRes.getRegisteredCount();
                    itemId = itemRes.getItemId();
                }
                jsonArray.getJSONObject(i).put("lowestPrice", lowestPrice);
                jsonArray.getJSONObject(i).put("registeredCount", registeredCount);
                jsonArray.getJSONObject(i).put("itemId", itemId);
            }


            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("naver/bookApi/adv")
    public ResponseEntity getAllBooksAdv(@RequestParam(value="isbn", defaultValue="") String isbn) {
        try {
            String text = URLEncoder.encode(isbn, "UTF-8");
            //String apiURL = "https://openapi.naver.com/v1/search/book.json?query="+ text; // json 결과
            //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
            String apiURL = "https://openapi.naver.com/v1/search/book_adv.xml?d_isbn="+ text+"&display=30";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
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

