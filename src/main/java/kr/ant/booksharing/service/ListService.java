package kr.ant.booksharing.service;

import kr.ant.booksharing.dao.ListMapper;
import kr.ant.booksharing.model.Book.BookRes;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.utils.ResponseMessage;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ListService {

    private final ListMapper listMapper;

    public ListService(final ListMapper listMapper) {
        this.listMapper = listMapper;
    }


    /**
     * 전체 책 목록 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<BookRes>> findAllBook() {
        final List<BookRes> books = listMapper.findAllBook();
        if (books.isEmpty()) {
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BOARD);
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_BOARDS, books);
    }

    /**
     * 전체 책 목록 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<BookRes>> findSearchedBook(final String keyword) {
        final List<BookRes> books;

        if(keyword.length() == 0){
            books = listMapper.findAllBook();
        }
        else{
            books = listMapper.findSearchedBook(keyword);
        }

        if (books.isEmpty()) {
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BOARD);
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_BOARDS, books);
    }
}