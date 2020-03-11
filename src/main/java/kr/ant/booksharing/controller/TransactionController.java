package kr.ant.booksharing.controller;

import kr.ant.booksharing.domain.Transaction;
import kr.ant.booksharing.model.BoogleBoxInfo;
import kr.ant.booksharing.model.SellItemReq;
import kr.ant.booksharing.service.TransactionService;
import kr.ant.booksharing.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
     * 거래 STEP 변경
     *
     * @return ResponseEntity
     */
    @GetMapping("/step")
    public ResponseEntity changeTransactionStep(@RequestParam("sellItemId")final String sellItemId) {
        try {
            return new ResponseEntity<>(transactionService.changeTransactionStep(sellItemId),HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 북을 박스 정보 저장
     *
     * @return ResponseEntity
     */
    @PostMapping("/booglebox")
    public ResponseEntity changeTransactionStep(@RequestBody BoogleBoxInfo boogleBoxInfo) {
        try {
            return new ResponseEntity<>(transactionService.saveBoogleBoxIdAndPasswordAndChangeStep(boogleBoxInfo),HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 결제 완료 상태 저장 및
     *
     * @return ResponseEntity
     */
    @GetMapping("/payment")
    public ResponseEntity savePaymentDone(@RequestParam("sellItemId") String sellItemId) {
        try {
            return new ResponseEntity<>(transactionService.savePaymentDoneAndChangeStep(sellItemId),HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 거래 취소
     *
     * @return ResponseEntity
     */
    @DeleteMapping("")
    public ResponseEntity cancelTransaction(@RequestParam("sellItemId")final String sellItemId) {
        try {
            return new ResponseEntity<>(transactionService.deleteTransaction(sellItemId),HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
