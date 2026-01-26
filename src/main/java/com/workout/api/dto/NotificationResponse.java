package com.workout.api.dto;


import com.workout.api.entity.Notification;
import com.workout.api.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private NotificationType type;
    private String message;
    private Long senderId;
    private String senderNickname;
    private Long relatedId;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationResponse from (Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getMessage(),
                notification.getSender().getId(),
                notification.getSender().getNickname(),
                notification.getRelatedId(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}
