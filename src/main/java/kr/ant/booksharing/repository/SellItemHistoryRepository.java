package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.SellItemHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SellItemHistoryRepository extends MongoRepository<SellItemHistory, String> {
    Long countByItemId(String itemId);
}
