package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.Major;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MajorRepository extends MongoRepository<Major, String> {
    Optional<Major> findByCampus(String campus);
}
