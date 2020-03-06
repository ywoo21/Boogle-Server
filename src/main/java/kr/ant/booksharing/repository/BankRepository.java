package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BankRepository extends MongoRepository<Bank, String> {
    Optional<Bank> findBy_id(String id);
}
