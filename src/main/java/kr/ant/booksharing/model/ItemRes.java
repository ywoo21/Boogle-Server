package kr.ant.booksharing.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ItemRes {
    private String itemId;
    private String title;
    private String author;
    private String publisher;
    private String pubdate;
    private String imageUrl;
    private String price;
    private String regiPrice;
    private int regiCount;
    private boolean isItemReceivingRegistered;
}
