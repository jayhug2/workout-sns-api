package com.workout.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class HealthCheckResponse {

    private String status;
    private String database;
    private String redis;
    private LocalDateTime timestamp;

    public static HealthCheckResponse up(String database, String redis) {
        return new HealthCheckResponse("UP", database, redis, LocalDateTime.now());
    }

    public static HealthCheckResponse down() {
        return new HealthCheckResponse("DOWN", "UNKNOWN", "UNKNOWN", LocalDateTime.now());
    }
}
