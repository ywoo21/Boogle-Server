package kr.ant.booksharing.dao;

import kr.ant.booksharing.model.Book.BookReq;
import kr.ant.booksharing.model.Book.BookRes;
import kr.ant.booksharing.model.Transaction.TransactionReq;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ListMapper {

    // 책 목록 전체 조회
    @Select("SELECT * FROM sell_item")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "price", column = "price"),
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "author", column = "author"),
            @Result(property = "publisher", column = "publisher"),
            @Result(property = "regiPrice", column = "regi_price"),
            @Result(property = "quality", column = "quality"),
            @Result(property = "deal", column = "deal"),
            @Result(property = "place", column = "place"),
            @Result(property = "sellerId", column = "seller_id"),
            @Result(property = "boxNumber", column = "box_number"),
            @Result(property = "boxPassword", column = "box_password"),
            @Result(property = "date", column = "date"),
            @Result(property = "state", column = "state"),
            @Result(property = "doodle", column = "doodle"),
            @Result(property = "damage", column = "damage"),
            @Result(property = "colored", column = "colored"),

    })
    public List<BookRes> findAllBook();

    // 키워드에 해당하는 책 목록 조회
    @Select("SELECT * FROM sell_item WHERE title LIKE CONCAT('%',#{keyword},'%')")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "price", column = "price"),
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "author", column = "author"),
            @Result(property = "publisher", column = "publisher"),
            @Result(property = "regiPrice", column = "regi_price"),
            @Result(property = "quality", column = "quality"),
            @Result(property = "deal", column = "deal"),
            @Result(property = "place", column = "place"),
            @Result(property = "sellerId", column = "seller_id"),
            @Result(property = "boxNumber", column = "box_number"),
            @Result(property = "boxPassword", column = "box_password"),
            @Result(property = "date", column = "date"),
            @Result(property = "state", column = "state"),
            @Result(property = "doodle", column = "doodle"),
            @Result(property = "damage", column = "damage"),
            @Result(property = "colored", column = "colored"),
    })
    public List<BookRes> findSearchedBook(@Param("keyword") final String keyword);

    // 책 id에 해당하는 책 목록 조회
    @Select("SELECT * FROM sell_item WHERE id = #{bookId}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "price", column = "price"),
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "author", column = "author"),
            @Result(property = "publisher", column = "publisher"),
            @Result(property = "regiPrice", column = "regi_price"),
            @Result(property = "quality", column = "quality"),
            @Result(property = "deal", column = "deal"),
            @Result(property = "place", column = "place"),
            @Result(property = "sellerId", column = "seller_id"),
            @Result(property = "boxNumber", column = "box_number"),
            @Result(property = "boxPassword", column = "box_password"),
            @Result(property = "date", column = "date"),
            @Result(property = "state", column = "state"),
            @Result(property = "doodle", column = "doodle"),
            @Result(property = "damage", column = "damage"),
            @Result(property = "colored", column = "colored"),
    })
    public BookRes findBookByBookId(@Param("bookId") final int bookId);

    // 책 정보 저장
    @Insert("INSERT INTO sell_item(id, title, price, author, publisher, image_url, regi_price, quality, deal, place, seller_id, box_number, box_password, doodle, damage, colored) VALUES(#{bookReq.id}, " +
            "#{bookReq.title}, #{bookReq.price}, #{bookReq.author}, #{bookReq.publisher}, #{bookReq.imageUrl}, " +
            "#{bookReq.regiPrice}, #{bookReq.quality}, #{bookReq.deal}, #{bookReq.placeToString}, #{bookReq.sellerId}," +
            " #{bookReq.boxNumber}, #{bookReq.boxPassword}, #{bookReq.doodle}, #{bookReq.damage},#{bookReq.colored})")
    @Options(useGeneratedKeys = true, keyProperty = "bookReq.id", keyColumn="id")
    void saveBook(@Param("bookReq") final BookReq bookReq);

    // 거래 정보 저장
    @Insert("INSERT INTO transaction(id, seller_id, buyer_id, book_id) VALUES(#{transactionReq.id}, " +
            "#{transactionReq.sellerId}, #{transactionReq.buyerId}, #{transactionReq.bookId})")
    @Options(useGeneratedKeys = true, keyProperty = "transaction.id", keyColumn="id")
    void saveTransaction(@Param("transactionReq") final TransactionReq transactionReq);

    // 구매자 id로 거래 일자 조회
    @Select("SELECT * FROM transaction WHERE buyer_id = #{userId}")
    @Results(value = {
            @Result(property = "date", column = "date")
    })
    public List<Timestamp> findTransDateByBuyerId(@Param("userId") final int userId);

    // 구매자 id로 거래 상태 조회
    @Select("SELECT * FROM transaction WHERE buyer_id = #{userId}")
    @Results(value = {
            @Result(property = "state", column = "state")
    })
    public List<Integer> findTransStateByBuyerId(@Param("userId") final int userId);

    // 구매자 id로 거래 책 id 조회
    @Select("SELECT * FROM transaction WHERE buyer_id = #{userId}")
    @Results(value = {
            @Result(property = "bookId", column = "book_id")
    })
    public List<Integer> findBookIdByBuyerId(@Param("userId") final int userId);

    // 판매자 id로 책 id 조회
    @Select("SELECT * FROM sell_item WHERE seller_id = #{userId}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "price", column = "price"),
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "author", column = "author"),
            @Result(property = "publisher", column = "publisher"),
            @Result(property = "regiPrice", column = "regi_price"),
            @Result(property = "quality", column = "quality"),
            @Result(property = "deal", column = "deal"),
            @Result(property = "place", column = "place"),
            @Result(property = "sellerId", column = "seller_id"),
            @Result(property = "boxNumber", column = "box_number"),
            @Result(property = "boxPassword", column = "box_password"),
            @Result(property = "date", column = "date"),
            @Result(property = "state", column = "state"),
            @Result(property = "doodle", column = "doodle"),
            @Result(property = "damage", column = "damage"),
            @Result(property = "colored", column = "colored"),
    })
    public List<BookRes> findBookIdBySellerId(@Param("userId") final int userId);

    // 구매 상태 증가
    @Update("UPDATE transaction SET state = state + 1 WHERE book_id = #{bookId}")
    void updateBuyState(@Param("bookId") final int bookId);

    // 책 id로 구매 상태 조회
    @Select("SELECT * FROM transaction WHERE book_id = #{bookId}")
    @Results(value = {
            @Result(property = "state", column = "state")
    })
    public int findTransStateByBookId(@Param("bookId") final int bookId);

    // 책 id로 판매 상태 조회
    @Select("SELECT * FROM sell_item WHERE id = #{bookId}")
    @Results(value = {
            @Result(property = "state", column = "state")
    })
    public int findBookStateByBookId(@Param("bookId") final int bookId);

    // 판매 상태 증가
    @Update("UPDATE sell_item SET state = state + 1 WHERE id = #{bookId}")
    void updateSellState(@Param("bookId") final int bookId);

    // 판매 책 삭제
    @Update("DELETE FROM sell_item WHERE id = #{bookId}")
    void deleteBook(@Param("bookId") final int bookId);
}