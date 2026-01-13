package com.workout.api.controller;

import com.workout.api.dto.PostResponse;
import com.workout.api.dto.UserResponse;
import com.workout.api.service.PostService;
import com.workout.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "검색 API")
public class SearchController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping("/posts/title")
    @Operation(summary = "게시글 제목 검색")
    public ResponseEntity<Page<PostResponse>> searchPostsByTitle(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        Page<PostResponse> posts = postService.searchByTitle(keyword, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/content")
    @Operation(summary = "게시글 내용 검색")
    public ResponseEntity<Page<PostResponse>> searchPostsByContent(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        Page<PostResponse> posts = postService.searchByContent(keyword, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts")
    @Operation(summary = "게시글 통합 검색 (제목 + 내용)")
    public ResponseEntity<Page<PostResponse>> searchPosts(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        Page<PostResponse> posts = postService.searchByTitleOrContent(keyword, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/users")
    @Operation(summary = "사용자 닉네임 검색")
    public ResponseEntity<Page<UserResponse>> searchUsers(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        Page<UserResponse> users = userService.searchByNickname(keyword, pageable);
        return ResponseEntity.ok(users);
    }
}
