package kr.ant.booksharing.model;

import kr.ant.booksharing.domain.CustomerInquiry;
import kr.ant.booksharing.domain.User;
import lombok.Data;

@Data
public class CustomerInquiryRes {
    private CustomerInquiry customerInquiry;
    private User user;
}
