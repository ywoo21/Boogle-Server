package kr.ant.booksharing.controller;

import kr.ant.booksharing.domain.Bank;
import kr.ant.booksharing.domain.Major;
import kr.ant.booksharing.repository.BankRepository;
import kr.ant.booksharing.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("bank")
public class BankController {

    private final BankService bankService;

    public BankController(final BankService bankService){
        this.bankService = bankService;
    }

    /**
     * 전체 은행 정보 저장
     *
     * @param bankList 은행 정보 리스트
     * @return ResponseEntity
     */
    @PostMapping("")
    public ResponseEntity saveAllBankInfo(@RequestBody final List<Bank> bankList) {
        try {
            return new ResponseEntity<>(bankService.saveAllBankList(bankList), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 전체 은행 정보 조회
     *
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity getAllBankInfo() {
        try {
            return new ResponseEntity<>(bankService.findAllBankList(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
