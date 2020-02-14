package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.Bank;
import kr.ant.booksharing.domain.Transaction;
import kr.ant.booksharing.domain.UserBankAccount;
import kr.ant.booksharing.model.DefaultRes;
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

    public UserBankAccountService(final UserBankAccountRepository userBankAccountRepository){
        this.userBankAccountRepository = userBankAccountRepository;
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
}

