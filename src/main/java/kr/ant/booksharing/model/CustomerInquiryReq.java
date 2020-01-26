package kr.ant.booksharing.model;

import kr.ant.booksharing.domain.CustomerInquiry;
import kr.ant.booksharing.domain.User;
import lombok.Data;

@Data
public class CustomerInquiryReq {
    private CustomerInquiry customerInquiry;
    private String token;
}
