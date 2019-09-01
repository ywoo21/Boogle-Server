package kr.ant.booksharing.service;

import kr.ant.booksharing.dao.UserMapper;
import kr.ant.booksharing.model.Book;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.SignIn.SignInRes;
import kr.ant.booksharing.model.SignIn.SignInReq;
import kr.ant.booksharing.model.SignUp.SignUpReq;
import kr.ant.booksharing.utils.ResponseMessage;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(final UserMapper userMapper, final PasswordEncoder passwordEncoder,
                       final JwtService jwtService) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

    }


    /**
     * 회원 정보 저장
     *
     * @param signUpReq 회원 데이터
     * @return DefaultRes
     */
    public DefaultRes saveUser(final SignUpReq signUpReq) {
        try {
            String encodedPassword = passwordEncoder.encode(signUpReq.getPassword());
            signUpReq.setPassword(encodedPassword);
            userMapper.saveUser(signUpReq);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_BOARD);
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 회원 정보 인증
     *
     * @param signInReq 회원 데이터
     * @return DefaultRes
     */
    public DefaultRes authUser(final SignInReq signInReq) {
        try {
            if(userMapper.findUser(signInReq) != null){
                SignInRes signInRes = userMapper.findUser(signInReq);
                if(passwordEncoder.matches(signInReq.getPassword(), signInRes.getPassword())){
                    final JwtService.TokenRes tokenRes =
                            new JwtService.TokenRes(jwtService.create(signInRes.getId()));

                    signInRes.setTokenRes(tokenRes); signInRes.setPassword("");

                    return DefaultRes.res(StatusCode.CREATED, ResponseMessage.LOGIN_SUCCESS,signInRes);
                }
                else { return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.LOGIN_FAIL); }
            }
            else{
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.LOGIN_FAIL);
            }


        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
    /**
     * 이메일 중복 검사
     *
     * @param email 회원 데이터
     * @return DefaultRes
     */
    public DefaultRes checkEmail(final String email) {
        try {
            if(userMapper.checkEmail(email) != null && !(userMapper.checkEmail(email).equals(""))){
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.ALREADY_USER);
            }
            else{
                return DefaultRes.res(StatusCode.OK, ResponseMessage.USABLE_USER);
            }
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
