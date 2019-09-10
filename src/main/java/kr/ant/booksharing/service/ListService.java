package kr.ant.booksharing.service;

import kr.ant.booksharing.dao.ListMapper;
import kr.ant.booksharing.dao.UserMapper;

import kr.ant.booksharing.model.Book.BookReq;
import kr.ant.booksharing.model.Book.BookRes;
import kr.ant.booksharing.model.DefaultRes;

import kr.ant.booksharing.model.Transaction.TransactionReq;
import kr.ant.booksharing.model.Transaction.TransactionRes;

import kr.ant.booksharing.utils.ResponseMessage;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ListService {

    private final ListMapper listMapper;
    private final UserMapper userMapper;

    public ListService(final ListMapper listMapper, final UserMapper userMapper) {
        this.listMapper = listMapper;
        this.userMapper =  userMapper;
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
     * 검색된 책 목록 조회
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

    /**
     * 책 상세 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<BookRes> findAdvancedBook(final int bookId) {
        final BookRes book;

        book = listMapper.findBookByBookId(bookId);

        if (book == null) {
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BOARD);
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_BOARDS, book);
    }

    /**
     * 책 정보 저장
     *
     * @param bookReq 책 데이터
     * @return DefaultRes
     */
    public DefaultRes saveBook(final BookReq bookReq) {
        try {
            List<String> place = bookReq.getPlace();
            String placeToString = String.join("/", place);
            bookReq.setPlaceToString(placeToString);

            final String sellerEmail = bookReq.getSellerEmail();
            final int sellerId = userMapper.findUserIdByUserEmail(sellerEmail);

            bookReq.setSellerId(sellerId);

            String doodle = bookReq.getDoodle();
            String damage = bookReq.getDamage();
            String colored = bookReq.getColored();
            double totalScore = 5.0;

            if(doodle.contains(Character.toString('B'))){totalScore -= 0.5;}
            else if(doodle.contains(Character.toString('C'))){totalScore -= 1.0;}

            if(damage.contains(Character.toString('B'))){totalScore -= 0.5;}
            else if(damage.contains(Character.toString('C'))){totalScore -= 1.0;}

            if(colored.contains(Character.toString('B'))){totalScore -= 0.5;}
            else if(colored.contains(Character.toString('C'))){totalScore -= 1.0;}

            bookReq.setQuality(String.valueOf(totalScore));

            listMapper.saveBook(bookReq);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_BOARD);
        } catch (Exception e) {
            log.error("{}",e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
    /**
     * 거래 정보 저장
     *
     * @param transactionReq 거래 데이터
     * @return DefaultRes
     */
    public DefaultRes saveTransaction(final TransactionReq transactionReq) {
        try {
            final String buyerEmail = transactionReq.getBuyerEmail();
            final int buyerId = userMapper.findUserIdByUserEmail(buyerEmail);

            transactionReq.setBuyerId(buyerId);
            listMapper.saveTransaction(transactionReq);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_BOARD);
        } catch (Exception e) {
            log.error("{}",e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 구매 목록 조회
     *
     * @param email 유저 email
     * @return DefaultRes
     */
    public DefaultRes findBuyList(final String email) {
        try {
            int userId  = userMapper.findUserIdByUserEmail(email);
            List <Integer> bookId = listMapper.findBookIdByBuyerId(userId);
            List <Timestamp> date = listMapper.findTransDateByBuyerId(userId);
            List <Integer> state = listMapper.findTransStateByBuyerId(userId);

            if(bookId.isEmpty() || date == null){ return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BOARD); }
            else{
                List <BookRes> buyList = new ArrayList<>();
                for(Integer id : bookId){
                    buyList.add(listMapper.findBookByBookId(id));
                }
                for(int i = 0; i<buyList.size(); i++){
                    buyList.get(i).setTransDate(date.get(i));
                    buyList.get(i).setTransState(state.get(i));

                    String sellerPhoneNumber = userMapper.findUserPhoneNumberByUserId(buyList.get(i).getSellerId());
                    buyList.get(i).setSellerPhoneNumber(sellerPhoneNumber);
                }
                if(buyList.isEmpty()){ return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BOARD); }
                else{
                    return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_BOARDS, buyList);
                }
            }
        } catch (Exception e) {
            log.error("{}",e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 판매 목록 조회
     *
     * @param email 유저 email
     * @return DefaultRes
     */
    public DefaultRes findSellList(final String email) {
        try {
            int userId  = userMapper.findUserIdByUserEmail(email);
            List<BookRes> buyList = listMapper.findBookIdBySellerId(userId);
            if(buyList.isEmpty()){ return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BOARD); }
            else{
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_ALL_BOARDS, buyList);
            }
        } catch (Exception e) {
            log.error("{}",e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 거래 상태 수정(구매)
     *
     * @param email 회원 이메일
     * @return DefaultRes
     */
    public DefaultRes updateBuyTransaction(final String email, int bookId) {
        try {
            final int state = listMapper.findTransStateByBookId(bookId);
            if(state == 1) listMapper.updateBuyState(bookId); // 상태가 1일 때만 증가시킨다.
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_BOARD);
        } catch (Exception e) {
            log.error("{}",e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 거래 상태 수정(판매)
     *
     * @param bookId 책 id
     * @return DefaultRes
     */
    public DefaultRes updateSellTransaction(int bookId) {
        try {
            final int state = listMapper.findBookStateByBookId(bookId);
            if(state == 1) listMapper.updateBuyState(bookId);
            listMapper.updateSellState(bookId);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_BOARD);
        } catch (Exception e) {
            log.error("{}",e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 판매 책 삭제
     *
     * @param bookId 책 id
     * @return DefaultRes
     */
    public DefaultRes deleteBook(int bookId) {
        try {
            listMapper.deleteBook(bookId);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_BOARD);
        } catch (Exception e) {
            log.error("{}",e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}