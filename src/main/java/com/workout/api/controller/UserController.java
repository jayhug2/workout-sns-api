package com.workout.api.controller;

import com.workout.api.dto.UserProfileResponse;
import com.workout.api.service.UserService;
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

    @GetMapping("/{userId}/profile")
    @Operation(summary = "사용자 프로필 조회")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Long userId) {
        UserProfileResponse profile = userService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }
}