package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.Bank;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.repository.BankRepository;
import kr.ant.booksharing.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {
    private final BankRepository bankRepository;

    public BankService(final BankRepository bankRepository){
        this.bankRepository = bankRepository;
    }

    /**
     * 전체 은행 정보 저장
     *
     * @param bankList 은행 정보 리스트
     * @return DefaultRes
     */
    public DefaultRes saveAllBankList (final List<Bank> bankList) {
        try {
            bankRepository.saveAll(bankList);
            return DefaultRes.res(StatusCode.CREATED, "전체 은행 정보 저장 성공");
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "전체 은행 정보 저장 실패");
        }
    }

    /**
     * 전체 은행 정보 조회
     *
     * @return DefaultRes
     */
    public DefaultRes findAllBankList () {
        try {
            return DefaultRes.res(StatusCode.OK, "전체 은행 정보 조회 성공", bankRepository.findAll());
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "전체 은행 정보 조회 실패");
        }
    }
}
