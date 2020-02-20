package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.UserBankAccount;
import kr.ant.booksharing.domain.UserCampusAuthImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserCampusAuthImageRepository extends MongoRepository<UserCampusAuthImage, String> {
    Optional<UserCampusAuthImage> findByUserId(int userId);
}
