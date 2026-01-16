package com.workout.api.dto;

import com.workout.api.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ImageResponse {

    private Long id;
    private String originalFilename;
    private String storedFilename;
    private String imageUrl;
    private Long fileSize;
    private String contentType;
    private Long userId;
    private LocalDateTime createdAt;

    public static ImageResponse from(Image image) {
        return new ImageResponse(
                image.getId(),
                image.getOriginalFilename(),
                image.getStoredFilename(),
                "/api/images/" + image.getStoredFilename(),
                image.getFileSize(),
                image.getContentType(),
                image.getUser().getId(),
                image.getCreatedAt()
        );
    }

}
