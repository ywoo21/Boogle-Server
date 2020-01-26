package kr.ant.booksharing.service;

import kr.ant.booksharing.dao.UserMapper;
import kr.ant.booksharing.domain.User;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.SignIn.SignInReq;
import kr.ant.booksharing.repository.UserRepository;
import kr.ant.booksharing.utils.ResponseMessage;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;


@Slf4j
@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender javaMailSender;


    public UserService(final UserMapper userMapper, final PasswordEncoder passwordEncoder,
                       final UserRepository userRepository, final JwtService jwtService,
                       final JavaMailSender javaMailSender) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.javaMailSender = javaMailSender;
    }


    /**
     * 회원 정보 저장
     *
     * @param user 회원
     * @return DefaultRes
     */
    public DefaultRes saveUser(final User user) {
        try {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            System.out.println(user.toString());
            userRepository.save(user);
            return DefaultRes.res(StatusCode.CREATED, "회원 정보 저장 성공");
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "회원 정보 저장 실패");
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
            if(userRepository.findByEmail(signInReq.getEmail()).isPresent()){
                User user = userRepository.findByEmail(signInReq.getEmail()).get();
                if(passwordEncoder.matches(signInReq.getPassword(), user.getPassword())){
                    final JwtService.TokenRes tokenRes =
                            new JwtService.TokenRes(jwtService.create(user.getId()));
                    return DefaultRes.res(StatusCode.OK, "로그인 성공",tokenRes.getToken());
                }
                else { return DefaultRes.res(StatusCode.NOT_FOUND, "로그인 실패 ");}
            }
            else{
                return DefaultRes.res(StatusCode.NOT_FOUND, "로그인 실패 ");
            }
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
    /**
     * 이메일 중복 검사
     *
     * @param email 회원 이메일
     * @return DefaultRes
     */
    public DefaultRes checkEmail(final String email) {
        try {
            if(userRepository.findByEmail(email).isPresent()){
                return DefaultRes.res(StatusCode.OK, "중복된 이메일");
            }
            else{
                return DefaultRes.res(StatusCode.NOT_FOUND, "사용 가능한 이메일");
            }
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 닉네임 중복 검사
     *
     * @param nickname 회원 닉네임
     * @return DefaultRes
     */
    public DefaultRes checkNickname(final String nickname) {
        try {
            if(userRepository.findByNickname(nickname).isPresent()){
                return DefaultRes.res(StatusCode.OK, "중복된 닉네임");
            }
            else{
                return DefaultRes.res(StatusCode.NOT_FOUND, "사용 가능한 닉네임");
            }
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 인증 이메일 전송
     *
     * @return DefaultRes
     */
    public DefaultRes sendAuthEmail(final String email, final String campusEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("boogleforus@gmail.com");
            message.setTo(campusEmail);
            message.setSubject("북을 이메일 인증");
            String code = Integer.toString(Math.abs(email.hashCode())).substring(0,4);
            if(code.length() < 4){
                String zeroString = "0";
                for(int i = 0; i <  4 - code.length(); i++){
                    zeroString += "0";
                }
                code = zeroString + code;
            }
            message.setText("인증 코드 : " + code);
            javaMailSender.send(message);
            return DefaultRes.res(StatusCode.OK, "메일 전송 성공");
        }
        catch(Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.BAD_REQUEST, "메일 전송 실패");
        }
    }

    /**
     * 이메일 인증
     *
     * @return DefaultRes
     */
    public DefaultRes emailAuth(final String email, final String authCode) {
        try {
            if(authCode.substring(0,1).equals("0") || authCode.substring(0,2).equals("00")
            || authCode.substring(0,3).equals("000") || authCode.equals("0000")){
                authCode.replace("0", "");
            }

            String code = Integer.toString(Math.abs(email.hashCode())).substring(0,4);
            if(code.length() < 4){
                String zeroString = "0";
                for(int i = 0; i <  4 - code.length(); i++){
                    zeroString += "0";
                }
                code = zeroString + code;
            }

            if(authCode.equals(code))
                return DefaultRes.res(StatusCode.OK, "이메일 인증 성공", true);
            else return DefaultRes.res(StatusCode.OK, "이메일 인증 실패", false);

        }
        catch(Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.BAD_REQUEST, "이메일 인증 실패");
        }
    }

    /**
     * 회원 비밀번호 변경
     *
     * @param signInReq 회원 이메일
     * @return DefaultRes
     */
    public DefaultRes modifyPassword(final SignInReq signInReq) {
        try {
            userMapper.changeUserPassword(signInReq.getEmail(), passwordEncoder.encode(signInReq.getPassword()));
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CHANGED_PWD);
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * @return 인증을 위해 사용자에게 전달 될 네 자리의 인증 번호
     */
    private String getAuthenticationNumber() {
        Random random = new Random();

        return String.valueOf(random.nextInt(9000) + 1000);
    }

    public int authorization(final String jwt) {

        final int userIdx = jwtService.decode(jwt).getUser_idx();
        if (userIdx == -1) return -1;

        final Optional<User > user = userRepository.findById(userIdx);
        if (!user.isPresent()) return -1;

        return userIdx;

    }
}
