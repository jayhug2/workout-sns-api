package com.workout.api.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {

    }

    /**
     * 현재 인증된 사용자의 ID를 반환
     * @return 사용자 ID
     * @throws IllegalStateException 인증 정보가 업석나 유효하지 않은 경우
    */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof Long)) {
            throw new IllegalStateException("올바르지 않은 인증 정보입니다.");
        }

        return (Long) principal;
    }
}
