package kr.ant.booksharing.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Subject {
    private String code;
    private String name;
    private String department;
    private String gradePoint;
    private String time;
    private String professor;
}
