package springSecurityJWT.user;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN"),
    MANAGER("MANAGER");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    @JsonCreator
    public static UserRole from(String value) {
        if (value == null) {
            throw new IllegalArgumentException("null일 수 없습니다.");
        }

        return Arrays.stream(values())
                .filter(userRole -> userRole.name().equalsIgnoreCase(value)
                        || userRole.getRole().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 값 입니다." + value));
    }
}
