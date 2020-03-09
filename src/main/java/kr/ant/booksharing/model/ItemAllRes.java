package kr.ant.booksharing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAllRes {
    private List<ItemRes> itemResList;
    private List<ItemRes> itemNotRegisteredResList;
}
