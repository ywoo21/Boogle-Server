package kr.ant.booksharing.model;

import lombok.Data;

@Data
public class BoogleBoxReq {
    private String _id;

    private String boxId;
    private String boxPassword;
}
