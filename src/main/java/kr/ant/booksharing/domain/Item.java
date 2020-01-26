package kr.ant.booksharing.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@Document("item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String _id;

    private String itemId;
    private String title;
    private int regiCount;
}
