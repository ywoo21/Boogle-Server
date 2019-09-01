package kr.ant.booksharing.model;

import kr.ant.booksharing.service.JwtService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


public class SignIn {
    @Getter
    @Setter
    @ToString
    public static class SignInReq {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @ToString
    public static class SignInRes {
        private int id;
        private String email;
        private String password;
        private String name;
        private JwtService.TokenRes tokenRes;
    }
}