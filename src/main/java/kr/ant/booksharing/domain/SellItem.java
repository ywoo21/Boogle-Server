package kr.ant.booksharing.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import kr.ant.booksharing.enums.QualityExtra;
import kr.ant.booksharing.enums.QualityGeneral;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@Document("sell_item")
public class SellItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private QualityGeneral qualityGeneral;
    private List<Boolean> qualityExtraList;
    private int sellerId;

    private Date regiTime;

    private String comment;
    private boolean isTraded;
    private String sellerBankAccountId;

    private String subject;
    private String professor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SellItem sellItem = (SellItem) o;
        return itemId.equals(sellItem.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
