package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.BoogleBox;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoogleBoxRepository extends MongoRepository<BoogleBox, String> {
}
