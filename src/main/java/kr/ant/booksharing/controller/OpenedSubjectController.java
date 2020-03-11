package kr.ant.booksharing.controller;

import kr.ant.booksharing.domain.Major;
import kr.ant.booksharing.domain.OpenedSubject;
import kr.ant.booksharing.model.SubjectTest;
import kr.ant.booksharing.service.MajorService;
import kr.ant.booksharing.service.OpenedSubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("subject")
public class OpenedSubjectController {
    private final OpenedSubjectService openedSubjectService;

    public OpenedSubjectController(final OpenedSubjectService openedSubjectService) {
        this.openedSubjectService = openedSubjectService;
    }

    /**
     * 캠퍼스별 해당 학기 과목 정보 저장
     *
     * @param openedSubject 캠퍼스별 해당 학기 과목 정보
     * @return ResponseEntity
     */
    @PostMapping("")
    public ResponseEntity saveAllSemesterMajor(@RequestBody final OpenedSubject openedSubject) {
        try {
            return new ResponseEntity<>(openedSubjectService.saveAllSemesterSubject(openedSubject), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 캠퍼스별 해당 학기 과목 정보 찾기
     *
     * @param campus
     * @param keyword
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity searchCampusMajor(@RequestParam("campus") final String campus,
                                            @RequestParam("keyword") final String keyword,
                                            @RequestParam("year") final String year,
                                            @RequestParam("semester") final String semester) {
        try {
            return new ResponseEntity<>(openedSubjectService.findAllSubjectByCampusAndKeyword(campus,keyword,year,semester), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("save")
    public ResponseEntity save() {
        try {
            SubjectTest subjectTest = new SubjectTest();
            ArrayList<String> strings [] = new ArrayList[6];
            for(int j  = 0; j < 6; j++){
                strings[j] = new ArrayList<>();
            }
            try{
                for(int i = 0; i < 6; i++){
                    File file = new File("C:\\Users\\ywooo\\ywoo-dev-project\\springboot\\boogle-server\\src\\main\\java\\kr\\ant\\booksharing\\utils\\" + i+ ".txt");
                    FileReader filereader = new FileReader(file);
                    BufferedReader bufReader = new BufferedReader(filereader);
                    String line = "";
                    while((line = bufReader.readLine()) != null){
                        strings[i].add(line);
                    }
                    bufReader.close();
                }

            }catch (FileNotFoundException e) {
                // TODO: handle exception
            }catch(IOException e){
                System.out.println(e);
            }

            List<String> codeList = strings[0];

            for(int k = 0; k < codeList.size(); k++){
                String newStr = codeList.get(k).replaceAll(" ", "");
                newStr = newStr.replaceAll("\\p{Z}", "");
                codeList.set(k, newStr);
            }

            subjectTest.setCode(codeList);
            subjectTest.setName(strings[1]);
            subjectTest.setDepartment(strings[2]);
            subjectTest.setGradePoint(strings[3]);

            subjectTest.setTime(strings[4]);
            subjectTest.setProfessor(strings[5]);


            return new ResponseEntity<>(openedSubjectService.save(subjectTest), HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
