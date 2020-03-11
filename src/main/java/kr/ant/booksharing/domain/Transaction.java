package kr.ant.booksharing.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Document("transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String _id;

    private String sellItemId;
    private int sellerId;
    private int buyerId;

    private int transactionType;

    private List<Date> transactionTimeList;

    private Date transCreatedTime;

    private int step;

    private String boxId;
    private String boxPassword;

    private boolean isPaymentDone;
}
