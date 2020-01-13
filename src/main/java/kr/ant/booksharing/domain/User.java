package kr.ant.booksharing.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;
    private String password;

    private String checkList;
    private String name;
    private String nickname;
    private String campus;
    private String semester;
    private String major;
    private String phoneNumber;
}
