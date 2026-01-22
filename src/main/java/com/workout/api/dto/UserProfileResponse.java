package com.workout.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserProfileResponse {

    private Long userId;
    private String nickname;
    private Long postCount;
    private Long followerCount;
    private Long followingCount;
    private List<PostResponse> recentPosts;

}
