package kr.ant.booksharing.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUrl {
    private MultipartFile image;
}
