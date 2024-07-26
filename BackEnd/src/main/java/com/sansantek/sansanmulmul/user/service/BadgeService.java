package com.sansantek.sansanmulmul.user.service;

import com.sansantek.sansanmulmul.user.domain.badge.UserBadge;
import com.sansantek.sansanmulmul.user.repository.UserBadgeRepository;
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
