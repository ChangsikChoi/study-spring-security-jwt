package springSecurityJWT.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springSecurityJWT.dto.CreateUserRequest;
import springSecurityJWT.dto.CreateUserResponse;
import springSecurityJWT.dto.WhoAmIResponse;
import springSecurityJWT.security.CustomUserDetails;
import springSecurityJWT.security.IsAdminUser;
import springSecurityJWT.service.UserService;

@Controller
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @IsAdminUser
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) throws Exception {
        CreateUserResponse userResponse = userService.createUser(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/who-am-i")
    public ResponseEntity<WhoAmIResponse> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(new WhoAmIResponse(userDetails.getUsername(), userDetails.getUserNickname(), userDetails.getRole()));
    }
}
