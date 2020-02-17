package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.Major;
import kr.ant.booksharing.model.DefaultRes;
import kr.ant.booksharing.model.DepartmentMajor;
import kr.ant.booksharing.repository.MajorRepository;
import kr.ant.booksharing.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
            List<DepartmentMajor> departmentMajorList =
                    majorRepository.findByCampus(campus).get().getDepartmentMajorList();

            List<DepartmentMajor> searchedDepartmentMajorList = new ArrayList<>();

            for(int i = 0; i < departmentMajorList.size(); i++){

                String department = departmentMajorList.get(i).getDepartment();

                if(department.contains(keyword) || keyword.contains(department)){
                    searchedDepartmentMajorList.add(departmentMajorList.get(i));
                    continue;
                }

                List<String> majorList = departmentMajorList.get(i).getMajorList();

                for(int j = 0; j < majorList.size(); j++ ){
                    String major = majorList.get(j);

                    if(major.contains(keyword) || keyword.contains(major)){
                        searchedDepartmentMajorList.add(departmentMajorList.get(i));
                        continue;
                    }
                }

            }

            return DefaultRes.res(StatusCode.OK, "키워드를 포함하는 모든 전공 조회 성공", searchedDepartmentMajorList);
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "키워드를 포함하는 모든 전공 조회 실패");
        }
    }
}
