package kr.ant.booksharing.model;

import kr.ant.booksharing.domain.UserCampusAuthImage;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserCampusAuthImageReq {
    private MultipartFile userCampusAuthImageFile;
}
