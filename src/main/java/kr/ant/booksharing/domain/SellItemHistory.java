package kr.ant.booksharing.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Document("sell_item_history")
public class SellItemHistory {
    @Id
    private String _id;

    private String itemId;
    private String title;
    private String author;
    private String publisher;
    private String pubdate;
    private String imageUrl;
    private String price;
    private String regiPrice;
    private String originalPrice;
    private List<String> regiImageUrlList;
    private int dealType;
    private List<Boolean> qualityIn;
    private List<Boolean> qualityOut;
    private int sellerId;
    private Date regiTime;
    private String comment;
    private boolean isTraded;
}
