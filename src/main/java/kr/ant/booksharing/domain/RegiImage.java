package kr.ant.booksharing.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "regi_image")
public class RegiImage {
    private int id;

    @Id
    private String imageUrl;
}
