package kr.ant.booksharing.controller;

import kr.ant.booksharing.domain.Bank;
import kr.ant.booksharing.domain.Major;
import kr.ant.booksharing.domain.UserBankAccount;
import kr.ant.booksharing.repository.BankRepository;
import kr.ant.booksharing.service.BankService;
import kr.ant.booksharing.service.UserBankAccountService;
import kr.ant.booksharing.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("userBankAccount")
public class UserBankAccountController {

    private final UserBankAccountService userBankAccountService;

    public UserBankAccountController(final UserBankAccountService userBankAccountService){
        this.userBankAccountService = userBankAccountService;
    }

    /**
     * 계좌 정보 저장
     *
     * @param userBankAccount 계좌 정보
     * @return DefaultRes
     */
    @Auth
    @PostMapping("")
    public ResponseEntity saveUserBankAccount(@RequestBody final UserBankAccount userBankAccount,
                                              final HttpServletRequest httpServletRequest) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            userBankAccount.setUserId(userIdx);
            return new ResponseEntity<>(userBankAccountService.saveUserBankAccount(userBankAccount), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 계좌 정보 조회
     *
     * @return ResponseEntity
     */
    @Auth
    @GetMapping("")
    public ResponseEntity getAllBankInfo(final HttpServletRequest httpServletRequest) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>
                    (userBankAccountService.findAllUserBankAccountByUserId(userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 계좌 정보 조회
     *
     * @param userBankAccountId 계좌 정보 고유 번호
     * @return ResponseEntity
     */
    @Auth
    @DeleteMapping("")
    public ResponseEntity deleteUserBankAccount(@RequestParam("userBankAccountId") final String userBankAccountId) {
        try {
            return new ResponseEntity<>
                    (userBankAccountService.deleteUserBankAccount(userBankAccountId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
