package com.sansantek.sansanmulmul.user.service;

import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.badge.Badge;
import com.sansantek.sansanmulmul.user.domain.badge.UserBadge;
import com.sansantek.sansanmulmul.user.repository.BadgeRepository;
import com.sansantek.sansanmulmul.user.repository.UserBadgeRepository;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeService {

    private final UserBadgeRepository userBadgeRepository;
    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;

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

        // userId에 해당하는 UserBadge 목록을 조회
        List<UserBadge> userBadges = userBadgeRepository.findByUser_UserId(userId);

        // 각 UserBadge의 Badge 이름을 badgeList에 추가
        for (UserBadge userBadge : userBadges)
            badgeList.add(userBadge.getBadge().getBadgeName());

        return badgeList;
    }
}
