package kr.ant.booksharing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectTest {
    private List<String> code;
    private List<String> name;
    private List<String> department;
    private List<String> gradePoint;
    private List<String> time;
    private List<String> professor;
}
