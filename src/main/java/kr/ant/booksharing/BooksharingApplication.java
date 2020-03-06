package kr.ant.booksharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BooksharingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksharingApplication.class, args);
    }

}
