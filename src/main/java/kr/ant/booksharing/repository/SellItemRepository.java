package kr.ant.booksharing.repository;


import kr.ant.booksharing.domain.SellItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellItemRepository extends MongoRepository<SellItem, String> {
    Optional<SellItem> findBy_id(String id);
    Optional<List<SellItem>> findAllByItemId(String itemId);
    Optional<List<SellItem>> findAllByTitleContaining(String keyword);
}



