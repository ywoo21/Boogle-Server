package kr.ant.booksharing.model;

import kr.ant.booksharing.domain.SellItem;
import lombok.Data;

import java.util.List;

@Data
public class BookApiBuyRes {
    List<SellItem> sellItemList;
    String booksFromNaverApi;
}
