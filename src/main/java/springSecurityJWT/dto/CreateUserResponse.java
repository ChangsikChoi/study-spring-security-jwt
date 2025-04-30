package springSecurityJWT.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUserResponse {
    private String username;
    private String password;
    private String userNickname;
    private UserRole userRole;
}
