package kr.ant.booksharing.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;

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

    private Date sellerTime = new Date();
    private Date buyerTime = new Date();

    private int sellerStep;
    private int buyerStep;

    private String boxId;
    private String boxPassword;
}
