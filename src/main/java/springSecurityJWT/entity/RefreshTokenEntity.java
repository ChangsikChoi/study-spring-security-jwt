package springSecurityJWT.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "refresh_token")
public class RefreshTokenEntity {

    @Id
    private String userName;

    @Column(nullable = false, length = 512)
    private String refreshToken;

    private LocalDateTime expiryDate;
}
