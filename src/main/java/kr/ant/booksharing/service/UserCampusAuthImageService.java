package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.User;
import kr.ant.booksharing.domain.UserBookmark;
import kr.ant.booksharing.domain.UserCampusAuthImage;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.UserCampusAuthImageReq;
import kr.ant.booksharing.repository.UserBookmarkRepository;
import kr.ant.booksharing.repository.UserCampusAuthImageRepository;
import kr.ant.booksharing.repository.UserRepository;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class UserCampusAuthImageService {
    private final UserCampusAuthImageRepository userCampusAuthImageRepository;
    private final UserRepository userRepository;
    private final S3FileUploadService s3FileUploadService;

    public UserCampusAuthImageService(final UserCampusAuthImageRepository userCampusAuthImageRepository,
                                      final UserRepository userRepository,
                                      final S3FileUploadService s3FileUploadService) {
        this.userCampusAuthImageRepository = userCampusAuthImageRepository;
        this.userRepository = userRepository;
        this.s3FileUploadService = s3FileUploadService;
    }

    /**
     * 학생 인증 사진 저장
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<UserCampusAuthImage> saveUserCampusAuthImage(final MultipartFile userCampusAuthImageFile,
                                                                   final int userIdx) {
        try{

            String imageUrl =
                    s3FileUploadService.upload(userCampusAuthImageFile);

            UserCampusAuthImage userCampusAuthImage = new UserCampusAuthImage();

            userCampusAuthImage.setAuthImageUrl(imageUrl);

            userCampusAuthImage.setUserId(userIdx);

            userCampusAuthImageRepository.save(userCampusAuthImage);

            return DefaultRes.res(StatusCode.CREATED, "학생 인증 사진 저장 성공");

        }
        catch(Exception e){

            return DefaultRes.res(StatusCode.DB_ERROR, "학생 인증 사진 저장 실패");

        }
    }

    /**
     * 학생 인증 사진 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<UserCampusAuthImage> findUserCampusAuthImageByUserId(final int userId) {
        try{

            return DefaultRes.res(StatusCode.OK, "학생 인증 사진 조회 성공",
                    userCampusAuthImageRepository.findByUserId(userId).get());

        }
        catch(Exception e){

            return DefaultRes.res(StatusCode.DB_ERROR, "학생 인증 사진 조회 실패");

        }
    }

    /**
     * 학생 인증 상태 변경
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<UserCampusAuthImage> changeUserAuthComplete(final int userId) {
        try{

            User user = userRepository.findById(userId).get();

            if(!user.isAuthComplete()) {
                user.setAuthComplete(true);
                userRepository.save(user);
            }

            return DefaultRes.res(StatusCode.OK, "학생 인증 상태 변경 성공");

        }
        catch(Exception e){

            return DefaultRes.res(StatusCode.DB_ERROR, "학생 인증 상태 변경 실패");

        }
    }
}
