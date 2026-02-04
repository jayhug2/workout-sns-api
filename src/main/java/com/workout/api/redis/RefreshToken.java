package com.workout.api.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@RedisHash(value = "refreshToken", timeToLive = 604800)  // 7일 (초 단위)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    private String token;  // Refresh Token 문자열 (Primary Key)

    @Indexed
    private Long userId;   // User ID (검색용)

    private LocalDateTime expiryDate;  // 만료 시간
}