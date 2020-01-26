package kr.ant.booksharing.controller;

import kr.ant.booksharing.domain.Transaction;
import kr.ant.booksharing.service.UserBookmarkService;
import kr.ant.booksharing.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("bookmark")
public class UserBookmarkController{
    private final UserBookmarkService userBookmarkService;

    public UserBookmarkController(final UserBookmarkService userBookmarkService) {
        this.userBookmarkService = userBookmarkService;
    }

    /**
     * 찜하기 저장
     *
     * @return ResponseEntity
     */
    @Auth
    @GetMapping("")
    public ResponseEntity saveUserBookmark(@RequestParam("sellItemId") final String sellItemId,
                                           final HttpServletRequest httpServletRequest) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>(userBookmarkService.saveUserbookmark(userIdx, sellItemId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
