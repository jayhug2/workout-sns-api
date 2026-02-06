package com.workout.api.controller;

import com.workout.api.config.CookieProperties;
import com.workout.api.dto.*;
import com.workout.api.entity.User;
import com.workout.api.redis.RefreshToken;
import com.workout.api.repository.UserRepository;
import com.workout.api.service.RefreshTokenService;
import com.workout.api.service.UserService;
import com.workout.api.util.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieProperties cookieProperties;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserSignupRequest request) {
        User user = userService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getNickname()
        );

        UserResponse response = UserResponse.from(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/signin")
    @Operation(summary = "로그인")
    public ResponseEntity<LoginResponse> signin(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        LoginResult result = userService.login(request.getEmail(), request.getPassword());

        // Refresh Token 생성 및 HttpOnly Cookie 설정
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(result.getUser().getId());

        // ResponseCookie 사용 (SameSite 포함)
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .path("/")
                .maxAge(7 * 24 * 60 * 60)  // 7일
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        // Access Token만 Response Body에 반환
        LoginResponse loginResponse = new LoginResponse(
                result.getUser().getId(),
                result.getUser().getEmail(),
                result.getUser().getNickname(),
                "로그인 성공",
                result.getToken()  // Access Token
        );

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<Void> signout(
            HttpServletRequest request,
            HttpServletResponse response) {

        // Cookie에서 Refresh Token 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String refreshToken = Arrays.stream(cookies)
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);

            if (refreshToken != null) {
                // Redis에서 Refresh Token 삭제
                refreshTokenService.findByToken(refreshToken)
                        .ifPresent(token -> refreshTokenService.deleteByUserId(token.getUserId()));
            }
        }

        // Cookie 삭제 (SameSite 포함)
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(cookieProperties.isSecure())
                .path("/")
                .maxAge(0)  // 즉시 만료
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "Access Token 갱신")
    public ResponseEntity<TokenRefreshResponse> refresh(HttpServletRequest request) {
        // Cookie에서 Refresh Token 추출
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new RuntimeException("Refresh Token이 없습니다.");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Refresh Token이 없습니다."));

        // Refresh Token 검증 및 조회
        RefreshToken token = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 Refresh Token입니다."));

        refreshTokenService.verifyExpiration(token);

        // User 조회
        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 새 Access Token 발급
        String newAccessToken = jwtTokenProvider.generateToken(user.getEmail(), user.getId());

        TokenRefreshResponse response = new TokenRefreshResponse(newAccessToken);
        return ResponseEntity.ok(response);
    }
}