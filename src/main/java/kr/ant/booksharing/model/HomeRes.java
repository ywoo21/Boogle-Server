package kr.ant.booksharing.model;

import kr.ant.booksharing.domain.SellItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class HomeRes {
    private List<SellItem> recentRegisteredSellItemList;
    private List<SellItem> hotDealSellItemList;
    private List<SellItem> popularSellItemList;
}
