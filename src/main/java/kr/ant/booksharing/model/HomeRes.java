package kr.ant.booksharing.model;

import kr.ant.booksharing.model.Book.BookRes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class HomeRes {
    private List<BookRes> bookResList1;
    private List<BookRes> bookResList2;
    private List<BookRes> bookResList3;
}
