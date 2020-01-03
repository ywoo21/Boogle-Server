package kr.ant.booksharing.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class Item {

    @Getter
    @Setter
    @ToString
    public static class ItemReq {
        private String isbn;
        private String title;
        private String imageUrl;
        private String author;
        private String publisher;
    }

    @Getter
    @Setter
    @ToString
    public static class ItemRes {
        private int itemId;
        private String isbn;
        private String title;
        private String imageUrl;
        private String author;
        private String publisher;
        private String lowestPrice;
        private int registeredCount;
    }
}

