package com.workout.api.repository;

import com.workout.api.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByStoredFilename(String storedFilename);

    List<Image> findByPostId(Long postId);

    List<Image> findByUserId(Long userId);
}
