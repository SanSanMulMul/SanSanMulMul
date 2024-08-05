package com.sansantek.sansanmulmul.user.service.style;

import com.sansantek.sansanmulmul.exception.style.AlreadyStyleException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.style.HikingStyle;
import com.sansantek.sansanmulmul.user.domain.style.UserHikingStyle;
import com.sansantek.sansanmulmul.user.dto.request.UpdateUserHikingStyleRequest;
import com.sansantek.sansanmulmul.user.dto.response.UserStyleResponse;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import com.sansantek.sansanmulmul.user.repository.style.HikingStyleRepository;
import com.sansantek.sansanmulmul.user.repository.style.UserHikingStyleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStyleService {

    private final UserRepository userRepository;
    private final HikingStyleRepository hikingStyleRepository;
    private final UserHikingStyleRepository userStyleRepository;

    public String getStyleName(int hikingStyleId) {
        // hikingStyleId로 스타일 엔티티를 조회하고 스타일 이름을 반환
        return hikingStyleRepository.findById(hikingStyleId).get().getHikingStylesName();
    }

    // userId회원의 모든 등산 스타일 조회
    public List<Integer> getStyleList(int userId) {
        List<Integer> styleList = new ArrayList<>();

        // userId에 해당하는 userHikingStyle 리스트 조회
        List<UserHikingStyle> userHikingStyles = userStyleRepository.findByUser_UserId(userId);

        // userHikingStyles리스트에서 styleName을 추출해 styleList에 저장
        for (UserHikingStyle style : userHikingStyles)
            styleList.add(style.getStyle().getHikingStylesId());

        return styleList;
    }

    @Transactional
    // userId회원이 hikingStyleId스타일을 추가
    public void addStyle(int userId, int hikingStyleId) {
        // 추가를 진행할 회원 정보 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        // 추가를 진행할 등산 스타일 정보 조회
        HikingStyle hikingStyle = hikingStyleRepository.findByHikingStylesId(hikingStyleId)
                .orElseThrow(() -> new RuntimeException("해당 등산 스타일을 찾을 수 없습니다."));

        // 이미 추가했는지 확인
        Optional<UserHikingStyle> existingStyle = userStyleRepository.findByUserAndStyle(user, hikingStyle);
        if (existingStyle.isPresent())
            throw new AlreadyStyleException();

        // userHikingStyle 정보 생성
        UserHikingStyle style = UserHikingStyle.builder()
                .user(user)
                .style(hikingStyle)
                .build();

        // User의 userStyles리스트에 추가
        user.getUserStyles().add(style);

        // User엔티티를 저장해 style 연관 관계 반영
        userRepository.save(user);
    }

    @Transactional
    // userId 회원이 여러 hikingStyleId 스타일을 추가
    public void addStyles(int userId, UpdateUserHikingStyleRequest request) {
        // 추가를 진행할 회원 정보 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        // 각 스타일 ID에 대해 추가 작업 수행
        for (int hikingStyleId : request.getStyles()) {
            // 추가를 진행할 등산 스타일 정보 조회
            HikingStyle hikingStyle = hikingStyleRepository.findByHikingStylesId(hikingStyleId)
                    .orElseThrow(() -> new RuntimeException("해당 등산 스타일을 찾을 수 없습니다."));

            // 이미 추가했는지 확인
            Optional<UserHikingStyle> existingStyle = userStyleRepository.findByUserAndStyle(user, hikingStyle);
            if (existingStyle.isPresent()) {
                throw new AlreadyStyleException();
            }

            // userHikingStyle 정보 생성
            UserHikingStyle style = UserHikingStyle.builder()
                    .user(user)
                    .style(hikingStyle)
                    .build();

            // User의 userStyles 리스트에 추가
            user.getUserStyles().add(style);
        }

        // User 엔티티를 저장해 style 연관 관계 반영
        userRepository.save(user);
    }

    @Transactional
    // userId회원이 hikingStyleId스타일을 제거
    public void deleteStyle(int userId, int hikingStyleId) {
        // 제거를 진행할 회원 정보 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        log.info("user: {}", user);

        // 제거를 진행할 스타일 정보 조회
        HikingStyle hikingStyle = hikingStyleRepository.findByHikingStylesId(hikingStyleId)
                .orElseThrow(() -> new RuntimeException("해당 등산 스타일을 찾을 수 없습니다."));
        log.info("style: {}", hikingStyle);

        // 이미 제거했는지 확인
        Optional<UserHikingStyle> existingStyle = userStyleRepository.findByUserAndStyle(user, hikingStyle);
        if (existingStyle.isEmpty())
            throw new AlreadyStyleException();

        // UserHikingStyle객체 삭제
        userStyleRepository.delete(existingStyle.get());

        // 양방향 관계 삭제
        user.getUserStyles().remove(existingStyle.get());

        // User엔티티를 저장해 style 연관 관계 반영
        userRepository.save(user);
    }

    @Transactional
    // userId 회원이 갖고 있는 모든 등산 스타일 제거
    public void deleteAllStyles(int userId) {
        // 제거를 진행할 회원 정보 조회
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        log.info("user: {}", user);

        // 회원이 갖고 있는 모든 등산 스타일 조회
        List<UserHikingStyle> userHikingStyles = userStyleRepository.findByUser(user);

        // 각 스타일에 대해 삭제 작업 수행
        for (UserHikingStyle userHikingStyle : userHikingStyles) {
            // UserHikingStyle 객체 삭제
            userStyleRepository.delete(userHikingStyle);

            // 양방향 관계 삭제
            user.getUserStyles().remove(userHikingStyle);
        }

        // User 엔티티를 저장해 style 연관 관계 반영
        userRepository.save(user);
    }

    @Transactional
    public Boolean updateUserHikingStyle(int userId, UpdateUserHikingStyleRequest request) {
        try {
            // userId회원의 모든 스타일을 제거
            deleteAllStyles(userId);

            // userId회원의 hikingStyleId스타일을 추가
            addStyles(userId, request);

            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
