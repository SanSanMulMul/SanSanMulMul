package com.sansantek.sansanmulmul.mountain.service;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.domain.UserMountain;
import com.sansantek.sansanmulmul.mountain.repository.MountainRepository;
import com.sansantek.sansanmulmul.mountain.repository.UserMountainRepository;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserMountainService {

    private final UserRepository userRepository;
    private final MountainRepository mountainRepository;
    private final UserMountainRepository userMountainRepository;

    // 즐겨찾기 추가
    public void addLikedMountain(String userProviderId, int mountainId) {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Mountain mountain = mountainRepository.findById(mountainId)
                .orElseThrow(() -> new EntityNotFoundException("Mountain not found"));

        UserMountain userMountain = UserMountain.builder()
                .user(user)
                .mountain(mountain)
                .build();
        userMountainRepository.save(userMountain);
    }

    // 즐겨찾기 삭제
    public void removeLikedMountain(String userProviderId, int mountainId) {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Mountain mountain = mountainRepository.findById(mountainId)
                .orElseThrow(() -> new EntityNotFoundException("Mountain not found"));

        UserMountain userMountain = userMountainRepository.findByUserAndMountain(user, mountain)
                .orElseThrow(() -> new EntityNotFoundException("UserMountain not found"));
        userMountainRepository.delete(userMountain);
    }

    // 즐겨찾기 조회
    public List<Mountain> getLikedMountains(String userProviderId) {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        log.debug("User found: {}", user);

        List<Mountain> likedMountains = userMountainRepository.findMountainsByUser(user);
        log.debug("Liked Mountains: {}", likedMountains);

        return likedMountains;
    }
    //userId를 통한 즐겨찾기 조회
    public List<Mountain> getLikedMountainsByUserId(int userId) {
        // userId를 사용하여 User 엔티티를 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        log.debug("User found: {}", user);

        // 해당 유저가 즐겨찾기한 산 목록을 조회
        List<Mountain> likedMountains = userMountainRepository.findMountainsByUser(user);
        log.debug("Liked Mountains: {}", likedMountains);

        return likedMountains;
    }

}
