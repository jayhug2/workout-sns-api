package com.workout.api.controller;

import com.workout.api.dto.ImageResponse;
import com.workout.api.entity.Image;
import com.workout.api.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "Image", description = "이미지 API")
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 업로드")
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        Image image = imageService.uploadImage(file, userId);
        ImageResponse response = ImageResponse.from(image);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{filename}")
    @Operation(summary = "이미지 조회")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource resource = imageService.loadImage(filename);
        Image image = imageService.getImageByFilename(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + image.getOriginalFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "이미지 삭제")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        imageService.deleteImage(id, userId);

        return ResponseEntity.noContent().build();
    }
}
