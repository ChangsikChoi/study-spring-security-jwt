package springSecurityJWT.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthSuccessResponse {
    private String accessToken;
    private String csrfToken;
}
