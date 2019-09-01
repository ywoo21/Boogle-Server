package kr.ant.booksharing.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class Transaction {
    @Getter
    @Setter
    @ToString
    public static class TransactionReq {
        private int id;
        private int bookId;
        private String buyerEmail;
        private int buyerId;
        private int sellerId;
    }

    @Getter
    @Setter
    @ToString
    public static class TransactionRes {

    }
}
