package springSecurityJWT.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

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
