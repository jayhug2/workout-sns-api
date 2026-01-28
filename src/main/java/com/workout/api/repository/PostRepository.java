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

    @Query(value = """
    SELECT p.id, p.title, p.content, p.user_id, p.created_at, p.updated_at
    FROM posts p
    LEFT JOIN likes l ON l.post_id = p.id
    LEFT JOIN comments c ON c.post_id = p.id
    GROUP BY p.id, p.title, p.content, p.user_id, p.created_at, p.updated_at
    ORDER BY ((COUNT(DISTINCT l.id) * 2) + (COUNT(DISTINCT c.id) * 1) - 
              (TIMESTAMPDIFF(HOUR, p.created_at, CURRENT_TIMESTAMP) / 24.0)) DESC
    """,
            nativeQuery = true)
    Page<Post> findPopularPosts(Pageable pageable);
}
