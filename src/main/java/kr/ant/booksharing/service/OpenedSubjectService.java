package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.Major;
import kr.ant.booksharing.domain.OpenedSubject;
import kr.ant.booksharing.model.*;
import kr.ant.booksharing.repository.MajorRepository;
import kr.ant.booksharing.repository.OpenedSubjectRepository;
import kr.ant.booksharing.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OpenedSubjectService {
    private final OpenedSubjectRepository openedSubjectRepository;

    public OpenedSubjectService(final OpenedSubjectRepository openedSubjectRepository){
        this.openedSubjectRepository = openedSubjectRepository;
    }

    /**
     * 캠퍼스별 해당 학기 전체 과목 저장
     *
     * @param openedSubject
     * @return DefaultRes
     */
    public DefaultRes saveAllSemesterSubject (final OpenedSubject openedSubject) {
        try {
            openedSubjectRepository.save(openedSubject);
            return DefaultRes.res(StatusCode.CREATED, "캠퍼스별 해당 학기 전체 과목 저장 성공");
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "캠퍼스별 해당 학기 전체 과목 저장 실패");
        }
    }

    /**
     * 키워드를 포함하는 모든 과목 정보 조회
     *
     * @param campus
     * @param keyword
     *
     * @return DefaultRes
     */
    public DefaultRes findAllSubjectByCampusAndKeyword (final String campus, final String keyword, final String year, final String semester) {
        try {


            List<SemesterSubject> semesterSubjectList =
                    openedSubjectRepository.findByCampus(campus).get().getSemesterSubjectList();

            List<String> searchedResultList = new ArrayList<>();

            for(int i = 0; i < semesterSubjectList.size(); i++){

                if(!semesterSubjectList.get(i).getYear().equals(year) ||
                        !semesterSubjectList.get(i).getSemester().equals(semester)){
                    continue;
                }
                else{
                    List<Subject> subjectList = semesterSubjectList.get(i).getSubjectList();
                    for(int j = 0; j < subjectList.size(); j++){

                        Subject subject = subjectList.get(j);

                        final String code = subject.getCode();
                        final String name = subject.getName();
                        final String professor = subject.getProfessor();

                        if((code.contains(keyword) || keyword.contains(code)) && !code.equals("")){
                            searchedResultList.add(name + " / " + professor);
                        }
                        else if((name.contains(keyword) || keyword.contains(name)) && !name.equals("")){
                            searchedResultList.add(name + " / " + professor);
                        }
                        else if((professor.contains(keyword) || keyword.contains(professor))&& !professor.equals("")){
                            searchedResultList.add(name + " / " + professor);
                        }
                    }
                }
            }

            // Stream 을 이용한 중복 제거
            searchedResultList = searchedResultList.parallelStream().distinct().collect(Collectors.toList());


            return DefaultRes.res(StatusCode.OK, "키워드를 포함하는 모든 과목 정보 조회 성공", searchedResultList);
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "키워드를 포함하는 모든 과목 정보 조회 실패");
        }
    }

    /**
     * 캠퍼스별 해당 학기 전체 과목 저장
     *
     * @param subjectTest
     * @return DefaultRes
     */
    public DefaultRes save (final SubjectTest subjectTest) {
        try {

            OpenedSubject openedSubject = new OpenedSubject();
            openedSubject.setCampus("서강대학교");


            SemesterSubject semesterSubject = new SemesterSubject();
            semesterSubject.setSemester("1");
            semesterSubject.setYear("2020");

            List<Subject> subjectList = new ArrayList<>();

            for(int i = 0; i < subjectTest.getProfessor().size(); i++){
                Subject subject = Subject.builder()
                        .department(subjectTest.getDepartment().get(i))
                        .gradePoint(subjectTest.getGradePoint().get(i))
                        .code(subjectTest.getCode().get(i))
                        .professor(subjectTest.getProfessor().get(i))
                        .name(subjectTest.getName().get(i))
                        .time(subjectTest.getTime().get(i))
                        .build();
                subjectList.add(subject);
            }

            semesterSubject.setSubjectList(subjectList);

            List<SemesterSubject> semesterSubjectList = new ArrayList<>();
            semesterSubjectList.add(semesterSubject);
            openedSubject.setSemesterSubjectList(semesterSubjectList);

            openedSubjectRepository.save(openedSubject);

            return DefaultRes.res(StatusCode.CREATED, "캠퍼스별 해당 학기 전체 과목 저장 성공");
        } catch (Exception e) {
            System.out.println(e);
            return DefaultRes.res(StatusCode.DB_ERROR, "캠퍼스별 해당 학기 전체 과목 저장 실패");
        }
    }
}
