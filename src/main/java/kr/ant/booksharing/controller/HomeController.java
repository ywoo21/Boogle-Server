package kr.ant.booksharing.controller;

import kr.ant.booksharing.model.Book.BookRes;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.HomeRes;
import kr.ant.booksharing.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("home")
public class HomeController {
    private final HomeService homeService;

    public HomeController(final HomeService homeService) {
        this.homeService = homeService;
    }

    /**
     * 최근 등록 도서 목록 조회
     *
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity getAllBooks() {
        try {
            DefaultRes<HomeRes> homeRes = homeService.findRecentBook();
            return new ResponseEntity<>(homeRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
