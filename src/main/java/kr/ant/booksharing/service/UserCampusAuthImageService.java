package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.UserBookmark;
import kr.ant.booksharing.domain.UserCampusAuthImage;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.UserCampusAuthImageReq;
import kr.ant.booksharing.repository.UserBookmarkRepository;
import kr.ant.booksharing.repository.UserCampusAuthImageRepository;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserCampusAuthImageService {
    private final UserCampusAuthImageRepository userCampusAuthImageRepository;
    private final S3FileUploadService s3FileUploadService;

    public UserCampusAuthImageService(final UserCampusAuthImageRepository userCampusAuthImageRepository,
                                      final S3FileUploadService s3FileUploadService) {
        this.userCampusAuthImageRepository = userCampusAuthImageRepository;
        this.s3FileUploadService = s3FileUploadService;
    }

    /**
     * 학생 인증 사진 저장
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<UserCampusAuthImage> saveUserCampusAuthImage(final UserCampusAuthImageReq userCampusAuthImageReq) {
        try{

            String imageUrl =
                    s3FileUploadService.upload(userCampusAuthImageReq.getUserCampusAuthImageFile());

            UserCampusAuthImage userCampusAuthImage = userCampusAuthImageReq.getUserCampusAuthImage();

            userCampusAuthImage.setAuthImageUrl(imageUrl);

            userCampusAuthImageRepository.save(userCampusAuthImage);

            return DefaultRes.res(StatusCode.CREATED, "학생 인증 사진 성공");

        }
        catch(Exception e){

            return DefaultRes.res(StatusCode.DB_ERROR, "학생 인증 사진 실패");

        }
    }
}
