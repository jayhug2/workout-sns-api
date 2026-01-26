package com.workout.api.controller;

import com.workout.api.dto.NotificationResponse;
import com.workout.api.service.NotificationService;
import com.workout.api.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "notification", description = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "내 알림 목록 조회")
    public ResponseEntity<Page<NotificationResponse>> getMyNotifications(Pageable pageable) {
        Long userId = SecurityUtil.getCurrentUserId();
        Page<NotificationResponse> notifications = notificationService.getMyNotification(userId, pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread-count")
    @Operation(summary = "읽지 않은 알림 개수")
    public ResponseEntity<Long> getUnreadCount() {
        Long userId = SecurityUtil.getCurrentUserId();
        Long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "알림 읽음 처리")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        notificationService.markAsRead(id, userId);
        return ResponseEntity.noContent().build();
    }

}
