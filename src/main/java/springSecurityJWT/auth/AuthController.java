package springSecurityJWT.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springSecurityJWT.auth.request.LoginRequest;
import springSecurityJWT.auth.request.SignUpRequest;
import springSecurityJWT.auth.response.AuthSuccessResponseDto;
import springSecurityJWT.auth.response.AuthSuccessResponse;
import springSecurityJWT.jwt.JWTConfig;
import springSecurityJWT.jwt.JWTUtil;
import springSecurityJWT.user.CustomUserDetails;
import springSecurityJWT.user.UserService;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final String CSRF_TOKEN_COOKIE_NAME = "csrf_token";
    private final JWTUtil jwtUtil;
    private final JWTConfig jwtConfig;
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request) {
        try {
            userService.signUp(request);
            return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthSuccessResponse> login(HttpServletResponse response, @Valid @RequestBody LoginRequest request) {
        AuthSuccessResponseDto loginTokens = authService.login(request);
        //리프레시 토큰은 Http Only Cookie로 리턴
        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, loginTokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(jwtConfig.getRefreshTokenExpiration())
                .sameSite("None")
                .build();
        //CSRF 방어용 토큰 쿠키 발급
        String csrfToken = UUID.randomUUID().toString();
        ResponseCookie csrfCookie = ResponseCookie.from(CSRF_TOKEN_COOKIE_NAME, csrfToken)
                .httpOnly(false)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(jwtConfig.getRefreshTokenExpiration())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, csrfCookie.toString());

        return ResponseEntity.ok(new AuthSuccessResponse(loginTokens.getAccessToken(), csrfToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            HttpServletResponse response,
            @CookieValue(REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
            @CookieValue(CSRF_TOKEN_COOKIE_NAME) String csrfToken,
            @RequestHeader("X-CSRF-Token") String csrfHeader
    ) {
        //CSRF 토큰 값 일치 확인
        if (!csrfToken.equals(csrfHeader)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Invalid CSRF token");
        }
        // 리프레시 토큰 서명 유효 확인
        if (!jwtUtil.validateToken(refreshToken)) {
            // 토큰 폐기 및 로그아웃
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid refresh token");
        }

        AuthSuccessResponseDto successResponse = authService.refresh(refreshToken);
        //리프레시 토큰은 Http Only Cookie로 리턴
        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, successResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(jwtConfig.getRefreshTokenExpiration())
                .sameSite("None")
                .build();
        //CSRF 방어용 토큰 쿠키 발급
        String newCsrfToken = UUID.randomUUID().toString();
        ResponseCookie csrfCookie = ResponseCookie.from(CSRF_TOKEN_COOKIE_NAME, newCsrfToken)
                .httpOnly(false)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(jwtConfig.getRefreshTokenExpiration())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, csrfCookie.toString());

        return ResponseEntity.ok(new AuthSuccessResponse(successResponse.getAccessToken(), newCsrfToken));
    }
}
