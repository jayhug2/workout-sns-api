package com.workout.api.repository;

import com.workout.api.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Post> findByContentContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
      String titleKeyword,
      String contentKeyword,
      Pageable pageable
    );

    @Query("SELECT post FROM Post post WHERE post.user.id IN " +
            "(SELECT follow.following.id FROM Follow follow WHERE follow.follower.id = :userId) " +
            "ORDER BY post.createdAt DESC")
    Page<Post> findFeedByUserId(@Param("userId") Long userId, Pageable pageable);

    Long countByUserId(Long userId);

    Page<Post> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
