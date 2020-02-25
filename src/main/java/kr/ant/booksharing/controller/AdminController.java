package kr.ant.booksharing.controller;

import kr.ant.booksharing.service.BoogleBoxService;
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
    private final BoogleBoxService boogleBoxService;

    public AdminController(final UserService userService,
                           final TransactionService transactionService,
                           final BoogleBoxService boogleBoxService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.boogleBoxService = boogleBoxService;
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
    @GetMapping("/transactions/stepTwo")
    public ResponseEntity getAllStepTwoTransaction(){
        try {
            return new ResponseEntity<>(transactionService.findAllStepTwoTransaction(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * step4 거래 정보 목록 열람
     *
     * @return ResponseEntity
     */
    @GetMapping("/transactions/stepFour")
    public ResponseEntity getAllStepFourTransaction(){
        try {
            return new ResponseEntity<>(transactionService.findAllStepFourTransaction(), HttpStatus.OK);
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

    /**
     * 북을박스 전체 정보 조회
     *
     * @return ResponseEntity
     */
    @GetMapping("/booglebox")
    public ResponseEntity getAllBoogleBox(){
        try{
            return new ResponseEntity<>(boogleBoxService.findAllBoogleBoxes(), HttpStatus.OK);
        } catch (Exception e){
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
