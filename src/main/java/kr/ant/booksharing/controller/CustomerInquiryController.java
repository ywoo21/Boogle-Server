package kr.ant.booksharing.controller;

import kr.ant.booksharing.domain.CustomerInquiry;
import kr.ant.booksharing.repository.CustomerInquiryRepository;
import kr.ant.booksharing.service.CustomerInquiryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("customerInquiry")
public class CustomerInquiryController {
    private final CustomerInquiryService customerInquiryService;

    public CustomerInquiryController(final CustomerInquiryService customerInquiryService) {this.customerInquiryService = customerInquiryService;}

    @GetMapping("")
    public ResponseEntity getAllCustomerInquiries(){
        try {
            return new ResponseEntity<>(customerInquiryService.findAllCustomerInquiries(), HttpStatus.OK);
        } catch (Exception e){
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity saveCustomerInquiry(@RequestBody final CustomerInquiry customerInquiry){
        try {
            return new ResponseEntity<>(customerInquiryService.saveCustomerInquiry(customerInquiry), HttpStatus.OK);
        } catch (Exception e){
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
