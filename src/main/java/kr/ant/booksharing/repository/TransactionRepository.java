package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
