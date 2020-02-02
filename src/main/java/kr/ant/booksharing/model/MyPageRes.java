package kr.ant.booksharing.model;

import kr.ant.booksharing.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class MyPageRes {
    private String userName;
    private List<BookmarkedItem> bookmarkedItemList;
    private List<TransactionItem> buyTransList;
    private List<TransactionItem> sellTransList;
}
