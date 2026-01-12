package com.workout.api.controller;


import com.workout.api.dto.FollowResponse;
import com.workout.api.entity.Follow;
import com.workout.api.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Follow", description = "팔로우 API")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{userId}/follow")
    @Operation(summary = "팔로우/언팔로우")
    public ResponseEntity<Void> toggleFollow(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        Long followerId = (Long) authentication.getPrincipal();
        followService.toggleFollow(followerId, userId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{userId}/followers")
    @Operation(summary = "팔로워 목록 조회")
    public ResponseEntity<Page<FollowResponse>> getFollower(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        Page<Follow> followers = followService.getFollowers(userId, pageable);
        Page<FollowResponse> responses = followers.map(FollowResponse::from);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{userId}/followings")
    @Operation(summary = "팔로잉 목록 조회")
    public ResponseEntity<Page<FollowResponse>> getFollowing (
            @PathVariable Long userId,
            Pageable pageable
    ) {
        Page<Follow> followings = followService.getFollowing(userId, pageable);
        Page<FollowResponse> responses = followings.map(FollowResponse::from);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{userId}/follow/me")
    @Operation(summary = "팔로우 여부 확인")
    public ResponseEntity<Boolean> isFollowing(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        Long followerId = (Long) authentication.getPrincipal();
        boolean isFollowing = followService.isFollowing(followerId, userId);

        return ResponseEntity.ok(isFollowing);
    }

    @GetMapping("/{userId}/followers/count")
    @Operation(summary = "팔로워 수 조회")
    public ResponseEntity<Long> getFollowerCount(
            @PathVariable Long userId
    ) {
        Long count = followService.getFollowerCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{userId}/followings/count")
    @Operation(summary = "팔로잉 수 조회")
    public ResponseEntity<Long> getFollowingCount(
            @PathVariable Long userId
    ) {
        Long count = followService.getFollowingCount(userId);
        return ResponseEntity.ok(count);
    }


}
