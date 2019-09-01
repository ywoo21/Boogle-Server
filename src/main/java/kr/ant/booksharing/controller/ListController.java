package kr.ant.booksharing.controller;

import kr.ant.booksharing.model.Book.BookReq;
import kr.ant.booksharing.model.Book.BookRes;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.Transaction;
import kr.ant.booksharing.model.Transaction.TransactionReq;
import kr.ant.booksharing.service.ListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController

public class ListController {

    private final ListService listService;

    public ListController(final ListService listService) {
        this.listService = listService;
    }

    /**
     * 모든 책 목록 조회
     *
     * @return ResponseEntity
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity getAllBooks() {
        try {
            DefaultRes<List<BookRes>> defaultRes = listService.findAllBook();
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 검색 된 책 목록 조회
     *
     * @return ResponseEntitz
     */
    @RequestMapping("list/search")
    public ResponseEntity getSearchedBooks(@RequestParam(value="keyword", defaultValue="") String keyword) {
        try {
            DefaultRes<List<BookRes>> defaultRes = listService.findSearchedBook(keyword);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 책 상세 조회
     *
     * @return ResponseEntitz
     */
    @RequestMapping("list/adv")
    public ResponseEntity getSearchedBooks(@RequestParam(value="bookId", defaultValue="") int bookId) {
        try {
            DefaultRes<BookRes> defaultRes = listService.findAdvancedBook(bookId);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 구매 정보 조회
     *
     * @return ResponseEntity
     */
    @RequestMapping("trans/buylist")
    public ResponseEntity getMyBuylist(@RequestParam(value="keyword", defaultValue="") String keyword) {
        try {
            DefaultRes<List<BookRes>> defaultRes = listService.findSearchedBook(keyword);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 책 정보 저장
     *
     * @param bookReq 책 데이터
     * @return ResponseEntity
     */
    @RequestMapping(value = "/list/save", method=RequestMethod.POST)
    public ResponseEntity saveBook(@RequestBody final BookReq bookReq) {
        try {
            return new ResponseEntity<>(listService.saveBook(bookReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 거래 정보 저장
     *
     * @param transactionReq 거래 데이터
     * @return ResponseEntity
     */
    @RequestMapping(value = "/trans/save", method=RequestMethod.POST)
    public ResponseEntity saveTransactinon(@RequestBody final TransactionReq transactionReq) {
        try {
            listService.updateSellTransaction(transactionReq.getBookId());
            return new ResponseEntity<>(listService.saveTransaction(transactionReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
    /**
     * 회원 거래 정보 조회(구매)
     *
     * @param email 유저 이메일
     * @return ResponseEntity
     */
    @RequestMapping(value = "/trans/buyList", method=RequestMethod.GET)
    public ResponseEntity getBuyTransactinon(@RequestParam("email") String email) {
        try {
            return new ResponseEntity<>(listService.findBuyList(email), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 회원 거래 정보 조회(판매)
     *
     * @param email 유저 이메일
     * @return ResponseEntity
     */
    @RequestMapping(value = "/trans/sellList", method=RequestMethod.GET)
    public ResponseEntity getSellTransactinon(@RequestParam("email") String email) {
        try {
            return new ResponseEntity<>(listService.findSellList(email), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 구매 상태 변경(증가)
     *
     * @param email 유저 이메일
     * @return ResponseEntity
     */
    @RequestMapping(value = "/trans/buyList/updateState", method=RequestMethod.POST)
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
     * 판매 상태 변경(증가)
     *
     * @param bookId 책 id
     * @return ResponseEntity
     */
    @RequestMapping(value = "/trans/sellList/updateState", method=RequestMethod.POST)
    public ResponseEntity updateSellState
    (@RequestParam("bookId") int bookId) {
        try {
            return new ResponseEntity<>(listService.updateSellTransaction(bookId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 판매 책 삭제
     *
     * @param bookId 책 id
     * @return ResponseEntity
     */
    @RequestMapping(value = "/trans/buyList", method=RequestMethod.DELETE)
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
