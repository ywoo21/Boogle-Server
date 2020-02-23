package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.BoogleBox;
import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.domain.Transaction;
import kr.ant.booksharing.domain.TransactionHistory;
import kr.ant.booksharing.model.BoogleBoxInfo;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.repository.*;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final SellItemRepository sellItemRepository;
    private final MailSenderService mailSenderService;
    private final MailContentBuilderService mailContentBuilderService;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final BoogleBoxRepository boogleBoxRepository;

    public TransactionService(final TransactionRepository transactionRepository,
                              final TransactionHistoryRepository transactionHistoryRepository,
                              final SellItemRepository sellItemRepository,
                              final MailSenderService mailSenderService,
                              final MailContentBuilderService mailContentBuilderService,
                              final JavaMailSender javaMailSender,
                              final UserRepository userRepository,
                              final BoogleBoxRepository boogleBoxRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.sellItemRepository = sellItemRepository;
        this.mailSenderService = mailSenderService;
        this.mailContentBuilderService = mailContentBuilderService;
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.boogleBoxRepository = boogleBoxRepository;
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

            final String id  = transactionRepository.save(transaction).get_id();

            TransactionHistory transactionHistory =

                    TransactionHistory.builder()
                            ._id(id)
                            .boxId(transaction.getBoxId())
                            .transactionTimeList(transaction.getTransactionTimeList())
                            .transactionType(transaction.getTransactionType())
                            .boxPassword(transaction.getBoxPassword())
                            .buyerId(transaction.getBuyerId())
                            .sellerId(transaction.getSellerId())
                            .sellItemId(transaction.getSellItemId())
                            .transCreatedTime(transaction.getTransCreatedTime())
                            .step(transaction.getStep())
                            .build();

            transactionHistoryRepository.save(transactionHistory);

            String buyerName = userRepository.findById(transaction.getBuyerId()).get().getName();
            String userName = userRepository.findById(transaction.getSellerId()).get().getName();

            String content = mailContentBuilderService.buildTransRequest(sellItem, userName, buyerName);
            MimeMessagePreparator mimeMessagePreparator =
                    mailSenderService.createMimeMessage(userRepository.findById(sellItem.getSellerId()).get().getEmail(),
                    "구매 요청", content);
            javaMailSender.send(mimeMessagePreparator);

            return DefaultRes.res(StatusCode.CREATED, "거래 정보 저장 성공");

        }
        catch(Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "거래 정보 저장 실패");

        }
    }

    /**
     * 전체 거래 정보 목록 열람
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<Transaction>> findAllTransaction(){
        try{
            return DefaultRes.res(StatusCode.OK, "거래 정보 목록 열람 성공", transactionRepository.findAll());
        } catch (Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.NOT_FOUND, "거래 정보 목록 열람 실패");
        }
    }

    /**
     * 거래 STEP 변경
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<Transaction> changeTransactionStep(final String sellItemId) {
        try{
            Transaction transaction = transactionRepository.findBySellItemId(sellItemId).get();
            transaction.setStep(transaction.getStep() + 1);

            List<Date> currentTransactionTimeList = transaction.getTransactionTimeList();
            currentTransactionTimeList.add(new Date());

            transaction.setTransactionTimeList(currentTransactionTimeList);
            transactionRepository.save(transaction);
            return DefaultRes.res(StatusCode.CREATED, "거래 STEP 변경 성공");
        }
        catch(Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "거래 STEP 변경 실패");
        }
    }

    /**
     * 북을 박스 정보 저장 및 STEP 변경
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<Transaction> saveBoogleBoxIdAndPasswordAndChangeStep(final BoogleBoxInfo boogleBoxInfo) {
        try{
            Transaction transaction = transactionRepository.findBySellItemId(boogleBoxInfo.getSellItemId()).get();
            transaction.setBoxId(boogleBoxInfo.getId());
            transaction.setBoxPassword(boogleBoxInfo.getPassword());
            transactionRepository.save(transaction);

            if(transaction.isPaymentDone()){
                changeTransactionStep(boogleBoxInfo.getSellItemId());
            }

            //boogleBox db에 저장
            BoogleBox boogleBox = new BoogleBox();
            boogleBox.set_id(boogleBoxInfo.getId());
            boogleBox.setBoxPassword(boogleBoxInfo.getPassword());
            boogleBox.setSellItemId(boogleBoxInfo.getSellItemId());
            boogleBox.setSellerId(transaction.getSellerId());
            boogleBox.setBuyerId(transaction.getBuyerId());
            boogleBox.setRegisterTime(new Date());
            boogleBox.setEmpty(false);

            boogleBoxRepository.save(boogleBox);

            return DefaultRes.res(StatusCode.CREATED, "북을 박스 정보 저장");
        }
        catch(Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "북을 박스 정보 실패");
        }
    }

    /**
     * 송금 완료 상태 저장 및 STEP 변경
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<Transaction> savePaymentDoneAndChangeStep(final String sellItemId) {
        try{

            Transaction transaction = transactionRepository.findBySellItemId(sellItemId).get();
            transaction.setPaymentDone(true);
            transactionRepository.save(transaction);

            if(!transaction.getBoxId().equals("") && !transaction.getBoxPassword().equals("")){
                changeTransactionStep(sellItemId);
            }

            return DefaultRes.res(StatusCode.CREATED, "송금 완료 상태 저장 성공");
        }
        catch(Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "송금 완료 상태 저장 실패");
        }
    }

    /**
     * 거래 삭제
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<Transaction> deleteTransaction(final String sellItemId) {
        try{
            transactionRepository.deleteBySellItemId(sellItemId);
            transactionHistoryRepository.deleteBySellItemId(sellItemId);
            SellItem sellItem =
                    sellItemRepository.findById(sellItemId).get();
            sellItem.setTraded(false);
            sellItemRepository.save(sellItem);
            return DefaultRes.res(StatusCode.OK, "거래 취소 성공");
        }
        catch(Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "거래 취소 실패");
        }
    }

    /**
     * step2 거래 정보 목록 열람
     *
     *
     */
    public DefaultRes<List<Transaction>> findAllStepTwoTransaction(){
        try{
            return DefaultRes.res(StatusCode.OK, "step2 거래 정보 목록 열람 성공", transactionRepository.findAllByStepEquals(2).get());
        } catch(Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.NOT_FOUND, "step2 거래 정보 목록 열람 실패");
        }
    }

    /**
     * step4 거래 정보 목록 열람
     *
     *
     */
    public DefaultRes<List<Transaction>> findAllStepFiveTransaction(){
        try{
            return DefaultRes.res(StatusCode.OK, "step4 거래 정보 목록 열람 성공", transactionRepository.findAllByStepEquals(4).get());
        } catch(Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.NOT_FOUND, "step4 거래 정보 목록 열람 실패");
        }
    }
}
