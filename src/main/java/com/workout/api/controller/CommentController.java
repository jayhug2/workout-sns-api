package com.workout.api.controller;

import com.workout.api.dto.CommentRequest;
import com.workout.api.dto.CommentResponse;
import com.workout.api.entity.Comment;
import com.workout.api.service.CommentService;
import com.workout.api.util.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request,
            @RequestHeader("Authorization") String token
    ) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.substring(7));

        Comment comment = commentService.createComment(
                postId,
                userId,
                request.getContent()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommentResponse.from(comment));

    }
}
