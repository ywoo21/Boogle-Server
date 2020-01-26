package kr.ant.booksharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.model.ImageFileReq;
import kr.ant.booksharing.model.SellItemReq;
import kr.ant.booksharing.service.SellItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static kr.ant.booksharing.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("sell")
public class SellItemController {
    private final SellItemService sellItemService;

    public SellItemController(final SellItemService sellItemService) {
        this.sellItemService = sellItemService;
    }

    /**
     * 판매 상품 조회
     *
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity getAllSellItems(@RequestParam(value="itemId", defaultValue="") String itemId) {
        try {
            return new ResponseEntity<>(sellItemService.findAllSellItems(itemId), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 판매 상품 상세 조회
     *
     * @return ResponseEntity
     */
    @GetMapping("/detail")
    public ResponseEntity getSellItem(@RequestParam(value="id", defaultValue="") String id) {
        try {
            return new ResponseEntity<>(sellItemService.findSellItem(id), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 상품 판매 등록
     *
     * @return ResponseEntity
     */
    @PostMapping("")
    public ResponseEntity saveItem(final SellItemReq sellItemReq,
                                   @RequestPart(value="imageFileList", required = false)
                                   final List<MultipartFile> imageFileList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SellItem sellItem = objectMapper.readValue(sellItemReq.getSellItemString(), SellItem.class);
            return new ResponseEntity<>(sellItemService.saveItem(sellItem,
                   imageFileList), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 상품 판매 등록
     *
     * @return ResponseEntity
     */
    @PostMapping("test")
    public ResponseEntity saveItem(final ImageFileReq imageFileReq) {
        try {
            return new ResponseEntity<>(sellItemService.saveImageInS3(imageFileReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
