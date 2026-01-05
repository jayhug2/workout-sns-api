package com.workout.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Health", description = "서버 상태 확인 API")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "헬스체크")
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Workout SNS API");
        return response;
    }

    @GetMapping("/test")
    @Operation(summary = "인증 테스트")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "인증 성공!");
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
}
