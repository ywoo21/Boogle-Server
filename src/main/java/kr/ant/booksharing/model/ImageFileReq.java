package kr.ant.booksharing.model;

import kr.ant.booksharing.domain.SellItem;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ImageFileReq {
    private MultipartFile imageFile;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}