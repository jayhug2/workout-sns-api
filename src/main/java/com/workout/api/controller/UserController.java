package com.workout.api.controller;

import com.workout.api.dto.UserProfileResponse;
import com.workout.api.dto.UserResponse;
import com.workout.api.service.UserService;
import com.workout.api.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "현재 로그인한 사용자 정보 조회")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(userService.getUserById(userId));
    }


    @GetMapping("/{userId}/profile")
    @Operation(summary = "사용자 프로필 조회")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Long userId) {
        UserProfileResponse profile = userService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }
}