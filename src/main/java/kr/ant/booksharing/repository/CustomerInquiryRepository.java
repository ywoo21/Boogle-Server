package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.CustomerInquiry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerInquiryRepository extends MongoRepository<CustomerInquiry, Integer> {
}
