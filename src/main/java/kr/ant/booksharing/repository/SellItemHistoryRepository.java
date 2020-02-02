package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.domain.SellItemHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SellItemHistoryRepository extends MongoRepository<SellItemHistory, String> {
}
