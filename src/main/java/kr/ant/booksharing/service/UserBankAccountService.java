package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.Bank;
import kr.ant.booksharing.domain.Transaction;
import kr.ant.booksharing.domain.UserBankAccount;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.UserBankAccountRes;
import kr.ant.booksharing.repository.BankRepository;
import kr.ant.booksharing.repository.UserBankAccountRepository;
import kr.ant.booksharing.repository.UserBookmarkRepository;
import kr.ant.booksharing.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserBankAccountService {
    private final UserBankAccountRepository userBankAccountRepository;
    private final BankRepository bankRepository;

    public UserBankAccountService(final UserBankAccountRepository userBankAccountRepository,
                                  final BankRepository bankRepository){
        this.userBankAccountRepository = userBankAccountRepository;
        this.bankRepository = bankRepository;
    }

    /**
     * 계좌 정보 저장
     *
     * @param userBankAccount 계좌 정보
     * @return DefaultRes
     */
    public DefaultRes saveUserBankAccount (final UserBankAccount userBankAccount) {
        try {
            userBankAccountRepository.save(userBankAccount);
            return DefaultRes.res(StatusCode.CREATED, "계좌 정보 저장 성공");
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "계좌 정보 저장 성공");
        }
    }

    /**
     * 계좌 정보 조회
     *
     * @param userId 회원 고유 번호
     * @return DefaultRes
     */
    public DefaultRes findAllUserBankAccountByUserId (final int userId) {

        try {
            List<UserBankAccount> userBankAccountList = new ArrayList<>();

            if(userBankAccountRepository.findAllByUserId(userId).isPresent()){
                userBankAccountList.addAll(userBankAccountRepository.findAllByUserId(userId).get());
            }

            return DefaultRes.res(StatusCode.OK, "전체 은행 정보 조회 성공", userBankAccountList);

        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "전체 은행 정보 조회 실패");
        }
    }

    /**
     * 계좌 정보 삭제
     *
     * @param userBankAccountId 계좌 정보 고유 번호
     * @return DefaultRes
     */
    public DefaultRes deleteUserBankAccount(final String userBankAccountId) {
        try {
            userBankAccountRepository.deleteById(userBankAccountId);
            return DefaultRes.res(StatusCode.OK, "계좌 정보 삭제 성공");
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "계좌 정보 삭제 ");
        }
    }

    /**
     * 계좌 정보 조회
     *
     * @param userId 회원 고유 번호
     * @return DefaultRes
     */
    public DefaultRes findAllUserBankAccountIncludingBankNameByUserId (final int userId) {

        try {
            List<UserBankAccount> userBankAccountList = new ArrayList<>();

            if(userBankAccountRepository.findAllByUserId(userId).isPresent()){
                userBankAccountList.addAll(userBankAccountRepository.findAllByUserId(userId).get());
            }

            List<UserBankAccountRes> userBankAccountResList = new ArrayList<>();

            if(userBankAccountList.size() > 0){
                userBankAccountList.stream().forEach(ub -> {
                    Bank bank = bankRepository.findBy_id(ub.getBankId()).get();
                    UserBankAccountRes userBankAccountRes = new UserBankAccountRes();
                    userBankAccountRes.setUserBankAccount(ub);
                    userBankAccountRes.setBankName(bank.getName());
                    userBankAccountResList.add(userBankAccountRes);
                });
            }
            return DefaultRes.res(StatusCode.OK, "전체 은행 정보 조회 성공", userBankAccountResList);

        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "전체 은행 정보 조회 실패");
        }
    }
}

