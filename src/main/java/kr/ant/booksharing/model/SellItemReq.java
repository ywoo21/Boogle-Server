package kr.ant.booksharing.model;

import kr.ant.booksharing.domain.RegiImage;
import kr.ant.booksharing.domain.SellItem;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SellItemReq {
    private String sellItemString;
    private SellItem sellItem;
    private List<RegiImage> regiImageList;
    private List<MultipartFile> imageFileList;
}
