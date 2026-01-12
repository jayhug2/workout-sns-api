package com.workout.api.repository;


import com.workout.api.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Page<Follow> findByFollowingIdOrderByCreatedAtDesc(Long followingId, Pageable pageable);

    Page<Follow> findByFollowerIdOrderByCreatedAtDesc(Long followerId, Pageable pageable);

    Long countByFollowingId(Long followingId);

    Long countByFollowerId(Long followerId);
}
