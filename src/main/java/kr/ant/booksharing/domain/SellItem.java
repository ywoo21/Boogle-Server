package kr.ant.booksharing.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "sell_item")
public class SellItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int itemId;
    private String regiPrice;
    private int quality;
    private String deal;
    private Timestamp date;
    private int state;
}
