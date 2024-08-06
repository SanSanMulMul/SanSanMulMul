package com.sansantek.sansanmulmul.user.service.badge;

import com.sansantek.sansanmulmul.exception.auth.UserNotFoundException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.badge.Badge;
import com.sansantek.sansanmulmul.user.domain.badge.UserBadge;
import com.sansantek.sansanmulmul.user.repository.badge.BadgeRepository;
import com.sansantek.sansanmulmul.user.repository.badge.UserBadgeRepository;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeService {

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    // 회원가입 시 기본 칭호 지정
    public void setBasicBadge(int userId) {
        // 기본 등린이 뱃지 확인
        Badge basicBadge = badgeRepository.findByBadgeId(1)
                .orElseThrow(() -> new RuntimeException("기본 칭호를 찾을 수 없습니다."));
        log.debug("basicBadge: {}", basicBadge);

        // 해당 회원 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        log.debug("user: {}", user);

        // userId에 badgeId 1을 연결하기
        // UserBadge 객체 생성
        UserBadge userBadge = UserBadge.builder()
                .user(user)
                .badge(basicBadge)
                .build();

        // User의 userBadges 리스트에 추가
        user.getUserBadges().add(userBadge);

        // User 엔티티를 저장하여 UserBadge 연관 관계 반영
        userRepository.save(user);

    }

    // 해당 회원의 모든 칭호 조회
    public List<String> getBadgeList(int userId) {
        List<String> badgeList = new ArrayList<>();

        // 사용자 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException());

        // userId에 해당하는 userBadge 리스트 조회
        List<UserBadge> userBadges = userBadgeRepository.findByUser_UserId(userId);

        // userBadge 리스트에서 badgeName 추출하여 badgeList에 저장
        for (UserBadge userBadge : userBadges) {
            String badge = userBadge.getBadge().getBadgeImage() + " " + userBadge.getBadge().getBadgeName();
            badgeList.add(badge);
        }

        return badgeList;
    }

    // 해당 회원의 인증 칭호 추가
    public void addBadge(int userId, int badgeId) {
        // 해당 회원 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        log.debug("user: {}", user);

        // 해당 칭호 확인
        Badge badge = badgeRepository.findByBadgeId(badgeId)
                .orElseThrow(() -> new RuntimeException("기본 칭호를 찾을 수 없습니다."));
        log.debug("badge: {}", badge);

        // userId와 badgeId 컬럼 생성하기
        UserBadge userBadge = UserBadge.builder()
                .user(user)
                .badge(badge)
                .build();

        // User의 userBadge리스트에 추가
        user.getUserBadges().add(userBadge);

        // User엔티티를 저장해 userBadges연관 관계 반영
        userRepository.save(user);
    }

    // 해당 회원의 칭호 수정
    public void updateBadList(int userId, int badgeId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 회원의 고정 칭호 정보만 업데이트
        user.setUserStaticBadge(badgeId);

        // 업데이트한 회원 저장
        userRepository.save(user);
    }
}
