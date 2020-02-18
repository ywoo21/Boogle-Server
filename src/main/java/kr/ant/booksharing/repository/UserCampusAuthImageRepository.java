package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.UserBankAccount;
import kr.ant.booksharing.domain.UserCampusAuthImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserCampusAuthImageRepository extends MongoRepository<UserCampusAuthImage, String> {
}
