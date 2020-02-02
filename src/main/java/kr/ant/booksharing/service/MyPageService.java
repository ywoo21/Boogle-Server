package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.domain.Transaction;
import kr.ant.booksharing.domain.User;
import kr.ant.booksharing.domain.UserBookmark;
import kr.ant.booksharing.model.BookmarkedItem;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.MyPageRes;
import kr.ant.booksharing.model.TransactionItem;
import kr.ant.booksharing.repository.SellItemRepository;
import kr.ant.booksharing.repository.TransactionRepository;
import kr.ant.booksharing.repository.UserBookmarkRepository;
import kr.ant.booksharing.repository.UserRepository;
import kr.ant.booksharing.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final UserBookmarkRepository userBookmarkRepository;
    private final SellItemRepository sellItemRepository;
    private final TransactionRepository transactionRepository;

    public MyPageService(final UserRepository userRepository,
                         final UserBookmarkRepository userBookmarkRepository,
                         final SellItemRepository sellItemRepository,
                         final TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.userBookmarkRepository = userBookmarkRepository;
        this.sellItemRepository = sellItemRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * 마이페이지 조회
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<MyPageRes> findMyPage(final int userId) {
        try{
            MyPageRes myPageRes = new MyPageRes();

            myPageRes.setUserName(userRepository.findById(userId).get().getName());

            if(userBookmarkRepository.findAllByUserId(userId).isPresent()){

                List<UserBookmark> userBookmarkList = userBookmarkRepository.findAllByUserId(userId).get();
                List<BookmarkedItem> bookmarkedItemList = new ArrayList<>();

                for(UserBookmark userBookmark : userBookmarkList){

                    SellItem sellItem =
                            sellItemRepository.findBy_id(userBookmark.getSellItemId()).get();

                    BookmarkedItem bookmarkedItem =
                            BookmarkedItem.builder()
                                    .sellItemId(sellItem.get_id())
                                    .imageUrl(sellItem.getImageUrl())
                                    .regiPrice(sellItem.getRegiPrice())
                                    .title(sellItem.getTitle())
                                    .build();

                    bookmarkedItemList.add(bookmarkedItem);
                }
                myPageRes.setBookmarkedItemList(bookmarkedItemList);
            }
            else{
                myPageRes.setBookmarkedItemList(new ArrayList<>());
            }

            List<TransactionItem> buyTransList = new ArrayList<>();
            List<TransactionItem> sellTransList = new ArrayList<>();

            if(transactionRepository.findAllByBuyerId(userId).isPresent()){

                List<Transaction> buyerTransactionList =
                        transactionRepository.findAllByBuyerId(userId).get();

                for(Transaction buyerTransaction : buyerTransactionList){

                    TransactionItem transactionItem =

                            TransactionItem.builder()
                                    .sellItemId(buyerTransaction.getSellItemId())
                                    .traderName(userRepository.findById(buyerTransaction.getSellerId()).get().getName())
                                    .traderPhoneNumber(userRepository.findById(buyerTransaction.getSellerId()).get().getPhoneNumber())
                                    .transactionType(buyerTransaction.getTransactionType())
                                    .transactionCreatedTime(buyerTransaction.getTransCreatedTime())
                                    .transactionProcessedTimeList(buyerTransaction.getTransactionTimeList())
                                    .transactionStep(buyerTransaction.getStep())

                                    .title(sellItemRepository.findBy_id(buyerTransaction.getSellItemId())
                                            .get().getTitle())

                                    .transPrice(sellItemRepository.findBy_id(buyerTransaction.getSellItemId())
                                            .get().getRegiPrice())

                                    .build();

                    buyTransList.add(transactionItem);

                }
            }

            if(transactionRepository.findAllBySellerId(userId).isPresent()){

                List<Transaction> sellerTransactionList =
                        transactionRepository.findAllBySellerId(userId).get();

                for(Transaction sellerTransaction : sellerTransactionList){

                    TransactionItem transactionItem =

                            TransactionItem.builder()
                                    .sellItemId(sellerTransaction.getSellItemId())
                                    .traderName(userRepository.findById(sellerTransaction.getBuyerId()).get().getName())
                                    .traderPhoneNumber(userRepository.findById(sellerTransaction.getBuyerId()).get().getPhoneNumber())
                                    .transactionType(sellerTransaction.getTransactionType())
                                    .transactionCreatedTime(sellerTransaction.getTransCreatedTime())
                                    .transactionProcessedTimeList(sellerTransaction.getTransactionTimeList())
                                    .transactionStep(sellerTransaction.getStep())

                                    .title(sellItemRepository.findBy_id(sellerTransaction.getSellItemId())
                                            .get().getTitle())

                                    .transPrice(sellItemRepository.findBy_id(sellerTransaction.getSellItemId())
                                            .get().getRegiPrice())

                                    .build();

                    sellTransList.add(transactionItem);

                }
            }
            myPageRes.setBuyTransList(buyTransList);
            myPageRes.setSellTransList(sellTransList);

            return DefaultRes.res(StatusCode.OK, "마이페이지 조회 성공", myPageRes);
        }
        catch (Exception e){
            System.out.println(e);
            return DefaultRes.res(StatusCode.NOT_FOUND, "마이페이지 조회 실패");
        }
    }
}
