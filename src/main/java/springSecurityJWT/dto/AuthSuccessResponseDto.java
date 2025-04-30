package springSecurityJWT.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthSuccessResponseDto {
    private String accessToken;
    private String refreshToken;

}
