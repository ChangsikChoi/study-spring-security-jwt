package springSecurityJWT.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@Getter
public class JWTConfig {
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final SecretKey secretKey;

    public JWTConfig(
            @Value("${jwt.expiration.access}") long accessTokenExpiration,
            @Value("${jwt.expiration.refresh}") long refreshTokenExpiration,
            @Value("${jwt.secret}") String secretKey) {
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        //비밀키 생성기에서 인코딩하여 문자열 값 생성하기 때문에 디코딩한 키 전달.
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }
}
