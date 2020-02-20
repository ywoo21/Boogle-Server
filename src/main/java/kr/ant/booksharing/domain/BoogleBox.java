package kr.ant.booksharing.domain;

import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

public class BoogleBox {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    String _id;

    String boxId;
    String boxPassword;

    boolean isEmpty;
}
