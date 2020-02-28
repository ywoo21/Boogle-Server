package kr.ant.booksharing.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRes {
    private String token;
    private boolean isAuthComplete;
}
