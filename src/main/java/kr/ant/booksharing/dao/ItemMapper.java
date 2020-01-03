package kr.ant.booksharing.dao;

import kr.ant.booksharing.model.Item.ItemRes;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface ItemMapper {

    // ISBN으로 조회
    @Select("SELECT * FROM item WHERE isbn = #{isbn}")
    @Results(value = {
            @Result(property = "itemId", column = "id"),
            @Result(property = "isbn", column = "isbn"),
            @Result(property = "title", column = "title"),
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "author", column = "author"),
            @Result(property = "publisher", column = "publisher"),
            @Result(property = "lowestPrice", column = "lowest_price"),
            @Result(property = "registeredCount", column = "registered_count"),
    })
    public Optional<ItemRes> findAllByIsbn(@Param("isbn") final String isbn);
}