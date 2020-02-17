package kr.ant.booksharing.domain;

import kr.ant.booksharing.model.DepartmentMajor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

@Data
@Document("major")
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String _id;

    private String campus;
    private List<DepartmentMajor> departmentMajorList;
}
