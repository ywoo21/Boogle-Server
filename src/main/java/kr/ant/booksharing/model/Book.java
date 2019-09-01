package kr.ant.booksharing.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;


public class Book {

    @Getter
    @Setter
    @ToString
    public static class BookReq {
        private int id;
        private String title;
        private String price;
        private String author;
        private String publisher;
        private String imageUrl;
        private String regiPrice;
        private String quality;
        private String deal;
        private List<String> place;
        private int boxNumber;
        private int boxPassword;
        private String placeToString;
        private String sellerName;
        private String sellerEmail;
        private int sellerId;
        private int state;
        private String doodle;
        private String damage;
        private String colored;
    }

    @Getter
    @Setter
    @ToString
    public static class BookRes {
        private int id;
        private String title;
        private String price;
        private String author;
        private String publisher;
        private String imageUrl;
        private String regiPrice;
        private String quality;
        private String deal;
        private String place;
        private int boxNumber;
        private int boxPassword;
        private int sellerId;
        private String sellerPhoneNumber;
        private Timestamp date;
        private Timestamp transDate;
        private int transState;
        private int state;
        private String doodle;
        private String damage;
        private String colored;

    }
}

