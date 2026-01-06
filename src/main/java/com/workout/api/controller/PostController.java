package com.workout.api.controller;

import com.workout.api.dto.PostRequest;
import com.workout.api.dto.PostResponse;
import com.workout.api.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 API")
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "게시글 생성")
    public ResponseEntity<PostResponse> create(
            Authentication authentication,
            @Valid @RequestBody PostRequest request
            ) {
        String email = authentication.getName();
        PostResponse response = postService.create(email, request);
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
    public ResponseEntity<List<PostResponse>> findAll() {
        List<PostResponse> responses = postService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정")
    public ResponseEntity<PostResponse> update(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody PostRequest request
    ) {
        String email = authentication.getName();
        PostResponse response = postService.update(id, email, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String email = authentication.getName();
        postService.delete(id, email);
        return ResponseEntity.noContent().build();
    }
}
