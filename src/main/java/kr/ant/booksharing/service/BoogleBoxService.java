package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.BoogleBox;
import kr.ant.booksharing.domain.SellItem;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.repository.BoogleBoxRepository;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BoogleBoxService {
    private final BoogleBoxRepository boogleBoxRepository;

    public BoogleBoxService(BoogleBoxRepository boogleBoxRepository){
        this.boogleBoxRepository = boogleBoxRepository;
    }

    /**
     * 북을박스 전체 정보 조회
     *
     * @return DefaultRes
     */
    public DefaultRes<List<BoogleBox>> findAllBoogleBoxes(){
        try{
            List<BoogleBox> boogleBoxList = boogleBoxRepository.findAll();
            return DefaultRes.res(StatusCode.OK, "북을박스 전체 정보 조회 성공", boogleBoxList);
        }catch (Exception e){
            return DefaultRes.res(StatusCode.NOT_FOUND, "북을박스 전체 정보 조회 실패");
        }
    }
}
