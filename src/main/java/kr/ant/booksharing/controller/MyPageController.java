package kr.ant.booksharing.controller;

import kr.ant.booksharing.service.ListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PastOrPresent;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("myPage")
public class MyPageController {

    private final ListService listService;

    public MyPageController(final ListService listService) {
        this.listService = listService;
    }

    /**
     * 마이페이지 구매 목록 조회
     *
     * @param email 회원 이메일
     * @return ResponseEntity
     */
    @GetMapping("buyList")
    public ResponseEntity getBuyTransactinon(@RequestParam("email") String email) {
        try {
            return new ResponseEntity<>(listService.findBuyList(email), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 마이페이지 판매 목록 조회
     *
     * @param email 회원 이메일
     * @return ResponseEntity
     */
    @GetMapping("sellList")
    public ResponseEntity getSellTransactinon(@RequestParam("email") String email) {
        try {
            return new ResponseEntity<>(listService.findSellList(email), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 마이페이지 구매 상태 변경(state 1 -> state 2)
     *
     * @param email 유저 이메일
     * @return ResponseEntity
     */
    @PostMapping("buyList/updateState")
    public ResponseEntity updateBuyState
    (@RequestParam("email") String email, @RequestParam("bookId") int bookId) {
        try {
            return new ResponseEntity<>(listService.updateBuyTransaction(email, bookId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 마이페이지 등록된 판매 도서 삭제
     *
     * @param bookId 책 id
     * @return ResponseEntity
     */
    @DeleteMapping("buyList")
    public ResponseEntity deleteBook
    (@RequestParam("bookId") int bookId) {
        try {
            listService.deleteBook(bookId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
