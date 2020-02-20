package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.TransactionHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionHistoryRepository extends MongoRepository<TransactionHistory, String> {
}
