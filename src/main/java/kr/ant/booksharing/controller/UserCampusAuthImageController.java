package kr.ant.booksharing.controller;

import kr.ant.booksharing.model.UserCampusAuthImageReq;
import kr.ant.booksharing.service.UserCampusAuthImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("authImage")
public class UserCampusAuthImageController {
    private final UserCampusAuthImageService userCampusAuthImageService;

    public UserCampusAuthImageController(final UserCampusAuthImageService userCampusAuthImageService) {
        this.userCampusAuthImageService = userCampusAuthImageService;
    }

    /**
     * 학생 인증 사진 저장
     *
     * @return ResponseEntity
     */
    @PostMapping("")
    public ResponseEntity saveUserCampusAuthImage(UserCampusAuthImageReq userCampusAuthImageReq,
                                                  @RequestPart(value = "userCampusAuthImageFile")
                                                  final MultipartFile userCampusAuthImageFile) {
        try {
            if(userCampusAuthImageFile != null) userCampusAuthImageReq.setUserCampusAuthImageFile(userCampusAuthImageFile);
            return new ResponseEntity<>(userCampusAuthImageService.saveUserCampusAuthImage(userCampusAuthImageReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
