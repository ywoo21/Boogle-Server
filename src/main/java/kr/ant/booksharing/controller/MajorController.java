package kr.ant.booksharing.controller;

import kr.ant.booksharing.domain.Major;
import kr.ant.booksharing.service.MajorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("majors")
public class MajorController {

    private final MajorService majorService;

    public MajorController(final MajorService majorService) {

        this.majorService = majorService;

    }

    /**
     * 캠퍼스별 전체 전공 저장
     *
     * @param major 캠퍼스별 전체 전공
     * @return ResponseEntity
     */
    @PostMapping("")
    public ResponseEntity saveAllCampusMajor(@RequestBody final Major major) {
        try {
            return new ResponseEntity<>(majorService.saveAllCampusMajor(major), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 캠퍼스별 전공 찾기
     *
     * @param campus
     * @param keyword
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity searchCampusMajor(@RequestParam("campus") final String campus,
                                            @RequestParam("keyword") final String keyword) {
        try {
            return new ResponseEntity<>(majorService.findAllMajorByCampusAndKeyword(campus,keyword), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
