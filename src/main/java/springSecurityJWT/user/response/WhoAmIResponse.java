package springSecurityJWT.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WhoAmIResponse {
    private String userName;
    private String userNickName;
    private String userRole;
}
