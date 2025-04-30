# Spring Security + JWT 인증 시스템 데모
* Access Token + Refresh Token 구조
* Refresh Token 로테이션 적용
* CSRF 방어 및 Role 기반 인가 기능 포함

## 기술 스택

* Java 17
* Spring Boot 3.4.4
* Spring Security 6
* JJWT 0.12.6
* Spring Web, Validation, JPA
* Lombok

## 주요 기능

#### 인증(Authentication)

* 회원가입 / 로그인 API
* JWT 발급 (Access + Refresh)
* CSRF 방어용 UUID 토큰 발급 및 검증
* Access Token 만료 시 Refresh Token으로 재발급
* Access Token 재발급 시 Refresh Token 로테이션 적용
* 로그아웃 시 Refresh Token 무효화 + 쿠키 삭제

#### 인가(Authorization)
* 사용자 인증 기반 접근 제어
* @PreAuthorize 기반 권한 검증
* ADMIN Role 전용 사용자 생성 API

## 디렉토리 구조
```
springSecurityJWT
├ config            # 보안 설정 (CORS, 필터체인, PasswordEncoder)
├ controller        # 인증, 유저 API
├ dto               # 요청/응답 DTO
├ entity            # User, RefreshToken 등 JPA 엔티티
├ exception         # 그룹 예외 처리
├ repository        # 데이터 접근 계층
├ security          # JWT 필터, 유틸, CustomUserDetails
└ service           # 인증 및 사용자 비즈니스 로직 
```


## 보안 항목별 설명

| 항목            | 설명                                                                         |
|---------------|----------------------------------------------------------------------------|
| Access Token  | 요청 시 `Authorization: Bearer` 헤더에 포함                                        |
| Refresh Token | `HttpOnly + Secure + SameSite=None` 쿠키로 관리                                 |
| CSRF 토큰       | UUID 생성 후 쿠키 + 바디에 전달 → 재발급 시 검증                                           |
| 토큰 로테이션       | 유효한 RefreshToken 사용 시 → AccessToken + **새 RefreshToken 발급**, 기존 리프레시토큰은 폐기 |
| 인증 정보 저장      | SecurityContextHolder에 CustomUserDetails 저장                                |
| 로그아웃          | Refresh Token DB 삭제 + 쿠키 만료 응답                                             |
| 보안 키 관리       | JWT 서명용 비밀키는 `.env` 파일에서 로딩하여 `application.yml`에서 주입                       |

## 실행 방법

**1. 데이터베이스(mysql) 실행**
```bash
  docker-compose up -d
```
**2. 빌드 & 실행**
```bash
  ./gradlew clean build
  ./gradlew bootRun
```
**3. JWT 비밀키 생성 (필요시)**

[키 생성 클래스](springSecurityJWT/security/SecretKeyGenerator.java)의 main() 메서드 실행하여 생성
```java
SecretKey key = Jwts.SIG.HS512.key().build();
String encodedSecretKey = Encoders.BASE64URL.encode(key.getEncoded());
```

## 인증 흐름 요약
```text
[로그인] POST /api/auth/login 
  -> Access Token + Refresh Token 발급
  CSRF 토큰 UUID 전달 (Cookie + Body)

[인증 요청] GET /api/users/who-am-i: 유저 본인 조회
  -> Authorization 헤더 이용하여 인증 -> SecurityContext에 인증 주입

[토큰 재발급] POST /api/auth/refresh
  -> 쿠키에 저장된 RefreshToken + CSRF 헤더 확인
  유효성 검증 후, 기존 RefreshToken 폐기 후 새로 발급

[로그아웃] POST /api/auth/logout
  -> Refresh Token DB에서 삭제, 쿠키 삭제
```

## 테스트 방법

* Postman 활용
  * `Samples.postman_collection.json` 임포트하여 테스트
  * 본인조회, 유저생성, 로그아웃 요청시 `Authorization`탭의 Auth Type을 `Bearer Token`으로 설정 후 AccessToken 설정
* 기본 유저
  * ADMIN 권한 사용자
    * ID: admin
    * PW: 1234
  * USER 권한 사용자
    * ID: test
    * PW: 1234

## 쿠키 / CORS 정책
* `Access-Control-Allow-Origin`: 개발용 도메인 지정
* `allowCredentials`: true
* RefreshToken 쿠키 설정:
  * `HttpOnly: true`, `Secure: true`, `SameSite: None`, `Path: /api/auth/refresh`
* CSRF Token 쿠키 설정:
  * `HttpOnly: false`, `Secure: true`, `SameSite: None`, `Path: /`
