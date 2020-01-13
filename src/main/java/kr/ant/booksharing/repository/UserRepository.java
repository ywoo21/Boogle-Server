package kr.ant.booksharing.repository;

import kr.ant.booksharing.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
