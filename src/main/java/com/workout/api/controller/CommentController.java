package com.workout.api.controller;

import com.workout.api.dto.CommentRequest;
import com.workout.api.dto.CommentResponse;
import com.workout.api.entity.Comment;
import com.workout.api.service.CommentService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 API")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    @Operation(summary = "댓글 작성")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request
    ) {
        Long userId = SecurityUtil.getCurrentUserId();

        Comment comment = commentService.createComment(
                postId,
                userId,
                request.getContent()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommentResponse.from(comment));
    }

    @GetMapping("/post/{postId}/comments")
    @Operation(summary = "댓글 목록 조회")
    public ResponseEntity<Page<CommentResponse>> getComments(
            @PathVariable Long postId,
            Pageable pageable) {
        Page<Comment> comments = commentService.getCommentsByPost(postId, pageable);

//        List<CommentResponse> responses = comments.stream()
//                .map(CommentResponse::from)
//                .collect(Collectors.toList());
        Page<CommentResponse> responses = comments.map(CommentResponse::from);

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/comments/{commentId}")
    @Operation(summary = "댓글 수정")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request
    ) {
        Long userId = SecurityUtil.getCurrentUserId();

        Comment comment = commentService.updateComment(
                commentId,
                userId,
                request.getContent()
        );

        return ResponseEntity.ok(CommentResponse.from(comment));
    }


    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<Void> deleteComment(
        @PathVariable Long commentId
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        commentService.deleteComment(commentId, userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts/{postId}/comments/count")
    @Operation(summary = "댓글 개수 조회")
    public ResponseEntity<Long> getCommentCount(
        @PathVariable Long postId
    ) {
        Long count = commentService.getCommentCount(postId);

        return ResponseEntity.ok(count);
    }

}
