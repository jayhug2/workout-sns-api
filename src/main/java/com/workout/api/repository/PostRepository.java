package com.workout.api.repository;

import com.workout.api.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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


}
