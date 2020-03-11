package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.Major;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.DepartmentMajor;
import kr.ant.booksharing.repository.MajorRepository;
import kr.ant.booksharing.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MajorService {

    private final MajorRepository majorRepository;

    public MajorService(final MajorRepository majorRepository){
        this.majorRepository = majorRepository;
    }

    /**
     * 캠퍼스별 전체 전공 저장
     *
     * @param major 회원
     * @return DefaultRes
     */
    public DefaultRes saveAllCampusMajor (final Major major) {
        try {
            majorRepository.save(major);
            return DefaultRes.res(StatusCode.CREATED, "캠퍼스별 전체 전공 저장 성공");
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "캠퍼스별 전체 전공 저장 실패");
        }
    }

    /**
     * 키워드를 포함하는 모든 전공 조회
     *
     * @param campus
     * @param keyword
     *
     * @return DefaultRes
     */
    public DefaultRes findAllMajorByCampusAndKeyword (final String campus, final String keyword) {
        try {

            // 모든 DepartmentMajor(학부, 소속 학과 리스트) 리스트 반환

            List<DepartmentMajor> departmentMajorList =
                    majorRepository.findByCampus(campus).get().getDepartmentMajorList();

            List<DepartmentMajor> searchedDepartmentMajorList = new ArrayList<>();
            List<String> searchedResultList = new ArrayList<>();

            for(int i = 0; i < departmentMajorList.size(); i++){

                String department = departmentMajorList.get(i).getDepartment();

                if(department.contains(keyword) || keyword.contains(department)){
                    searchedResultList.add(departmentMajorList.get(i).getDepartment());
                }

                List<String> majorList = departmentMajorList.get(i).getMajorList();

                for(int j = 0; j < majorList.size(); j++ ){
                    String major = majorList.get(j);

                    if(major.contains(keyword) || keyword.contains(major)){
                        searchedResultList.add(major);
                    }
                }
            }

            // Stream 을 이용한 중복 제거
            searchedResultList = searchedResultList.parallelStream().distinct().collect(Collectors.toList());


            return DefaultRes.res(StatusCode.OK, "키워드를 포함하는 모든 전공 조회 성공", searchedResultList);
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "키워드를 포함하는 모든 전공 조회 실패");
        }
    }
}
