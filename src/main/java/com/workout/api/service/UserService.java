package com.workout.api.service;

import com.workout.api.dto.LoginResult;
import com.workout.api.dto.PostResponse;
import com.workout.api.dto.UserProfileResponse;
import com.workout.api.dto.UserResponse;
import com.workout.api.entity.Post;
import com.workout.api.entity.User;
import com.workout.api.repository.FollowRepository;
import com.workout.api.repository.PostRepository;
import com.workout.api.repository.UserRepository;
import com.workout.api.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;

    @Transactional
    public User createUser(String email, String password, String nickname) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    public LoginResult login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getId());

        return new LoginResult(user, token);
    }

    public Page<UserResponse> searchByNickname(String keyword, Pageable pageable) {
        Page<User> users = userRepository.findByNicknameContainingIgnoreCase(keyword, pageable);
        return users.map(UserResponse::from);
    }

    public UserProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Long postCount = postRepository.countByUserId(userId);

        Long followerCount = followRepository.countByFollowingId(userId);

        Long followingCount = followRepository.countByFollowerId(userId);

        Pageable pageable = PageRequest.of(0, 3);
        Page<Post> recentPostsPage = postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        List<PostResponse> recentPostResponses = recentPostsPage.getContent().stream()
                .map(PostResponse::from)
                .toList();

        return new UserProfileResponse(
                user.getId(),
                user.getNickname(),
                postCount,
                followerCount,
                followingCount,
                recentPostResponses
        );
    }

}
