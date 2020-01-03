package kr.ant.booksharing.service;

import kr.ant.booksharing.dao.ListMapper;
import kr.ant.booksharing.dao.UserMapper;
import kr.ant.booksharing.model.Book.BookRes;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.HomeRes;
import kr.ant.booksharing.utils.ResponseMessage;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HomeService {
    private final ListMapper listMapper;
    private final UserMapper userMapper;

    public HomeService(final ListMapper listMapper, final UserMapper userMapper) {
        this.listMapper = listMapper;
        this.userMapper =  userMapper;
    }

    /**
     * 전체 책 목록 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<HomeRes> findRecentBook() {
        final List<BookRes> recentBooks = listMapper.findRecentBook();
        final List<BookRes> leastPriceBooks = listMapper.findLeastPriceBook();
        final List<BookRes> mostRegisteredBook = listMapper.findMostRegisteredBook();
        if (recentBooks.isEmpty() || leastPriceBooks.isEmpty()
        || mostRegisteredBook.isEmpty()) {
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_SELL_ITEM);
        }
        final HomeRes homeRes = new HomeRes();
        homeRes.setBookResList1(recentBooks);
        homeRes.setBookResList2(leastPriceBooks);
        homeRes.setBookResList3(mostRegisteredBook);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_SELL_ITEMS, homeRes);
    }
}
