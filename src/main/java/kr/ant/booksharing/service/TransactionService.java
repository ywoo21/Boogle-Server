package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.Transaction;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.repository.TransactionRepository;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(final TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * 거래 정보 저장
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<Transaction> saveTransaction(final Transaction transaction) {
        try{

            transactionRepository.save(transaction);

            return DefaultRes.res(StatusCode.CREATED, "거래 정보 저장 성공");

        }
        catch(Exception e){

            return DefaultRes.res(StatusCode.DB_ERROR, "거래 정보 저장 실패");

        }
    }
}
