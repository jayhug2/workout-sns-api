package com.workout.api.controller;

import com.workout.api.dto.LikeResponse;
import com.workout.api.entity.Like;
import com.workout.api.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Like", description = "좋아요 API")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{postId}/likes")
    @Operation(summary = "좋아요 토글")
    public ResponseEntity<Void> toggleLike(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        likeService.toggleLike(postId, userId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{postId}/likes/count")
    @Operation(summary = "좋아요 개수 조회")
    public ResponseEntity<Long> getLikeCount(
            @PathVariable Long postId
    ) {
        Long count = likeService.getLikeCount(postId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{postId}/likes/me")
    @Operation(summary = "좋아요 여부 확인")
    public ResponseEntity<Boolean> isLiked(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        boolean isLiked = likeService.isLiked(postId, userId);

        return ResponseEntity.ok(isLiked);
    }

    @GetMapping("/{postId}/likes")
    @Operation(summary = "좋아요 누른 사용자 목록")
    public ResponseEntity<List<LikeResponse>> getLikeUsers(
            @PathVariable Long postId
    ) {
        List<Like> likes = likeService.getLikeUsers(postId);

        List<LikeResponse> responses = likes.stream()
                .map(LikeResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}
