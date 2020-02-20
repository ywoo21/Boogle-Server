package kr.ant.booksharing.model;

import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.domain.User;
import lombok.Data;

@Data
public class SellItemRes {
    private SellItem sellItem;
    private User sellerUser;
    private boolean isBookmarked;
}