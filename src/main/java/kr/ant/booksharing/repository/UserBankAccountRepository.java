package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.UserBankAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserBankAccountRepository extends MongoRepository<UserBankAccount, String> {
    Optional<List<UserBankAccount>> findAllByUserId(final int userId);
    Optional<UserBankAccount> findBy_id(final String id);
}
