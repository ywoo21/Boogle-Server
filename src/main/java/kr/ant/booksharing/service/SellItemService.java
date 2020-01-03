package kr.ant.booksharing.service;

import kr.ant.booksharing.dao.ListMapper;
import kr.ant.booksharing.dao.SellItemMapper;
import kr.ant.booksharing.dao.UserMapper;
import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.model.Book;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.SellItem.SellItemRes;
import kr.ant.booksharing.repository.SellItemRepository;
import kr.ant.booksharing.utils.ResponseMessage;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SellItemService {

    private final SellItemRepository sellItemRepository;
    private final UserMapper userMapper;

    public SellItemService(final SellItemRepository sellItemRepository, final UserMapper userMapper) {
        this.sellItemRepository = sellItemRepository;
        this.userMapper =  userMapper;
    }

    /**
     * 책 상세 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<SellItem>> findSellItems(final int itemId) {
        if(sellItemRepository.findAllByItemId(itemId).isPresent()){
            List<SellItem> sellItemList = sellItemRepository.findAllByItemId(itemId).get();
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_SELL_ITEM, sellItemList);
        }
        else{
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_SELL_ITEM);
        }
    }
}
