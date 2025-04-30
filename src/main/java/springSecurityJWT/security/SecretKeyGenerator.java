package springSecurityJWT.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;

/**
 * JWT 서명용 비밀키 생성 클래스
 */
@Slf4j
public class SecretKeyGenerator {
    public static void main(String[] args) {
        SecretKey key = Jwts.SIG.HS512.key().build();
        String encodedSecretKey = Encoders.BASE64URL.encode(key.getEncoded());

        System.out.println("\n========================================");
        System.out.println("\nencodedSecretKey = " + encodedSecretKey + "\n");
        log.info("jjwt에서 제공하는 키 생성함수로 생성된 비밀키입니다.");
        log.info("BASE64URL 인코딩처리된 값 입니다.");
        System.out.println("=========================================");

    }
}
