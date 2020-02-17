package kr.ant.booksharing.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class TransactionItem {
    private String sellItemId;
    private String traderName;
    private String traderPhoneNumber;
    private String title;
    private int transactionType;
    private String transPrice;
    private String itemImageUrl;
    private String boxId;
    private String boxPassword;
    private Date transactionCreatedTime;
    private List<Date> transactionProcessedTimeList;
    private int transactionStep;
}
