package springSecurityJWT.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springSecurityJWT.auth.request.LoginRequest;
import springSecurityJWT.auth.response.AuthSuccessResponseDto;
import springSecurityJWT.jwt.JWTUtil;
import springSecurityJWT.jwt.RefreshTokenService;
import springSecurityJWT.user.CustomUserDetails;
import springSecurityJWT.user.UserEntity;
import springSecurityJWT.user.UserRole;
import springSecurityJWT.user.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthSuccessResponseDto login(LoginRequest request) {
        Optional<UserEntity> userOptional = userService.findByUserName(request.getId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("없는 사용자 입니다.");
        }
        UserEntity user = userOptional.get();

        boolean isMatchedPassword = passwordEncoder.matches(request.getPassword(), user.getUserPassword());
        if (!isMatchedPassword) {
            throw new IllegalArgumentException("비밀번호를 확인해주세요.");
        }
        String accessToken = jwtUtil.generateAccessToken(user.getUserName(), user.getRole(), user.getUserNickname(), user.isEnabled());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserName(), user.getRole(), user.getUserNickname(), user.isEnabled());

        refreshTokenService.save(user.getUserName(), refreshToken);

        return new AuthSuccessResponseDto(accessToken, refreshToken);
    }

    public AuthSuccessResponseDto refresh(String prevToken) {
        CustomUserDetails userDetails = jwtUtil.getUserDetailsFromToken(prevToken);
        String newRefreshToken = refreshTokenService.rotateRefreshToken(userDetails, prevToken);
        String accessToken = jwtUtil.generateAccessToken(
                userDetails.getUsername(),
                UserRole.from(userDetails.getRole()),
                userDetails.getUserNickname(),
                userDetails.isEnabled());
        return new AuthSuccessResponseDto(accessToken, newRefreshToken);
    }
}
