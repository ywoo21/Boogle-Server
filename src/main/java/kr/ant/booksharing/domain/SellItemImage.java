package kr.ant.booksharing.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sell_item_image")
public class SellItemImage {
    private int sell_item_id;
    private String image_url;
}
