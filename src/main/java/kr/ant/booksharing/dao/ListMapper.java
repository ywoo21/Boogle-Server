package kr.ant.booksharing.dao;

import kr.ant.booksharing.model.Book;
import kr.ant.booksharing.model.Book.BookReq;
import kr.ant.booksharing.model.Book.BookRes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ListMapper {

    // 책 목록 전체 조회
    @Select("SELECT * FROM book")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "price", column = "price")
    })
    public List<BookRes> findAllBook();

    // 키워드에 해당하는 책 목록 조회
    @Select("SELECT * FROM book WHERE title = #{keyword}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "price", column = "price")
    })
    public List<BookRes> findSearchedBook(@Param("keyword") final String keyword);

    // 책 정보 저장
    @Insert("INSERT INTO book(id, title, price) VALUES(#{bookReq.id}, " +
            "#{bookReq.title}, #{bookReq.price})")
    @Options(useGeneratedKeys = true, keyProperty = "bookReq.id", keyColumn="id")
    void saveBook(@Param("bookReq") final BookReq bookReq);
}