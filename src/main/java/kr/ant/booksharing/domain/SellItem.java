package kr.ant.booksharing.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "sell_item")
public class SellItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String itemId;
    private String title;
    private String author;
    private String publisher;
    private String pubdate;
    private String imageUrl;
    private String price;
    private String regiPrice;
    private int dealType;
    private int contactType;
    private String qualityIn;
    private String qualityOut;
    private int sellerId;
    private Date regiTime;
    private String comment;
}
