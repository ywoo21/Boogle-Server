package kr.ant.booksharing.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@Builder
@Document("item_receiving")
public class ItemReceiving {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String _id;

    private String itemId;
    private int userId;

    private String imageUrl;
    private String title;
    private String author;
    private String publisher;

}
