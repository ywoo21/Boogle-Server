package kr.ant.booksharing.controller;

import kr.ant.booksharing.domain.User;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.SignIn.SignInReq;
import kr.ant.booksharing.model.SignUp.SignUpReq;
import kr.ant.booksharing.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입
     *
     * @param user 회원 데이터
     * @return ResponseEntity
     */
    @PostMapping("signup")
    public ResponseEntity signup(@RequestBody final User user) {
        try {
            return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 회원가입 이메일 중복체크
     *
     * @param email 회원 이메일
     * @return ResponseEntity
     */
    @GetMapping("signup/validateEmail")
    public ResponseEntity validateEmail(@RequestParam("email") final String email) {
        try {
            return new ResponseEntity<>(userService.checkEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * 회원가입 닉네임 중복체크
     *
     * @param nickname 회원 닉네임
     * @return ResponseEntity
     */
    @GetMapping("signup/validateNickname")
    public ResponseEntity validateNickname(@RequestParam("nickname") final String nickname) {
        try {
            return new ResponseEntity<>(userService.checkNickname(nickname), HttpStatus.OK);
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
    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody final SignInReq signInReq) {
        try {
            return new ResponseEntity<>(userService.authUser(signInReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 이메일 인증
     *
     * @param email 회원 이메일 주소
     * @return ResponseEntity
     */
    @GetMapping("/signup/authNumber")
    public ResponseEntity emailAuth(@RequestParam("email") final String email,
                                    @RequestParam("campusEmail") final String campusEmail) {
        try {
            return new ResponseEntity<>(userService.sendAuthEmail(email, campusEmail), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 비밀번호 찾기
     *
     * @param signInReq 회원 데이터
     * @return ResponseEntity
     */
    @PostMapping("/change/password")
    public ResponseEntity changePassword(@RequestBody SignInReq signInReq) {
        try {
            return new ResponseEntity<>(userService.modifyPassword(signInReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
