package kr.ant.booksharing.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Data
@Document("customer_inquiry")
public class CustomerInquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String _id;

    private String email;
    private Integer type;
    private String msg;
    private List<String> imageUrlList;
    private Boolean isMember;
    private Boolean status;
    private Date date;
}
