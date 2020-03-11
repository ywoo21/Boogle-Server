package kr.ant.booksharing.repository;


import kr.ant.booksharing.domain.SellItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface SellItemRepository extends MongoRepository<SellItem, String> {
    Optional<SellItem> findBy_id(String id);
    Optional<List<SellItem>> findAllByItemId(String itemId);
    Optional<List<SellItem>> findAllByTitleContainingAndIsTraded(String keyword, boolean isTraded);

    @Query("{ 'title' : ?0 }")
    List<SellItem> findAllGroupByItemIdContaining(String keyword);

    Optional<List<SellItem>> findTop4ByIsTradedOrderByRegiTimeDesc(boolean isTraded);
    Optional<List<SellItem>> findTop4ByIsTradedOrderByRegiPriceAsc(boolean isTraded);
    Optional<List<SellItem>> findAllByItemIdAndIsTraded(String itemId, boolean isTraded);

    Optional<List<SellItem>> findAllBySellerId(int sellerId);

}



