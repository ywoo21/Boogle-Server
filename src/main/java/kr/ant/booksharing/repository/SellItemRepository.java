package kr.ant.booksharing.repository;


import kr.ant.booksharing.domain.SellItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SellItemRepository extends JpaRepository<SellItem, Integer> {
    Optional<List<SellItem>> findAllByItemId(int ItemId);
}



