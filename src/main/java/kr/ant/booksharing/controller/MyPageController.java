package kr.ant.booksharing.controller;

import kr.ant.booksharing.service.MyPageService;
import kr.ant.booksharing.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("myPage")
public class MyPageController {
    private final MyPageService myPageService;

    public MyPageController(final MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    /**
     * 마이페이지 조회
     *
     * @return ResponseEntity
     */
    @Auth
    @GetMapping("")
    public ResponseEntity getMyPage(final HttpServletRequest httpServletRequest) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>(myPageService.findMyPage(userIdx) ,HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
