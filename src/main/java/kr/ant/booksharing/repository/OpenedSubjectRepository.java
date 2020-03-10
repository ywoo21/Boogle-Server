package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.Major;
import kr.ant.booksharing.domain.OpenedSubject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OpenedSubjectRepository extends MongoRepository<OpenedSubject, String> {
    Optional<OpenedSubject> findByCampus(String campus);
}
