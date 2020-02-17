package kr.ant.booksharing.model;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentMajor {
    private String department;
    private List<String> majorList;
}
