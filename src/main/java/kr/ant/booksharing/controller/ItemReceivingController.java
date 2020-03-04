package kr.ant.booksharing.controller;

import kr.ant.booksharing.domain.Item;
import kr.ant.booksharing.domain.ItemReceiving;
import kr.ant.booksharing.service.ItemReceivingService;
import kr.ant.booksharing.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("itemReceiving")
public class ItemReceivingController {

    private final ItemReceivingService itemReceivingService;

    public ItemReceivingController(final ItemReceivingService itemReceivingService) {

        this.itemReceivingService = itemReceivingService;

    }

    /**
     * 물품 입고 알림 활성화
     *
     * @param itemReceiving
     * @return ResponseEntity
     */
    @Auth
    @PostMapping("")
    public ResponseEntity saveItemReceiving(@RequestBody final ItemReceiving itemReceiving,
                                            final HttpServletRequest httpServletRequest) {
        try {

            itemReceiving.setUserId((int) httpServletRequest.getAttribute("userIdx"));

            return new ResponseEntity<>(itemReceivingService.saveItemReceiving(itemReceiving), HttpStatus.OK);

        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 물품 입고 알림 비활성화
     *
     * @param itemId 물품 고유 번호
     * @return ResponseEntity
     */
    @Auth
    @GetMapping("/cancel")
    public ResponseEntity cancelItemReceiving(@RequestParam("itemId") final String itemId,
                                            final HttpServletRequest httpServletRequest) {
        try {

            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");

            return new ResponseEntity<>(itemReceivingService.deleteItemReceivingByUserIdAndItemId(userIdx, itemId), HttpStatus.OK);

        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 회원 별 물품 입고 알림 전체 조회
     *
     * @return ResponseEntity
     */
    @Auth
    @GetMapping("")
    public ResponseEntity getUserItemReceiving(final HttpServletRequest httpServletRequest) {
        try {

            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");

            return new ResponseEntity<>(itemReceivingService.findAllItemReceivingByUserId(userIdx), HttpStatus.OK);

        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

