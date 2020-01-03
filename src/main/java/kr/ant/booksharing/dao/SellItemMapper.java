package kr.ant.booksharing.dao;

import kr.ant.booksharing.model.Item.ItemRes;
import kr.ant.booksharing.model.SellItem.SellItemRes;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SellItemMapper {

    @Select("SELECT * FROM sell_item WHERE item_id = #{itemId}")
    @Results(value = {
            @Result(property = "sellItemId", column = "id"),
            @Result(property = "itemIdforSellItem", column = "item_id"),
            @Result(property = "regiPrice", column = "regi_price"),
            @Result(property = "quality", column = "quality"),
            @Result(property = "deal", column = "deal"),
            @Result(property = "date", column = "date"),
    })
    public Optional<List<SellItemRes>> findAllByItemId(@Param("itemId") final int itemId);
}
