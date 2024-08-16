package com.sansantek.sansanmulmul.stone.service;

import com.sansantek.sansanmulmul.exception.auth.UserNotFoundException;
import com.sansantek.sansanmulmul.stone.domain.Summitstone;
import com.sansantek.sansanmulmul.stone.repository.SummitstoneRepository;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.stone.domain.UserSummitstone;
import com.sansantek.sansanmulmul.stone.dto.response.StoneResponse;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import com.sansantek.sansanmulmul.stone.repository.UserSummitstoneRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummitstoneService {

    private final UserRepository userRepository;
    private final SummitstoneRepository summitstoneRepository;
    private final UserSummitstoneRepository userSummitstoneRepository;

    // 해당 회원의 모든 정상석 조회
    public List<StoneResponse> getStoneListByUser(int userId) {
        List<StoneResponse> stoneList = new ArrayList<>();

        // userId에 해당하는 userStone 리스트 조회
        List<UserSummitstone> userStone = userSummitstoneRepository.findByUser_UserId(userId);

        // userStone리스트에서 stoneName을 추출해 stoneList에 저장
        for (UserSummitstone userSummitstone : userStone) {
            StoneResponse sr = new StoneResponse(
                    userSummitstone.getSummitstone().getStoneId(),
                    userSummitstone.getSummitstone().getMountain().getMountainName(),
                    userSummitstone.getSummitstone().getStoneName(),
                    userSummitstone.getSummitstone().getStoneImg());
            stoneList.add(sr);
        }

        return stoneList;

    }

    // 특정 정상석 조회
    public StoneResponse getStone(int stoneId) {
        Summitstone stone = summitstoneRepository.findByStoneId(stoneId)
                .orElseThrow(() -> new RuntimeException("해당 정상석을 찾을 수 없습니다."));

        return new StoneResponse(
                stone.getStoneId(),
                stone.getMountain().getMountainName(),
                stone.getStoneName(),
                stone.getStoneImg()
        );
    }

    // 해당 회원 인증 정상석 추가
    @Transactional
    public boolean addStone(int userId, int stoneId) {
        try {
            // 해당 회원 확인
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new UserNotFoundException());
            log.debug("user: {}", user);

            // 해당 정상석 확인
            Summitstone stone = summitstoneRepository.findByStoneId(stoneId)
                    .orElseThrow(() -> new RuntimeException("해당 정상석을 찾을 수 없습니다."));
            log.debug("stone: {}", stone);

            // userId와 stoneId 컬럼 생성하기
            UserSummitstone userSummitstone = UserSummitstone.builder()
                    .user(user)
                    .summitstone(stone)
                    .build();

            // User의 userSummitstones리스트에 추가
            user.getUserSummitstones().add(userSummitstone);

            // User엔티티를 저장해 userSummitstones 연관 관계 반영
            userRepository.save(user);

            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    // 모든 정상석 조회
    public List<StoneResponse> getStoneList() {
        List<StoneResponse> stoneList = new ArrayList<>();

        List<Summitstone> summitstones = summitstoneRepository.findAll();

        for (Summitstone summitstone : summitstones) {
            StoneResponse sr = new StoneResponse(
                    summitstone.getStoneId(),
                    summitstone.getMountain().getMountainName(),
                    summitstone.getStoneName(),
                    summitstone.getStoneImg()
            );

            stoneList.add(sr);
        }

        return stoneList;
    }

    // 해당 회원 인증 정상석 체크
    public boolean chkUserStone(String userProviderId, int mountainId) {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new UserNotFoundException());

        return userSummitstoneRepository.existsByUserUserIdAndSummitstoneStoneId(user.getUserId(), mountainId);
    }

    
}
