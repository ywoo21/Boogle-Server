package kr.ant.booksharing.controller;

import kr.ant.booksharing.model.Book;
import kr.ant.booksharing.model.SignIn.SignInReq;
import kr.ant.booksharing.model.SignUp.SignUpReq;
import kr.ant.booksharing.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입
     *
     * @param signUpReq 회원 데이터
     * @return ResponseEntity
     */
    @RequestMapping(value = "/signup", method= RequestMethod.POST)
    public ResponseEntity signup(@RequestBody final SignUpReq signUpReq) {
        try {
            return new ResponseEntity<>(userService.saveUser(signUpReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 중복체크
     *
     * @param email 회원 데이터
     * @return ResponseEntity
     */
    @RequestMapping(value = "signup/validateEmail", method= RequestMethod.GET)
    public ResponseEntity signup(@RequestParam("email") final String email) {
        try {
            return new ResponseEntity<>(userService.checkEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 로그인
     *
     * @param signInReq 회원 데이터
     * @return ResponseEntity
     */
    @RequestMapping(value = "/signin", method= RequestMethod.POST)
    public ResponseEntity signin(@RequestBody final SignInReq signInReq) {
        try {
            return new ResponseEntity<>(userService.authUser(signInReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
