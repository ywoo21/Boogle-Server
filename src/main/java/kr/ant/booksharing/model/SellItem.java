package kr.ant.booksharing.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

public class SellItem {
    @Getter
    @Setter
    @ToString
    public static class SellItemReq{
        private int id;
        private int itemId;
        private String regiPrice;
        private int quality;
        private String deal;
        private Timestamp date;
    }

    public static class SellItemRes {
        private List<SellItem> sellItemList;
    }
}
