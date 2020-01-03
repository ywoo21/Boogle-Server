package kr.ant.booksharing.controller;

import kr.ant.booksharing.model.Book;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.service.ListService;
import kr.ant.booksharing.service.SellItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("sell")
public class SellItemController {
    private final SellItemService sellItemService;

    public SellItemController(final SellItemService sellItemService) {
        this.sellItemService = sellItemService;
    }

    /**
     * 판매 도서 조회
     *
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity getAllSellItems(@RequestParam(value="itemId", defaultValue="") int itemId) {
        try {
            return new ResponseEntity<>(sellItemService.findSellItems(itemId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
