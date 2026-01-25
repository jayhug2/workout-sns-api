package com.workout.api.listener;


import com.workout.api.entity.*;
import com.workout.api.event.CommentCreatedEvent;
import com.workout.api.event.FollowCreatedEvent;
import com.workout.api.event.LikeCreatedEvent;
import com.workout.api.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;

    @Async
    @EventListener
    public void handleLikeCreated(LikeCreatedEvent event) {
        Like like = event.getLike();
        Post post = like.getPost();
        User sender = like.getUser();
        User receiver = post.getUser();

        if (sender.getId().equals(receiver.getId())) {
            return;
        }

        String message = sender.getNickname() + "님이 회원님의 게시글을 좋아합니다.";

        Notification notification = Notification.builder()
                .type(NotificationType.LIKE)
                .message(message)
                .receiver(receiver)
                .sender(sender)
                .relatedId(post.getId())
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    @Async
    @EventListener
    public void handleCommentCreated(CommentCreatedEvent event) {
        Comment comment = event.getComment();
        Post post = comment.getPost();
        User sender = comment.getUser();
        User receiver = post.getUser();

        if (sender.getId().equals(receiver.getId())) {
            return;
        }

        String message = sender.getNickname() + "님이 댓글을 남겼습니다: " + comment.getContent();

        Notification notification = Notification.builder()
                .type(NotificationType.COMMENT)
                .message(message)
                .receiver(receiver)
                .sender(sender)
                .relatedId(post.getId())
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    @Async
    @EventListener
    public void handleFollowCreated(FollowCreatedEvent event) {
        Follow follow = event.getFollow();
        User sender = follow.getFollower();
        User receiver = follow.getFollowing();

        String message = sender.getNickname() + "님이 회원님을 팔로우합니다.";

        Notification notification = Notification.builder()
                .type(NotificationType.FOLLOW)
                .message(message)
                .receiver(receiver)
                .sender(sender)
                .relatedId(null)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }
}
