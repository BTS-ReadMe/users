package bts.oauth2.repository;

import bts.oauth2.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmailOrderByIdAsc(String email);
}
