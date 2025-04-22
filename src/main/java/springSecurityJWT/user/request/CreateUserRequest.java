package springSecurityJWT.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import springSecurityJWT.user.UserRole;
import springSecurityJWT.user.ValidUserRole;

@Getter
public class CreateUserRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String userNickname;

    @ValidUserRole
    private UserRole userRole;
}
