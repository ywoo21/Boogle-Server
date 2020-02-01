package kr.ant.booksharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.domain.Transaction;
import kr.ant.booksharing.model.BoogleBoxReq;
import kr.ant.booksharing.model.SellItemReq;
import kr.ant.booksharing.service.TransactionService;
import kr.ant.booksharing.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * 거래 정보 등록
     *
     * @return ResponseEntity
     */
    @Auth
    @PostMapping("")
    public ResponseEntity saveTransaction(@RequestBody final Transaction transaction,
                                          final HttpServletRequest httpServletRequest) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            transaction.setBuyerId(userIdx);
            return new ResponseEntity<>(transactionService.saveTransaction(transaction), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 거래 정보 목록 열람
     *
     * @return ResponseEntity
     */
    @GetMapping("/admin")
    public ResponseEntity getAllTransaction(){
        try {
            return new ResponseEntity<>(transactionService.findAllTransaction(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
