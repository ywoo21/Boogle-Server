package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.UserBookmark;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.repository.UserBookmarkRepository;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserBookmarkService {
    private final UserBookmarkRepository userBookmarkRepository;

    public UserBookmarkService(final UserBookmarkRepository userBookmarkRepository) {
        this.userBookmarkRepository = userBookmarkRepository;
    }

    /**
     * 찜하기 저장
     *
     * @param
     * @return DefaultRes
     */
    public DefaultRes<UserBookmark> saveUserbookmark(final int userId, final String sellItemId) {
        try{
            UserBookmark userBookmark = new UserBookmark();
            userBookmark.setUserId(userId);
            userBookmark.setSellItemId(sellItemId);
            userBookmarkRepository.save(userBookmark);

            return DefaultRes.res(StatusCode.CREATED, "찜하기 저장 성공");

        }
        catch(Exception e){

            return DefaultRes.res(StatusCode.DB_ERROR, "찜하기 저장 실패");

        }
    }
}
