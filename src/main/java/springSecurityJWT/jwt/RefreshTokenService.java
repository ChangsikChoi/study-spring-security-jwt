package springSecurityJWT.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springSecurityJWT.user.CustomUserDetails;
import springSecurityJWT.user.UserRole;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTConfig jwtConfig;
    private final JWTUtil jwtUtil;

    public void save(String username, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .userName(username)
                .refreshToken(refreshToken)
                .expiryDate(LocalDateTime.now().plus(jwtConfig.getRefreshTokenExpiration(), ChronoUnit.MILLIS))
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public String rotateRefreshToken(CustomUserDetails userDetails, String prevToken) {
        String username = userDetails.getUsername();

        //검증
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("not exist available token"));

        boolean isExpired = LocalDateTime.now().isAfter(refreshTokenEntity.getExpiryDate());
        if(isExpired) {
            throw new RuntimeException("token is expired");
        }
        if (!prevToken.equals(refreshTokenEntity.getRefreshToken())) {
            throw new RuntimeException("token mismatched");
        }

        //토큰 로테이션
        refreshTokenRepository.delete(refreshTokenEntity);

        String newRefreshToken = jwtUtil.generateRefreshToken(
                username,
                UserRole.from(userDetails.getRole()),
                userDetails.getUserNickname(),
                userDetails.isEnabled());

        this.save(username, newRefreshToken);

        return newRefreshToken;
    }
}
