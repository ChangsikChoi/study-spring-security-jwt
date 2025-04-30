package springSecurityJWT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springSecurityJWT.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUserName(String userName);

    boolean existsByUserName(String userName);
}
