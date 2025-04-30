package springSecurityJWT.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springSecurityJWT.config.JWTConfig;
import springSecurityJWT.dto.UserRole;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JWTUtil {
    private final JWTConfig jwtConfig;

    public String generateAccessToken(String username, UserRole role, String nickname, boolean isEnable) {
        return Jwts.builder()
                .subject(username)
                //커스텀 필드 설정
                .claim("role", role)
                .claim("nickname", nickname)
                .claim("enabled", isEnable)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getAccessTokenExpiration()))
                .signWith(jwtConfig.getSecretKey(), Jwts.SIG.HS512)
                .compact();
    }

    public String generateRefreshToken(String username, UserRole role, String nickname, boolean isEnable) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .claim("nickname", nickname)
                .claim("enabled", isEnable)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration()))
                .signWith(jwtConfig.getSecretKey(), Jwts.SIG.HS512)
                .compact();
    }

    public CustomUserDetails getUserDetailsFromToken(String token) {
        Claims payload = Jwts.parser()
                    .verifyWith(jwtConfig.getSecretKey())
                    .build()
                    .parseSignedClaims(token).getPayload();

        String subject = payload.getSubject();
        String role = payload.get("role", String.class);
        String nickname = payload.get("nickname", String.class);
        boolean isEnable = payload.get("enabled", Boolean.class);

        return new CustomUserDetails(subject, nickname, role, isEnable);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(jwtConfig.getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
