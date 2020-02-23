package kr.ant.booksharing.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

@Data
public class BoogleBox {
    @Id
    String _id;

    String boxPassword;
    String sellItemId;
    int sellerId;
    int buyerId;
    Date registerTime;

    boolean isEmpty;
}
