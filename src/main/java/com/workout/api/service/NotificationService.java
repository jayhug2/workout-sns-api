package com.workout.api.service;


import com.workout.api.dto.NotificationResponse;
import com.workout.api.entity.Notification;
import com.workout.api.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public Page<NotificationResponse> getMyNotification(Long userId, Pageable pageable) {
        Page<Notification> notifications = notificationRepository
                .findByReceiverIdOrderByCreatedAtDesc(userId, pageable);
        return notifications.map(NotificationResponse::from);
    }

    public Long getUnreadCount(Long userId) {
        return notificationRepository.countByReceiverIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));

        if (!notification.getReceiver().getId().equals(userId)) {
            throw new IllegalArgumentException("본인의 알림만 읽음 처리할 수 있습니다.");
        }

        notification.markAsRead();
    }
}
