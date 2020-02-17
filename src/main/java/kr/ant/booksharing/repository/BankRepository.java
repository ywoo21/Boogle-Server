package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BankRepository extends MongoRepository<Bank, String> {
}
