package kr.ant.booksharing.controller;

import kr.ant.booksharing.model.Book.BookRes;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.service.ListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("/list")
@CrossOrigin

public class ListController {

    //@NonNull
    private final ListService listService;

    /**
     * @RequiredArgsConstructor가 @NonNull이 붙은 필드의 생성자 자동 생성
     */

    /**
     * 생성자가 1개일 경우 @Autowired 생략 가능
     * @param listService 서비스
     */
    public ListController(final ListService listService) {
        this.listService = listService;
    }

    /**
     * 모든 책 목록 조회
     *
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity getAllBoards() {
        try {
            DefaultRes<List<BookRes>> defaultRes = listService.findAllBook();
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 검색 된 책 목록 조회
     *
     * @return ResponseEntity
     */
    @RequestMapping("/search")
    public ResponseEntity getSearchedBoards(@RequestParam(value="keyword", defaultValue="") String keyword) {
        try {
            DefaultRes<List<BookRes>> defaultRes = listService.findSearchedBook(keyword);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }


}
