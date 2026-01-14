package com.workout.api.service;

import com.workout.api.entity.Image;
import com.workout.api.entity.User;
import com.workout.api.repository.ImageRepository;
import com.workout.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Transactional
    public Image uploadImage(MultipartFile file, Long userId) {

        validateFile(file);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String storedFilename = UUID.randomUUID().toString() + "." + extension;

            Path filePath = uploadPath.resolve(storedFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Image image = Image.builder()
                    .originalFilename(originalFilename)
                    .storedFilename(storedFilename)
                    .filePath(filePath.toString())
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .user(user)
                    .build();

            return imageRepository.save(image);

        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }
    }

    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));
    }

    public Image getImageByFilename(String filename) {
        return imageRepository.findByStoredFilename(filename)
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다"));
    }

    @Transactional
    public void deleteImage(Long imageId, Long userId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));

        if (!image.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("이미지를 삭제할 권한이 없습니다.");
        }

        try {
            Path filePath = Paths.get(image.getFilePath());
            Files.deleteIfExists(filePath);

            imageRepository.delete(image);

        } catch (IOException e) {
            throw new RuntimeException("파일 삭제에 실패했습니다.", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기는 5MB를 초과할 수 없습니다.");
        }

        String filename = file.getOriginalFilename();
        String extension = getFileExtension(filename);

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. (jpg, jpeg, png, gif만 가능)");
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("잘못된 파일명입니다.");
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }



}
