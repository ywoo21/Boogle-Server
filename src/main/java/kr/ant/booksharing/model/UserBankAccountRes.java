package kr.ant.booksharing.model;

import kr.ant.booksharing.domain.UserBankAccount;
import lombok.Data;

@Data
public class UserBankAccountRes {
    private UserBankAccount userBankAccount;
    private String bankName;
}
