package com.workout.api.service;

import com.workout.api.entity.Like;
import com.workout.api.entity.Post;
import com.workout.api.entity.User;
import com.workout.api.repository.LikeRepository;
import com.workout.api.repository.PostRepository;
import com.workout.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId, userId);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return false;
        } else {
            Like like = Like.builder()
                    .post(post)
                    .user(user)
                    .build();
            likeRepository.save(like);
            return true;
        }
    }

    public Long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    public boolean isLiked(Long postId, Long userId) {
        return likeRepository.existsByPostIdAndUserId(postId, userId);
    }

    public Page<Like> getLikeUsers(Long postId, Pageable pageable) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        return likeRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);
    }

}
