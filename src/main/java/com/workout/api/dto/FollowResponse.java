package com.workout.api.dto;

import com.workout.api.entity.Follow;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FollowResponse {

    private Long id;
    private Long followerId;
    private String followerNickname;
    private Long followingId;
    private String followingNickname;
    private LocalDateTime createdAt;

    public static FollowResponse from(Follow follow) {
        return new FollowResponse(
                follow.getId(),
                follow.getFollower().getId(),
                follow.getFollower().getNickname(),
                follow.getFollowing().getId(),
                follow.getFollowing().getNickname(),
                follow.getCreatedAt()
        );
    }
}