package com.workout.api.dto;

import com.workout.api.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LikeResponse {

    private Long id;
    private Long postId;
    private Long userId;
    private String userNickname;
    private LocalDateTime createdAt;

    public static LikeResponse from(Like like) {
        return new LikeResponse(
                like.getId(),
                like.getPost().getId(),
                like.getUser().getId(),
                like.getUser().getNickname(),
                like.getCreatedAt()
        );
    }
}
