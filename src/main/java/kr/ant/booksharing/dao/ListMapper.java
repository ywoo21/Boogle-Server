package kr.ant.booksharing.dao;

import kr.ant.booksharing.model.Book.BookRes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ListMapper {

    // 보드 전체 조회 (findAllBoard)
    @Select("SELECT * FROM book")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "price", column = "price")
    })
    public List<BookRes> findAllBook();

    // 보드 전체 조회 (findAllBoard)
    @Select("SELECT * FROM book WHERE title = #{keyword}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "price", column = "price")
    })
    public List<BookRes> findSearchedBook(@Param("keyword") final String keyword);
}