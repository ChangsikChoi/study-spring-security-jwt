package springSecurityJWT.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springSecurityJWT.dto.SignUpRequest;
import springSecurityJWT.dto.CreateUserRequest;
import springSecurityJWT.dto.CreateUserResponse;
import springSecurityJWT.entity.UserEntity;
import springSecurityJWT.repository.UserRepository;
import springSecurityJWT.dto.UserRole;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest request) {
        if (existsByUserName(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        UserEntity user = UserEntity.builder()
                .userName(request.getUsername())
                .userPassword(passwordEncoder.encode(request.getPassword()))
                .userNickname(request.getUserNickname())
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }

    public CreateUserResponse createUser(CreateUserRequest request) {
        if (existsByUserName(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        UserEntity user = UserEntity.builder()
                .userName(request.getUsername())
                .userPassword(passwordEncoder.encode(request.getPassword()))
                .userNickname(request.getUserNickname())
                .role(request.getUserRole())
                .build();

        UserEntity saved = userRepository.save(user);

        return new CreateUserResponse(saved.getUserName(), request.getPassword(), saved.getUserNickname(), saved.getRole());
    }

    public boolean existsByUserName(String username) {
        return userRepository.existsByUserName(username);
    }

    public Optional<UserEntity> findByUserName(String username) {
        return userRepository.findByUserName(username);
    }
}
