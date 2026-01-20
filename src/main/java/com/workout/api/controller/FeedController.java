package com.workout.api.controller;

import com.workout.api.dto.PostResponse;
import com.workout.api.service.PostService;
import com.workout.api.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
@Tag(name = "Feed", description = "피드 API")
public class FeedController {

    private final PostService postService;

    @GetMapping
    @Operation(summary = "피드 조회 (팔로잉 게시글)")
    public ResponseEntity<Page<PostResponse>> getFeed(Pageable pageable) {
        Long userId = SecurityUtil.getCurrentUserId();
        Page<PostResponse> feed = postService.getFeed(userId, pageable);
        return ResponseEntity.ok(feed);
    }
}
