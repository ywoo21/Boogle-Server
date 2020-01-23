package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.Item;
import kr.ant.booksharing.domain.RegiImage;
import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.model.*;
import kr.ant.booksharing.repository.ItemRepository;
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
    private final ItemRepository itemRepository;

    public SellItemService(final SellItemRepository sellItemRepository,
                           final S3FileUploadService s3FileUploadService,
                           final RegiImageRepository regiImageRepository,
                           final ItemRepository itemRepository) {
        this.sellItemRepository = sellItemRepository;
        this.s3FileUploadService = s3FileUploadService;
        this.regiImageRepository = regiImageRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * 판매 상품 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<SellItem>> findAllSellItems(final String itemId) {

        if(sellItemRepository.findAllByItemId(itemId).isPresent()){

            List<SellItem> sellItemList = sellItemRepository.findAllByItemId(itemId).get();

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_SELL_ITEM, sellItemList);

        }
        else{
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_SELL_ITEM);
        }
    }

    /**
     * 판매 상품 상세 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<SellItem> findSellItem(final String id) {

        if(sellItemRepository.findBy_id(id).isPresent()){

            SellItem sellItem = sellItemRepository.findBy_id(id).get();

            return DefaultRes.res(StatusCode.OK, "판매 상품 상세 조회 성공", sellItem);

        }
        else{
            return DefaultRes.res(StatusCode.NOT_FOUND, "판매 상품 상세 조회 실패");
        }
    }

    /**
     * 물품 정보 등록
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<SellItem>> saveItem(final SellItem sellItem,
                                               final List<MultipartFile> imageFileList) {
        try{

            List<String> regiImageUrlList = new ArrayList<>();
            for(MultipartFile m : imageFileList){
                regiImageUrlList.add(s3FileUploadService.upload(m));
            }

            sellItem.setRegiImageUrlList(regiImageUrlList);

            Item item = new Item();
            if(itemRepository.findByItemId(sellItem.getItemId()).isPresent()){

                int currCont = itemRepository.findByItemId(sellItem.getItemId()).get().getRegiCount();

                item.setItemId(sellItem.getItemId());
                item.setTitle(sellItem.getTitle());
                item.setRegiCount(currCont++);
                itemRepository.save(item);

            }
            else{
                item.setItemId(sellItem.getItemId());
                item.setTitle(sellItem.getTitle());
                item.setRegiCount(1);
                itemRepository.save(item);
            }

            sellItemRepository.save(sellItem);

            return DefaultRes.res(StatusCode.CREATED, "물품 정보 등록 성공");

        }
        catch(Exception e){

            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, "물품 정보 등록 실패");

        }
    }
}
