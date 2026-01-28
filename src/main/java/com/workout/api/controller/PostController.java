package com.workout.api.controller;

import com.workout.api.dto.PostRequest;
import com.workout.api.dto.PostResponse;
import com.workout.api.service.PostService;
import com.workout.api.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 API")
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "게시글 생성")
    public ResponseEntity<PostResponse> create(
            @Valid @RequestBody PostRequest request
            ) {
        Long userId = SecurityUtil.getCurrentUserId();
        PostResponse response = postService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시글 조회")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        PostResponse response = postService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "게시글 목록 조회")
    public ResponseEntity<Page<PostResponse>> findAll(Pageable pageable) {
        Page<PostResponse> responses = postService.findAll(pageable);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정")
    public ResponseEntity<PostResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest request
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        PostResponse response = postService.update(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        postService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    @Operation(summary = "인기 게시글 조회")
    public ResponseEntity<Page<PostResponse>> getPopularPosts(Pageable pageable) {
        Page<PostResponse> posts = postService.getPopularPosts(pageable);
        return ResponseEntity.ok(posts);
    }
}
