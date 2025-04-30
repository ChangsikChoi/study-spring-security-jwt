package springSecurityJWT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springSecurityJWT.entity.RefreshTokenEntity;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {
}
