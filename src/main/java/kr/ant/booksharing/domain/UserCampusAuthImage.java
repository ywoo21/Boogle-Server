package kr.ant.booksharing.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@Document("user_campus_auth_image")
public class UserCampusAuthImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String _id;

    private int userId;
    private String authImageUrl;
}
