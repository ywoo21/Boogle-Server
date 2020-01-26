package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.Transaction;
import kr.ant.booksharing.domain.UserBookmark;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserBookmarkRepository extends MongoRepository<UserBookmark, String> {
    Optional<UserBookmark> findByUserIdAndSellItemId(final int userId, final String sellItemId);
    void deleteByUserIdAndSellItemId(final int userId, final String user);
}
