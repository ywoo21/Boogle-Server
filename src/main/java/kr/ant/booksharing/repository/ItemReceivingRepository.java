package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.ItemReceiving;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ItemReceivingRepository extends MongoRepository<ItemReceiving, String> {
    Optional<List<ItemReceiving>> findByUserId(int userId);
    Optional<ItemReceiving> findByItemId(String itemId);
    void deleteByUserIdAndItemId(int userId, String itemId);
}
