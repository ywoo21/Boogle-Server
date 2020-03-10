package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.Item;
import kr.ant.booksharing.domain.SellItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends MongoRepository<Item, String> {
    Optional<Item> findByItemId(String itemId);
    Optional<List<Item>> findAllByTitleContaining(String keyword);
    Optional<List<Item>> findAllBySubjectListContaining(String keyword);
    Optional<List<Item>> findAllByTitleOrSubjectListOrProfessorListContaining(String keyword);
    Optional<List<Item>> findByOrderByRegiCountDesc();
}
