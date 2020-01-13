package kr.ant.booksharing.service;

import com.sun.org.apache.xpath.internal.operations.Mult;
import kr.ant.booksharing.domain.RegiImage;
import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.model.*;
import kr.ant.booksharing.repository.RegiImageRepository;
import kr.ant.booksharing.repository.SellItemRepository;
import kr.ant.booksharing.utils.ResponseMessage;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SellItemService {

    private final SellItemRepository sellItemRepository;
    private final RegiImageRepository regiImageRepository;
    private final S3FileUploadService s3FileUploadService;

    public SellItemService(final SellItemRepository sellItemRepository,
                           final S3FileUploadService s3FileUploadService,
                           final RegiImageRepository regiImageRepository) {
        this.sellItemRepository = sellItemRepository;
        this.s3FileUploadService = s3FileUploadService;
        this.regiImageRepository = regiImageRepository;
    }

    /**
     * 책 상세 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<SellItem>> findSellItems(final int id) {
        if(sellItemRepository.findAllById(id).isPresent()){
            List<SellItem> sellItemList = sellItemRepository.findAllById(id).get();
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_SELL_ITEM, sellItemList);
        }
        else{
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_SELL_ITEM);
        }
    }

    /**
     * 물품 정보 등록
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<SellItem>> saveItem(final SellItem sellItem, final List<MultipartFile> imageFileList,
                                               final List<RegiImage> regiImageList) {
        try{
            int id = sellItemRepository.save(sellItem).getId();
            for(MultipartFile m : imageFileList){
                RegiImage regiImage = new RegiImage();
                regiImage.setImageUrl(s3FileUploadService.upload(m));
                regiImage.setId(id);
                regiImageList.add(regiImage);
            }
            regiImageRepository.saveAll(regiImageList);
            return DefaultRes.res(StatusCode.CREATED, "물품 정보 등록 성공");
        }
        catch(Exception e){
            return DefaultRes.res(StatusCode.DB_ERROR, "물품 정보 등록 실패");
        }
    }

    /**
     * 책 상세 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<String> saveImageUrl(final ImageUrl imageUrl) {
        try{
            return DefaultRes.res(StatusCode.CREATED, "S3에 저장되었습니다.",
                    s3FileUploadService.upload(imageUrl.getImage()));
        }
        catch(Exception e){
            return DefaultRes.res(StatusCode.NOT_FOUND, "S3 저장 실패");
        }
    }
}
