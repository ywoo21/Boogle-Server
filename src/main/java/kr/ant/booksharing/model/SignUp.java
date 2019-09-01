package kr.ant.booksharing.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

public class SignUp {
    @Getter
    @Setter
    @ToString
    public static class SignUpReq {
        private int id;
        private String name;
        private String email;
        private String password;
        private String phone;
        private String major_1;
        private String major_2;
    }
}
