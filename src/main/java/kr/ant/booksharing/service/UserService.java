package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.User;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.SignIn.SignInReq;
import kr.ant.booksharing.model.UserModificationReq;
import kr.ant.booksharing.model.UserModificationRes;
import kr.ant.booksharing.repository.UserRepository;
import kr.ant.booksharing.utils.ResponseMessage;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;


@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JavaMailSender javaMailSender;
    private final MailContentBuilderService mailContentBuilderService;
    private final MailSenderService mailSenderService;


    public UserService(final PasswordEncoder passwordEncoder,
                       final UserRepository userRepository, final JwtService jwtService,
                       final JavaMailSender javaMailSender,
                       final MailContentBuilderService mailContentBuilderService,
                       final MailSenderService mailSenderService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.javaMailSender = javaMailSender;
        this.mailContentBuilderService = mailContentBuilderService;
        this.mailSenderService = mailSenderService;
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
            int userId = userRepository.save(user).getId();

            final JwtService.TokenRes tokenRes =
                    new JwtService.TokenRes(jwtService.create(userId));

            return DefaultRes.res(StatusCode.CREATED, "회원 정보 저장 성공", tokenRes.getToken());
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "회원 정보 저장 실패", "");
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
     * 회원 정보 인증
     *
     * @param signInReq 회원 데이터
     * @return DefaultRes
     */
    public String getUserToken(final SignInReq signInReq) {
        try {
            if(userRepository.findByEmail(signInReq.getEmail()).isPresent()){
                User user = userRepository.findByEmail(signInReq.getEmail()).get();
                if(passwordEncoder.matches(signInReq.getPassword(), user.getPassword())){
                    final JwtService.TokenRes tokenRes =
                            new JwtService.TokenRes(jwtService.create(user.getId()));
                    return tokenRes.getToken();
                }
                else { return ""; }
            }
            else{
                return "";
            }
        } catch (Exception e) {
            System.out.println(e);
            return "";
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
    public DefaultRes sendAuthEmail(final String userName, final String email, final String campusEmail) {
        try {

            String code = Integer.toString(Math.abs(email.hashCode())).substring(0,4);

            if(code.length() < 4){
                String zeroString = "0";
                for(int i = 0; i <  4 - code.length(); i++){
                    zeroString += "0";
                }
                code = zeroString + code;
            }
            String content = mailContentBuilderService.buildEmailAuth(userName, code);

            MimeMessagePreparator messagePreparator =
                    mailSenderService.createMimeMessage(campusEmail, "북을 이메일 인증", content);

            javaMailSender.send(messagePreparator);
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
     * 회원 정보 변경 시 현재 회원 정보 조회
     *
     * @param userIdx 회원 고유 번호
     * @return DefaultRes
     */
    public DefaultRes findCurrentUserForUserModification(final int userIdx) {
        try {

            User user = userRepository.findById(userIdx).get();
            UserModificationRes userModificationRes =
                    UserModificationRes.builder()
                        .email(user.getEmail())
                        .name(user.getName())
                        .nickname(user.getNickname())
                        .major(user.getMajorList())
                        .semester(user.getSemester())
                        .phoneNumber(user.getPhoneNumber())
                        .build();

            return DefaultRes.res(StatusCode.OK, "회원 정보 조회 성공", userModificationRes);

        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "회원 정보 조회 실패ㅡ");
        }
    }

    /**
     * 회원 정보 변경
     *
     * @param userIdx 회원 고유 번호
     * @return DefaultRes
     */
    public DefaultRes modifyUser(final UserModificationReq userModificationReq,
                                 final int userIdx) {
        try {
            User user = userRepository.findById(userIdx).get();

            if(!userModificationReq.getEmail().equals("")) user.setEmail(userModificationReq.getEmail());
            if(!userModificationReq.getName().equals("")) user.setName(userModificationReq.getName());
            if(!userModificationReq.getNickname().equals("")) user.setNickname(userModificationReq.getNickname());
            if(!userModificationReq.getMajor().equals("")) user.setMajorList(userModificationReq.getMajor());
            if(!userModificationReq.getSemester().equals("")) user.setSemester(userModificationReq.getSemester());
            if(!userModificationReq.getPhoneNumber().equals("")) user.setPhoneNumber(userModificationReq.getPhoneNumber());

            if(!userModificationReq.getPassword().equals(""))
                user.setPassword(passwordEncoder.encode(userModificationReq.getPassword()));

            userRepository.save(user);

            return DefaultRes.res(StatusCode.CREATED, "회원 정보 수정 성공");
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "회원 정보 수정 실패");
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

    /**
     * 회원 정보 목록 열람
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes findAllUsers() {
        try {
            return DefaultRes.res(StatusCode.OK, "회원 정보 목록 열람 성공", userRepository.findAll());
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.NOT_FOUND, "회원 정보 목록 열람 실패");
        }
    }
}
