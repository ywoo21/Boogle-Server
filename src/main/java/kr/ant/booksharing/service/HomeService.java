package kr.ant.booksharing.service;

import kr.ant.booksharing.dao.ListMapper;
import kr.ant.booksharing.dao.UserMapper;
import kr.ant.booksharing.domain.Item;
import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.domain.Transaction;
import kr.ant.booksharing.model.Book.BookRes;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.HomeRes;
import kr.ant.booksharing.repository.ItemRepository;
import kr.ant.booksharing.repository.SellItemRepository;
import kr.ant.booksharing.utils.ResponseMessage;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HomeService {
    private final SellItemRepository sellItemRepository;
    private final ItemRepository itemRepository;

    public HomeService(final SellItemRepository sellItemRepository,
                       final ItemRepository itemRepository) {
        this.sellItemRepository = sellItemRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * 거래 삭제
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<List<SellItem>>> findAllHomeData() {
        try{

            List<List<SellItem>> homeDataList = new ArrayList<>();

            homeDataList.add(findRecentRegisteredSellItemList());
            homeDataList.add(findLowestPriceSellItemList());
            homeDataList.add(findMostRegisteredSellItemList());

            return DefaultRes.res(StatusCode.OK, "홈 정보 조회 성공", homeDataList);
        }
        catch(Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "홈 정보 조회 실패");
        }
    }

    /**
     * 모든 판매 상품 조회(최신순)
     *
     * @param
     * @return DefaultRes
     */
    public List<SellItem> findRecentRegisteredSellItemList() {
        List<SellItem> recentRegisteredSellItemList = new ArrayList<>();
        if(sellItemRepository.findTop4ByIsTradedOrderByRegiTimeDesc(false).isPresent()){
            recentRegisteredSellItemList.addAll(sellItemRepository.findTop4ByIsTradedOrderByRegiTimeDesc(false).get());
        }
        return recentRegisteredSellItemList;
    }

    /**
     * 모든 판매 상품 조회(저가순)
     *
     * @param
     * @return DefaultRes
     */
    public List<SellItem> findLowestPriceSellItemList() {
        List<SellItem> recentRegisteredSellItemList = new ArrayList<>();
        if(sellItemRepository.findTop4ByIsTradedOrderByRegiPriceAsc(false).isPresent()){
            recentRegisteredSellItemList.addAll(sellItemRepository.findTop4ByIsTradedOrderByRegiPriceAsc(false).get());
        }
        return recentRegisteredSellItemList;
    }

    /**
     * 모든 판매 상품 조회(등록수순)
     *
     * @param
     * @return DefaultRes
     */
    public List<SellItem> findMostRegisteredSellItemList() {

        List<SellItem> recentRegisteredSellItemList = new ArrayList<>();

        if(itemRepository.findByOrderByRegiCountDesc().isPresent()){
            List<Item> mostRegisteredItemList = itemRepository.findByOrderByRegiCountDesc().get();

            for(Item item : mostRegisteredItemList){
                if(recentRegisteredSellItemList.size() > 3) break;
                if(sellItemRepository.findAllByItemIdAndIsTraded(item.getItemId(), false).isPresent()){
                    recentRegisteredSellItemList.addAll(sellItemRepository.findAllByItemIdAndIsTraded(item.getItemId(), false).get());
                }
            }
        }

        recentRegisteredSellItemList =
                recentRegisteredSellItemList.stream().distinct().collect(Collectors.toList());

        return recentRegisteredSellItemList;
    }
}
