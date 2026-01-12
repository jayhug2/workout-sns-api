package com.workout.api.service;


import com.workout.api.entity.Follow;
import com.workout.api.entity.User;
import com.workout.api.repository.FollowRepository;
import com.workout.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean toggleFollow(Long followerId, Long followingId) {
        if (followingId.equals(followerId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우 할 수 없습니다.");
        }

        User followingUser = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        Optional<Follow> existingFollow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);

        if (existingFollow.isPresent()) {
            followRepository.delete(existingFollow.get());
            return false;
        } else {
            User followerUser = userRepository.findById(followerId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            Follow follow = Follow.builder()
                    .follower(followerUser)
                    .following(followingUser)
                    .build();

            followRepository.save(follow);
            return true;
        }
    }

    public Page<Follow> getFollowers(Long userId, Pageable pageable) {
        return followRepository.findByFollowingIdOrderByCreatedAtDesc(userId, pageable);
    }

    public Page<Follow> getFollowing(Long userId, Pageable pageable) {
        return followRepository.findByFollowerIdOrderByCreatedAtDesc(userId, pageable);
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    public Long getFollowerCount(Long userId) {
        return followRepository.countByFollowingId(userId);
    }

    public Long getFollowingCount(Long userId) {
        return followRepository.countByFollowerId(userId);
    }
}
