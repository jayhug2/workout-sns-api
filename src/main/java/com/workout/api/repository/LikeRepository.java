package com.workout.api.repository;

import com.workout.api.entity.Like;
import com.workout.api.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    Long countByPostId(Long postId);

    Page<Like> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);

    List<Like> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    Long post(Post post);
}
