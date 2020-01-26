package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.CustomerInquiry;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.repository.CustomerInquiryRepository;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomerInquiryService {
    private final CustomerInquiryRepository customerInquiryRepository;

    public CustomerInquiryService(final CustomerInquiryRepository customerInquiryRepository){this.customerInquiryRepository = customerInquiryRepository;}

    /**
     * 고객의 소리 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<CustomerInquiry>> findAllCustomerInquiries() {

        return DefaultRes.res(StatusCode.OK, "고객의 소리 조회 성공", customerInquiryRepository.findAll());
    }

    /**
     * 고객의 소리 저장
     *
     * @param customerInquiry 고객의 소리
     * @return DefaultRes
     */
    public DefaultRes saveCustomerInquiry(final CustomerInquiry customerInquiry) {
        try {
            customerInquiryRepository.save(customerInquiry);
            return DefaultRes.res(StatusCode.CREATED, "고객의 소리 저장 성공");
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "고객의 소리 저장 실패");
        }
    }
}
