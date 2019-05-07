package kr.ant.booksharing.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


public class Book {
    @Getter
    @Setter
    @ToString
    public static class BookRes {
        private int id;
        private String title;
        private String price;
    }
}

