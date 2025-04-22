package springSecurityJWT.user.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import springSecurityJWT.user.UserRole;

@Getter
@AllArgsConstructor
public class CreateUserResponse {
    private String username;
    private String password;
    private String userNickname;
    private UserRole userRole;
}
