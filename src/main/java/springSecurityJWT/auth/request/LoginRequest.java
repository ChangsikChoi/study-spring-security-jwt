package springSecurityJWT.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank
    private String id;

    @NotBlank
    private String password;
}
