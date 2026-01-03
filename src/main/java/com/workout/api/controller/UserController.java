package com.workout.api.controller;

import com.workout.api.dto.LoginRequest;
import com.workout.api.dto.LoginResponse;
import com.workout.api.dto.UserResponse;
import com.workout.api.dto.UserSignupRequest;
import com.workout.api.entity.User;
import com.workout.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserSignupRequest request) {
        User user = userService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getNickname()
        );

        UserResponse response = new UserResponse(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request.getEmail(), request.getPassword());

        User user = userService.findByEmail(request.getEmail());

        LoginResponse response = new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                "로그인 성공",
                token
        );
        return ResponseEntity.ok(response);
    }
}
