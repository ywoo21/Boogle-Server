package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.CustomerInquiry;
import kr.ant.booksharing.domain.User;
import kr.ant.booksharing.model.CustomerInquiryRes;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.repository.CustomerInquiryRepository;
import kr.ant.booksharing.repository.UserRepository;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
public class CustomerInquiryService {
    private final CustomerInquiryRepository customerInquiryRepository;
    private final UserRepository userRepository;
    private final S3FileUploadService s3FileUploadService;

    public CustomerInquiryService(final CustomerInquiryRepository customerInquiryRepository,
                                  final UserRepository userRepository,
                                  final S3FileUploadService s3FileUploadService){
        this.customerInquiryRepository = customerInquiryRepository;
        this.userRepository = userRepository;
        this.s3FileUploadService = s3FileUploadService;
    }

    /**
     * 고객의 소리 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<CustomerInquiryRes>> findAllCustomerInquiries() {
        List<CustomerInquiry> customerInquiryList = customerInquiryRepository.findAll();
        Iterator<CustomerInquiry> iter = customerInquiryList.iterator();
        List<CustomerInquiryRes> customerInquiryResList = new ArrayList<>();
        try {
            while (iter.hasNext()) {
                CustomerInquiry temp = iter.next();
                if (temp.getIsMember() != null && temp.getIsMember()) {
                    User user = userRepository.findByEmail(temp.getEmail()).get();
                    user.setPassword(null);
                    customerInquiryResList.add(new CustomerInquiryRes(temp, user));
                } else customerInquiryResList.add(new CustomerInquiryRes(temp, null));
            }
        } catch(Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.NOT_FOUND, "고객의 소리 조회 실패");
        }

        return DefaultRes.res(StatusCode.OK, "고객의 소리 조회 성공", customerInquiryResList);
    }

    /**
     * 처리된 고객의 소리 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<CustomerInquiryRes>> findAllDoneCustomerInquiries() {
        List<CustomerInquiry> customerInquiryList = customerInquiryRepository.findAllByStatusIsTrue().get();
        Iterator<CustomerInquiry> iter = customerInquiryList.iterator();
        List<CustomerInquiryRes> customerInquiryResList = new ArrayList<>();
        try {
            while (iter.hasNext()) {
                CustomerInquiry temp = iter.next();
                if (temp.getIsMember() != null && temp.getIsMember()) {
                    User user = userRepository.findByEmail(temp.getEmail()).get();
                    user.setPassword(null);
                    customerInquiryResList.add(new CustomerInquiryRes(temp, user));
                } else customerInquiryResList.add(new CustomerInquiryRes(temp, null));
            }
        } catch(Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.NOT_FOUND, "처리된 고객의 소리 조회 실패");
        }

        return DefaultRes.res(StatusCode.OK, "처리된 고객의 소리 조회 성공", customerInquiryResList);
    }

    /**
     * 처리 안 된 고객의 소리 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<CustomerInquiryRes>> findAllUnDoneCustomerInquiries() {
        List<CustomerInquiry> customerInquiryList = customerInquiryRepository.findAllByStatusIsFalse().get();
        Iterator<CustomerInquiry> iter = customerInquiryList.iterator();
        List<CustomerInquiryRes> customerInquiryResList = new ArrayList<>();
        try {
            while (iter.hasNext()) {
                CustomerInquiry temp = iter.next();
                if (temp.getIsMember() != null && temp.getIsMember()) {
                    User user = userRepository.findByEmail(temp.getEmail()).get();
                    user.setPassword(null);
                    customerInquiryResList.add(new CustomerInquiryRes(temp, user));
                } else customerInquiryResList.add(new CustomerInquiryRes(temp, null));
            }
        } catch(Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.NOT_FOUND, "처리 안 된 고객의 소리 조회 실패");
        }

        return DefaultRes.res(StatusCode.OK, "처리 안 된 고객의 소리 조회 성공", customerInquiryResList);
    }

    /**
     * 고객의 소리 저장
     *
     * @param customerInquiry 고객의 소리
     * @return DefaultRes
     */
    public DefaultRes saveCustomerInquiry(final int userId, final CustomerInquiry customerInquiry, final List<MultipartFile> imageFileList) {
        try {
            // 비회원일 떄
            if(userId == -1) {
                customerInquiry.setIsMember(false);
            }
            // 회원일 때
            else {
                customerInquiry.setIsMember(true);
                customerInquiry.setEmail(userRepository.findById(userId).get().getEmail());
            }
            //이미지 파일 처리
            List<String> imageUrlList = new ArrayList<>();
            for(MultipartFile m : imageFileList){
                imageUrlList.add(s3FileUploadService.upload(m));
            }
            customerInquiry.setImageUrlList(imageUrlList);

            customerInquiry.setStatus(false); // 운영진이 해당 문의를 처리한지 여부 초기화
            customerInquiry.setDate(new Date()); // 생성 시점 저장
            customerInquiryRepository.save(customerInquiry);
            return DefaultRes.res(StatusCode.CREATED, "고객의 소리 저장 성공");
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "고객의 소리 저장 실패");
        }
    }

    /**
     * 고객의 소리 처리 여부 변경
     *
     * @param customerInquiryId 고객의 소리
     * @return DefaultRes
     */
    public DefaultRes changeStatus(String customerInquiryId){
        try{
            CustomerInquiry customerInquiry = customerInquiryRepository.findById(customerInquiryId).get();
            if(customerInquiry.getStatus() == null) customerInquiry.setStatus(true);
            else customerInquiry.setStatus(!customerInquiry.getStatus());
            customerInquiryRepository.save(customerInquiry);
            return DefaultRes.res(StatusCode.OK, "고객의 소리 처리 여부 변경 성공");
        } catch (Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.FAILED, "고객의 소리 처리 여부 변경 실패");
        }
    }

}
