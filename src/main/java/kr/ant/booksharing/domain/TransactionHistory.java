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
@Document("transaction_history")
public class TransactionHistory {

    @Id
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
}
