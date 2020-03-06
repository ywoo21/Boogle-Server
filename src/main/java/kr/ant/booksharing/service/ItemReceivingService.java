package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.ItemReceiving;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.repository.ItemReceivingRepository;
import kr.ant.booksharing.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemReceivingService {
    private final ItemReceivingRepository itemReceivingRepository;

    public ItemReceivingService(final ItemReceivingRepository itemReceivingRepository) {
        this.itemReceivingRepository = itemReceivingRepository;
    }

    /**
     * 물품 입고 정보 저장
     *
     * @param itemReceiving 물품 입고 정보
     * @return DefaultRes
     */
    public DefaultRes saveItemReceiving(final ItemReceiving itemReceiving) {
        try {
            itemReceivingRepository.save(itemReceiving);
            return DefaultRes.res(StatusCode.CREATED, "물품 입고 정보 저장 성공");
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "물품 입고 정보 저장 실패");
        }
    }

    /**
     * 물품 입고 정보 삭제
     *
     * @param userId
     * @param itemId
     * @return DefaultRes
     */
    public DefaultRes deleteItemReceivingByUserIdAndItemId(final int userId, final String itemId) {
        try {
            itemReceivingRepository.deleteByUserIdAndItemId(userId, itemId);
            return DefaultRes.res(StatusCode.CREATED, "물품 입고 정보 저장 성공");
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "물품 입고 정보 저장 실패");
        }
    }


    /**
     * 회원별 물품 입고 정보 조회
     *
     * @param userId 회원 고유 번호
     * @return DefaultRes
     */
    public DefaultRes findAllItemReceivingByUserId(final int userId) {
        try {

            List<ItemReceiving> itemReceivingList = new ArrayList<>();

            if (itemReceivingRepository.findByUserId(userId).isPresent()) {

                itemReceivingList =
                        itemReceivingRepository.findByUserId(userId).get();

            }

            return DefaultRes.res(StatusCode.CREATED, "회원별 물품 입고 정보 조회 성공", itemReceivingList);
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "회원별 물품 입고 정보 조회 실패");
        }
    }
}
