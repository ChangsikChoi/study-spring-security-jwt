package springSecurityJWT.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springSecurityJWT.auth.request.SignUpRequest;
import springSecurityJWT.user.request.CreateUserRequest;
import springSecurityJWT.user.response.CreateUserResponse;

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

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByUserName(username)
//                .map(CustomUserDetails::new)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("찾을 수 없는 사용자 : " + username));
//    }
}
