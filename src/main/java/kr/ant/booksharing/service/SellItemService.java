package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.Item;
import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.domain.SellItemHistory;
import kr.ant.booksharing.domain.User;
import kr.ant.booksharing.model.*;
import kr.ant.booksharing.repository.*;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class SellItemService {

    private final SellItemRepository sellItemRepository;
    private final SellItemHistoryRepository sellItemHistoryRepository;
    private final S3FileUploadService s3FileUploadService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserBookmarkRepository userBookmarkRepository;
    private final UserService userService;

    public SellItemService(final SellItemRepository sellItemRepository,
                           final SellItemHistoryRepository sellItemHistoryRepository,
                           final S3FileUploadService s3FileUploadService,
                           final RegiImageRepository regiImageRepository,
                           final ItemRepository itemRepository,
                           final UserRepository userRepository,
                           final UserBookmarkRepository userBookmarkRepository,
                           final UserService userService) {
        this.sellItemRepository = sellItemRepository;
        this.sellItemHistoryRepository = sellItemHistoryRepository;
        this.s3FileUploadService = s3FileUploadService;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.userBookmarkRepository = userBookmarkRepository;
        this.userService = userService;
    }

    /**
     * 판매 상품 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<List<SellItem>> findAllSellItems(final String itemId) {

        if(sellItemRepository.findAllByItemIdAndIsTraded(itemId,false).isPresent()){

            List<SellItem> sellItemList = sellItemRepository.findAllByItemIdAndIsTraded(itemId, false).get();

            return DefaultRes.res(StatusCode.OK, "판매 상품 조회 성공", sellItemList);

        }
        else{
            return DefaultRes.res(StatusCode.NOT_FOUND, "판매 상품 조회 실패");
        }
    }

    /**
     * 판매 상품 상세 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<SellItemRes> findSellItem(final String token, final String id) {

        if(sellItemRepository.findBy_id(id).isPresent()){

            SellItemRes sellItemRes = new SellItemRes();
            SellItem sellItem = sellItemRepository.findBy_id(id).get();
            sellItemRes.setSellItem(sellItem);
            User sellerUser = userRepository.findById(sellItem.getSellerId()).get();
            sellerUser.setPassword("");
            sellItemRes.setSellerUser(userRepository.findById(sellItem.getSellerId()).get());

            if(token.equals("")){
                sellItemRes.setBookmarked(false);
            }

            else if(userBookmarkRepository.findByUserIdAndSellItemId
                    (userService.authorization(token), sellItem.get_id()).isPresent()){
                sellItemRes.setBookmarked(true);
            }
            else { sellItemRes.setBookmarked(false); }

            return DefaultRes.res(StatusCode.OK, "판매 상품 상세 조회 성공", sellItemRes);

        }
        else{
            return DefaultRes.res(StatusCode.NOT_FOUND, "판매 상품 상세 조회 실패");
        }
    }

    /**
     * 판매 상품 등록
     *
     * @param
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes<List<SellItem>> saveItem(final SellItem sellItem,
                                               final List<MultipartFile> imageFileList) {
        try{

            List<String> regiImageUrlList = new ArrayList<>();
            for(MultipartFile m : imageFileList){
                regiImageUrlList.add(s3FileUploadService.upload(m));
            }

            sellItem.setRegiImageUrlList(regiImageUrlList);

            int originalPrice = Integer.parseInt(sellItem.getOriginalPrice());

            if(sellItem.getDealType() == 0){
                sellItem.setRegiPrice(sellItem.getOriginalPrice());
            }
            else{
                sellItem.setRegiPrice(Integer.toString(originalPrice + 500));
            }

            Item item = new Item();
            if(itemRepository.findByItemId(sellItem.getItemId()).isPresent()){

                Item currItem =
                        itemRepository.findByItemId(sellItem.getItemId()).get();

                item.set_id(currItem.get_id());
                item.setItemId(currItem.getItemId());
                item.setTitle(currItem.getTitle());

                if(!sellItem.getSubject().equals("")){
                    List<String> subjectList = currItem.getSubjectList();
                    subjectList.add(sellItem.getSubject());
                    item.setSubjectList(subjectList);
                }
                if(!sellItem.getProfessor().equals("")){
                    List<String> professorList = currItem.getProfessorList();
                    professorList.add(sellItem.getProfessor());
                    item.setProfessorList(professorList);
                }

                item.setRegiCount(currItem.getRegiCount() + 1);

                itemRepository.save(item);

            }
            else{
                item.setItemId(sellItem.getItemId());
                item.setTitle(sellItem.getTitle());
                item.setSubjectList(new ArrayList<>(Arrays.asList(sellItem.getSubject())));
                item.setProfessorList(new ArrayList<>(Arrays.asList(sellItem.getProfessor())));
                item.setRegiCount(1);
                itemRepository.save(item);
            }


            String id = sellItemRepository.save(sellItem).get_id();

            SellItemHistory sellItemHistory =
                    SellItemHistory.builder()
                        ._id(id)
                        .itemId(sellItem.getItemId())
                        .author(sellItem.getAuthor())
                        .comment(sellItem.getComment())
                        .dealType(sellItem.getDealType())
                        .imageUrl(sellItem.getImageUrl())
                        .isTraded(sellItem.isTraded())
                        .price(sellItem.getPrice())
                        .pubdate(sellItem.getPubdate())
                        .publisher(sellItem.getPublisher())
                        .qualityGeneral(sellItem.getQualityGeneral())
                        .qualityExtraList(sellItem.getQualityExtraList())
                        .regiImageUrlList(sellItem.getRegiImageUrlList())
                        .title(sellItem.getTitle())
                        .regiTime(sellItem.getRegiTime())
                        .sellerId(sellItem.getSellerId())
                        .regiPrice(sellItem.getRegiPrice())
                        .originalPrice(sellItem.getOriginalPrice())
                            .subject(sellItem.getSubject())
                            .professor(sellItem.getProfessor())
                        .build();

            sellItemHistoryRepository.save(sellItemHistory);

            return DefaultRes.res(StatusCode.CREATED, "물품 정보 등록 성공");

        }
        catch(Exception e){

            System.out.println(e);

            return DefaultRes.res(StatusCode.DB_ERROR, "물품 정보 등록 실패");

        }
    }

    /**
     * 물품 정보 등록
     *
     * @param
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes<String> saveImageInS3(final ImageFileReq imageFileReq) {
        try{

            String imageUrl = s3FileUploadService.upload(imageFileReq.getImageFile());
            return DefaultRes.res(StatusCode.CREATED, "물품 정보 등록 성공", imageUrl);

        }
        catch(Exception e){

            System.out.println(e);

            return DefaultRes.res(StatusCode.DB_ERROR, "물품 정보 등록 실패");

        }
    }
}