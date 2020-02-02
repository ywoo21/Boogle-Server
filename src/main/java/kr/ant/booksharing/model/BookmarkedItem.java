package kr.ant.booksharing.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookmarkedItem {
    private String sellItemId;
    private String imageUrl;
    private String title;
    private String regiPrice;
}
