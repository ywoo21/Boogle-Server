package kr.ant.booksharing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ant.booksharing.domain.RegiImage;
import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.model.ImageUrl;
import kr.ant.booksharing.model.RegiImageReq;
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
     * 판매 도서 조회
     *
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity getAllSellItems(@RequestParam(value="Id", defaultValue="") int id) {
        try {
            return new ResponseEntity<>(sellItemService.findSellItems(id), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 물품 판매 등록
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
                   imageFileList, sellItemReq.getRegiImageList()), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 물품 사진 S3 저장
     *
     * @return ResponseEntity
     */
    @PostMapping("/imageUrl")
    public ResponseEntity saveImaegUrlS3(ImageUrl imageUrl, @RequestPart(value = "image", required = false) final MultipartFile image) {
        try {
            return new ResponseEntity<>(sellItemService.saveImageUrl(imageUrl), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
