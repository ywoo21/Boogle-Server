package kr.ant.booksharing.domain;

import kr.ant.booksharing.model.DepartmentMajor;
import kr.ant.booksharing.model.SemesterSubject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

@Data
@Document("opened_subject")
public class OpenedSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String _id;

    private String campus;
    private List<SemesterSubject> semesterSubjectList;
}
