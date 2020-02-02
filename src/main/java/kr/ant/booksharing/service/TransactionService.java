package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.domain.Transaction;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.SellItemRes;
import kr.ant.booksharing.repository.SellItemRepository;
import kr.ant.booksharing.repository.TransactionRepository;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellItemRepository sellItemRepository;

    public TransactionService(final TransactionRepository transactionRepository,
                              final SellItemRepository sellItemRepository) {
        this.transactionRepository = transactionRepository;
        this.sellItemRepository = sellItemRepository;
    }

    /**
     * 거래 정보 저장
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<Transaction> saveTransaction(Transaction transaction) {
        try{

            SellItem sellItem = sellItemRepository.findBy_id(transaction.getSellItemId()).get();

            sellItem.setTraded(true);
            sellItemRepository.save(sellItem);

            if(!transactionRepository.findBySellItemId(transaction.getSellItemId()).isPresent()){

                transaction.setTransCreatedTime(new Date());

                Date date = new Date();

                List transactionTimeList = new ArrayList<>();

                transactionTimeList.add(date);

                transaction.setTransactionTimeList(transactionTimeList);

            }
            else{

                Transaction curTrans =
                        transactionRepository.findBySellItemId(transaction.getSellItemId()).get();

                List<Date> curTransactionTimeList = curTrans.getTransactionTimeList();

                Date date = new Date();

                curTransactionTimeList.add(date);

                curTrans.setTransactionTimeList(curTransactionTimeList);

                transaction = curTrans;

            }

            transactionRepository.save(transaction);

            return DefaultRes.res(StatusCode.CREATED, "거래 정보 저장 성공");

        }
        catch(Exception e){

            return DefaultRes.res(StatusCode.DB_ERROR, "거래 정보 저장 실패");

        }
    }
}
