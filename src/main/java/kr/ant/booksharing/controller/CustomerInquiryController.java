package kr.ant.booksharing.controller;

import kr.ant.booksharing.domain.CustomerInquiry;
import kr.ant.booksharing.model.CustomerInquiryReq;
import kr.ant.booksharing.repository.CustomerInquiryRepository;
import kr.ant.booksharing.service.CustomerInquiryService;
import kr.ant.booksharing.service.UserService;
import kr.ant.booksharing.utils.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("customerInquiry")
public class CustomerInquiryController {
    private final CustomerInquiryService customerInquiryService;
    private final UserService userService;

    public CustomerInquiryController(final CustomerInquiryService customerInquiryService,
                                     final UserService userService) {
        this.customerInquiryService = customerInquiryService;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity getAllCustomerInquiries(){
        try {
            return new ResponseEntity<>(customerInquiryService.findAllCustomerInquiries(), HttpStatus.OK);
        } catch (Exception e){
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/done")
    public ResponseEntity getAllDoneCustomerInquiries(){
        try {
            return new ResponseEntity<>(customerInquiryService.findAllDoneCustomerInquiries(), HttpStatus.OK);
        } catch (Exception e){
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/undone")
    public ResponseEntity getAllUnDoneCustomerInquiries(){
        try {
            return new ResponseEntity<>(customerInquiryService.findAllUnDoneCustomerInquiries(), HttpStatus.OK);
        } catch (Exception e){
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("")
    public ResponseEntity saveCustomerInquiry(final CustomerInquiryReq customerInquiryReq,
                                              @RequestPart(value="imageFileList", required = false)
                                              final List<MultipartFile> imageFileList){
        try {
            CustomerInquiry customerInquiry = customerInquiryReq.getCustomerInquiry();
            final String jwt = customerInquiryReq.getToken();
            int userIdx = -1;
            if(!jwt.equals("")) userIdx = userService.authorization(jwt);
            return new ResponseEntity<>(customerInquiryService.saveCustomerInquiry(userIdx, customerInquiry, imageFileList), HttpStatus.OK);
        } catch (Exception e){
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/changeStatus")
    public ResponseEntity changeCustomerInquiryStatus(@RequestParam(value="customerInquiryId") String customerInquiryId){
        try{
            return new ResponseEntity<>(customerInquiryService.changeStatus(customerInquiryId), HttpStatus.OK);
        } catch (Exception e){
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
