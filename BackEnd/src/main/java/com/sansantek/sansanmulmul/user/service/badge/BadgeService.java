package com.sansantek.sansanmulmul.user.service.badge;

import com.sansantek.sansanmulmul.crew.repository.CrewRepository;
import com.sansantek.sansanmulmul.exception.auth.UserNotFoundException;
import com.sansantek.sansanmulmul.record.domain.HikingRecord;
import com.sansantek.sansanmulmul.record.repository.HikingRecordRepository;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.badge.Badge;
import com.sansantek.sansanmulmul.user.domain.badge.ChkBadge;
import com.sansantek.sansanmulmul.user.domain.badge.UserBadge;
import com.sansantek.sansanmulmul.user.repository.badge.BadgeRepository;
import com.sansantek.sansanmulmul.user.repository.badge.ChkBadgeRepository;
import com.sansantek.sansanmulmul.user.repository.badge.UserBadgeRepository;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeService {

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final ChkBadgeRepository chkBadgeRepository;
    private final HikingRecordRepository hikingRecordRepository;

    // 회원가입 시 기본 칭호 지정
    public void setBasicBadge(int userId) {
        // 기본 등린이 뱃지 확인
        Badge basicBadge = badgeRepository.findByBadgeId(1)
                .orElseThrow(() -> new RuntimeException("기본 칭호를 찾을 수 없습니다."));
        log.debug("basicBadge: {}", basicBadge);

        // 해당 회원 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException());
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

        // 해당 칭호 확인
        Badge badge = badgeRepository.findByBadgeId(badgeId)
                .orElseThrow(() -> new RuntimeException("기본 칭호를 찾을 수 없습니다."));

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

    // 해당 회원 칭호 체크 컬럼 추가
    public void setChkBadge(int userId) {
        // 유저를 조회합니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // ChkBadge 객체를 생성하고 User만 설정합니다.
        ChkBadge chkBadge = ChkBadge.builder()
                .user(user)
                .build();

        // ChkBadge 객체를 저장합니다.
        chkBadgeRepository.save(chkBadge);
    }
    
    // 해당 회원 칭호 체크
    public void chkBadge(int userId, int recordId) {
        // 해당 회원 확인
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        // ChkBadge와 유저를 조회
        ChkBadge chkBadge = chkBadgeRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("칭호 체크 정보를 찾을 수 없습니다."));

        // 해당 기록 확인
        HikingRecord record = hikingRecordRepository.findByRecordId(recordId)
                .orElseThrow(() -> new RuntimeException("해당 기록을 찾을 수 없습니다."));

        // 유저가 이미 획득한 칭호 리스트
        List<UserBadge> existingBadges = userBadgeRepository.findByUser_UserId(userId);
        Set<Integer> existingBadgeIds = existingBadges.stream()
                .map(userBadge -> userBadge.getBadge().getBadgeId())
                .collect(Collectors.toSet());
        
        // 유저의 누적 기록
        int history = user.getRecords().size();

        // 해당 기록에 대한 mountainId
        int mountainId = record.getMountain().getMountainId();
        
        // 날다람쥐 칭호 추가
        if(25 <= history && history < 50
                && !existingBadgeIds.contains(badgeRepository.findByBadgeId(2))) {
            addBadge(userId, 2);
        }

        // 셰르파 칭호 추가
        if(50 <= history && history < 100
               && !existingBadgeIds.contains(badgeRepository.findByBadgeId(2))) {
            addBadge(userId, 2);
        }

        // 엄홍길 칭호 추가
        if(100 <= history && !existingBadgeIds.contains(badgeRepository.findByBadgeId(3))) {
            addBadge(userId, 3);
        }

        // 1000 칭호 추가
        if (1000 <= user.getUserTotalLength() && user.getUserTotalLength() < 10000
            && !existingBadgeIds.contains(badgeRepository.findByBadgeId(5))) {
            addBadge(userId, 5);
        }

        // 10000 칭호 추가
        if (10000 <= user.getUserTotalLength()
                && !existingBadgeIds.contains(badgeRepository.findByBadgeId(6))) {
            addBadge(userId, 6);
        }

        // 한라산 등반자 칭호 추가
        if (chkBadge.isChkHanra() && mountainId == 93 && !existingBadgeIds.contains(7)) {
            addBadge(userId, 7);
        }

        // 지리산 등반자 칭호 추가
        if (chkBadge.isChkJiri() && mountainId == 78 && !existingBadgeIds.contains(8)) {
            addBadge(userId, 8);
        }

        // 설악산 등반자 칭호 추가
        if (chkBadge.isChkSeolak() && mountainId == 52 && !existingBadgeIds.contains(9)) {
            addBadge(userId, 9);
        }

        // 무등산 등반자 칭호 추가
        if (chkBadge.isChkMudeung() && mountainId == 35 && !existingBadgeIds.contains(10)) {
            addBadge(userId, 10);
        }

        // 계룡산 등반자 칭호 추가
        if (chkBadge.isChkGyeryong() && mountainId == 7 && !existingBadgeIds.contains(11)) {
            addBadge(userId, 11);
        }

        // 5대 명산 등반자 칭호 추가 (모든 산 등반한 경우)
        Optional<Badge> fiveMajorBadge = badgeRepository.findByBadgeId(12);
        if (chkBadge.isChkHanra() && chkBadge.isChkJiri() && chkBadge.isChkSeolak() &&
                chkBadge.isChkMudeung() && chkBadge.isChkGyeryong() &&
                !existingBadgeIds.contains(fiveMajorBadge.get().getBadgeId())) {
            addBadge(userId, fiveMajorBadge.get().getBadgeId());
        }

        // 영남회장 칭호 추가 (모든 영남 알프스 산 등반한 경우)
        Optional<Badge> yeongnamBadge = badgeRepository.findByBadgeId(13);
        if (chkBadge.isChkUnmun() && chkBadge.isChkGazi() && chkBadge.isChkChunhwang() &&
                chkBadge.isChkJaeyak() && chkBadge.isChkYoungchuck() && chkBadge.isChkSinbool() &&
                chkBadge.isChkGanweol() && chkBadge.isChkGohun() &&
                !existingBadgeIds.contains(yeongnamBadge.get().getBadgeId())) {
            addBadge(userId, yeongnamBadge.get().getBadgeId());
        }
    }

}
