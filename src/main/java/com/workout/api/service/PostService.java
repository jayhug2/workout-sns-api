package com.workout.api.service;

import com.workout.api.dto.PostRequest;
import com.workout.api.dto.PostResponse;
import com.workout.api.entity.Image;
import com.workout.api.entity.Post;
import com.workout.api.entity.User;
import com.workout.api.repository.ImageRepository;
import com.workout.api.repository.PostRepository;
import com.workout.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public PostResponse create(Long userId, PostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setUser(user);
        Post savedPost = postRepository.save(post);

        if (request.getImageIds() != null && !request.getImageIds().isEmpty()) {
            List<Image> images = imageRepository.findAllById(request.getImageIds());

            for (Image image : images) {
                if (!image.getUser().getId().equals(userId)) {
                    throw new IllegalArgumentException("다른 사용자의 이미지는 사용할 수 없습니다.");
                }
                if (image.getPost() != null) {
                    throw new IllegalArgumentException("이미 다른 게시글에 연결된 이미지입니다");
                }
                savedPost.addImage(image);
            }
            imageRepository.saveAll(images);
        }

        return PostResponse.from(savedPost);
    }

    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다"));
        return PostResponse.from(post);
    }

    public Page<PostResponse> findAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(PostResponse::from);
    }

    @Transactional
    public PostResponse update(Long id, Long userId, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다"));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("본인의 게시글만 수정할 수 있습니다");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        return PostResponse.from(post);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("본인의 게시글만 삭제할 수 있습니다");
        }

        postRepository.delete(post);
    }

    public Page<PostResponse> searchByTitle(String keyword, Pageable pageable) {
        Page<Post> posts = postRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        return posts.map(PostResponse::from);
    }

    public Page<PostResponse> searchByContent(String keyword, Pageable pageable) {
        Page<Post> posts = postRepository.findByContentContainingIgnoreCase(keyword, pageable);
        return posts.map(PostResponse::from);
    }

    public Page<PostResponse> searchByTitleOrContent(String keyword, Pageable pageable) {
        Page<Post> posts = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
        return posts.map(PostResponse::from);
    }
}
