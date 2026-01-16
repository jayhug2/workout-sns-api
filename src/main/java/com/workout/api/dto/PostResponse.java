package com.workout.api.dto;

import com.workout.api.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String userNickName;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponse from(Post post) {

        List<String> imageUrls = post.getImages().stream()
                .map(image -> "/api/images/" + image.getStoredFilename())
                .toList();

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getNickname(),
                imageUrls,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
