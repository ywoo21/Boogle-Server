package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.CustomerInquiry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerInquiryRepository extends MongoRepository<CustomerInquiry, String> {
    Optional<List<CustomerInquiry>> findAllByStatusIsTrue();
    Optional<List<CustomerInquiry>> findAllByStatusIsFalse();
}
