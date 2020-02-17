package kr.ant.booksharing.controller;

import kr.ant.booksharing.service.TransactionService;
import kr.ant.booksharing.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final TransactionService transactionService;

    public AdminController(final UserService userService,
                           final TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    /**
     * 회원 정보 목록 열람
     *
     * @param
     * @return ResponseEntity
     */
    @GetMapping("/users")
    public ResponseEntity getAllUsers(){
        try{
            return new ResponseEntity<> (userService.findAllUsers(), HttpStatus.OK);
        } catch (Exception e){
            log.error("{}", e);
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 전체 거래 정보 목록 열람
     *
     * @return ResponseEntity
     */
    @GetMapping("/transactions")
    public ResponseEntity getAllTransaction(){
        try {
            return new ResponseEntity<>(transactionService.findAllTransaction(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * step2 거래 정보 목록 열람
     *
     * @return ResponseEntity
     */
    @GetMapping("/transactions/step_two")
    public ResponseEntity getAllStepTwoTransaction(){
        try {
            return new ResponseEntity<>(transactionService.findAllStepTwoTransaction(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * step5 거래 정보 목록 열람
     *
     * @return ResponseEntity
     */
    @GetMapping("/transactions/step_five")
    public ResponseEntity getAllStepFiveTransaction(){
        try {
            return new ResponseEntity<>(transactionService.findAllStepFiveTransaction(), HttpStatus.OK);
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
    @GetMapping("/change_step")
    public ResponseEntity changeTransactionStep(@RequestParam("sellItemId")final String sellItemId) {
        try {
            return new ResponseEntity<>(transactionService.changeTransactionStep(sellItemId),HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
