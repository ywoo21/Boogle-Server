package kr.ant.booksharing.controller;

import kr.ant.booksharing.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService){this.userService = userService;}

    /**
     * 회원 정보 목록 열람
     *
     * @param
     * @return ResponseEntity
     */
    @GetMapping("/users")
    public ResponseEntity getAllUsers(){
        try{
            return new ResponseEntity<> (userService.findAllUsers(), HttpStatus.OK);
        } catch (Exception e){
            log.error("{}", e);
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
    }
}
