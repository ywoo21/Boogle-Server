package kr.ant.booksharing.model;

import lombok.Data;

import java.util.List;

@Data
public class SemesterSubject {
    private String year;
    private String semester;
    private List<Subject> subjectList;
}
