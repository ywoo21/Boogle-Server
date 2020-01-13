package kr.ant.booksharing.model;

import kr.ant.booksharing.domain.RegiImage;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RegiImageReq {
    private List<MultipartFile> imageList;
    private RegiImage regiImage;
}
