package com.workout.api.dto;

import com.workout.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final LocalDateTime createdAt;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getCreatedAt()
        );
    }
}
