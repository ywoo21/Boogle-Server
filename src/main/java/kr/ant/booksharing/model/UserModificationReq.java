package kr.ant.booksharing.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserModificationReq {
    private String email;
    private String name;
    private String nickname;
    private String password;
    private String semester;
    private String major;
    private String phoneNumber;
}
