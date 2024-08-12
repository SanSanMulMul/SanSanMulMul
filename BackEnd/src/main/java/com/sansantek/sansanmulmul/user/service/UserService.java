package com.sansantek.sansanmulmul.user.service;

import com.sansantek.sansanmulmul.common.service.S3Service;
import com.sansantek.sansanmulmul.exception.auth.UserNotFoundException;
import com.sansantek.sansanmulmul.exception.user.UserDeletionException;
import com.sansantek.sansanmulmul.exception.user.UserUpdateException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.dto.request.PostFcmTokenReq;
import com.sansantek.sansanmulmul.user.dto.request.SignUpUserRequest;
import com.sansantek.sansanmulmul.user.dto.request.UpdateUserHikingStyleRequest;
import com.sansantek.sansanmulmul.user.dto.request.UpdateUserRequest;
import com.sansantek.sansanmulmul.user.dto.response.MyPageResponse;
import com.sansantek.sansanmulmul.user.dto.response.UserInfoResponse;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import com.sansantek.sansanmulmul.user.repository.badge.BadgeRepository;
import com.sansantek.sansanmulmul.user.service.style.UserStyleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    // Spring Security
    private final PasswordEncoder passwordEncoder;

    // Repository
    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;

    // Service
    private final UserStyleService userStyleService;
    private final S3Service s3Service;

    @Transactional
    public User signUp(SignUpUserRequest signUpUserRequest, String password, MultipartFile image) throws IOException {

        // 프로필 이미지
        String imgUrl = "https://sanmulbucket.s3.ap-northeast-2.amazonaws.com/common/defaultProfileImg.png";
        if (image != null && !image.isEmpty()) imgUrl = s3Service.uploadS3(image, "profileImg"); //S3 내 profileImg폴더로
        log.info("[S3저장됨] imgUrl: " + imgUrl);

        // 비밀번호 인코딩
        String encoder = passwordEncoder.encode(password);

        // 회원 객체 생성
        User user = new User(
                signUpUserRequest.getUserProviderId(),
                encoder,
                signUpUserRequest.getUserName(),
                signUpUserRequest.getUserNickName(),
                signUpUserRequest.getUserGender(),
                imgUrl,
                signUpUserRequest.getUserBirth(),
                1,
                signUpUserRequest.isUserIsAdmin()
        );

        // 회원 영속성
        userRepository.save(user);

        return user;
    }

    public boolean isExistsUser(String userProviderId) {
        return userRepository.existsByUserProviderId(userProviderId);
    }

    public boolean isExistsUserNickname(String userNickname) {
        return userRepository.existsByUserNickname(userNickname);
    }

    public boolean isExistsUserNicknameNotMe(String userNickname, String userProviderId) {
        return userRepository.existsByUserNicknameAndUserProviderIdNot(userNickname, userProviderId);
    }

    public User getUser(String userProviderId) {
        return userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getUser(int userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public UserInfoResponse getUserResponse(String userProviderId) {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserInfoResponse userInfo = new UserInfoResponse(
                user.getUserId(),
                user.getUserProviderId(),
                user.getUserName(),
                user.getUserNickname(),
                user.getUserGender(),
                user.getUserProfileImg(),
                user.getUserBirth(),
                user.getUserStaticBadge(),
                user.getUserTotalLength(),
                user.getUserTotalElevation(),
                user.getUserTotalSteps(),
                user.getUserTotalKcal(),
                user.getUserTotalHiking(),
                user.getUserStoneCount(),
                user.isUserIsAdmin()
        );

        return userInfo;
    }

    public UserInfoResponse getUserResponse(int userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserInfoResponse userInfo = new UserInfoResponse(
                user.getUserId(),
                user.getUserProviderId(),
                user.getUserName(),
                user.getUserNickname(),
                user.getUserGender(),
                user.getUserProfileImg(),
                user.getUserBirth(),
                user.getUserStaticBadge(),
                user.getUserTotalLength(),
                user.getUserTotalElevation(),
                user.getUserTotalSteps(),
                user.getUserTotalKcal(),
                user.getUserTotalHiking(),
                user.getUserStoneCount(),
                user.isUserIsAdmin()
        );

        return userInfo;
    }

    @Transactional
    public boolean updateUser(String userProviderId, UpdateUserRequest updateUserRequest, MultipartFile image) throws IOException{
        try {
            // 사용자 조회
            User user = userRepository.findByUserProviderId(userProviderId)
                    .orElseThrow(() -> new UserNotFoundException());

            // 사용자 정보 업데이트
            user.setUserNickname(updateUserRequest.getUserNickName()); // 닉네임
            user.setUserStaticBadge(updateUserRequest.getUserStaticBadge()); // 칭호
            userStyleService.updateUserHikingStyle(user.getUserId(),
                    updateUserRequest.getStyles()); // 등산 스타일
            // 이미지
            String beforeImg = user.getUserProfileImg();
            String newImgUrl = beforeImg;
            if (image != null && !image.isEmpty()) { //Multipart 입력으로 이미지가 들어온 경우 (=이미지 수정할 경우)
                // 1. 기존 이미지 S3에서 삭제
                s3Service.deleteS3(beforeImg);
                // 2. 새 이미지 S3에 업로드
                newImgUrl = s3Service.uploadS3(image, "profileImg");
            }
            // 3. 새 이미지 DB에 업데이트
            user.setUserProfileImg(newImgUrl);

            // 사용자 정보 저장
            userRepository.save(user);

            return true;
        } catch (UserNotFoundException e) {
            // 사용자를 찾을 수 없는 경우
            throw e;
        } catch (Exception e) {
            // 일반적인 오류 발생 시
            throw new UserUpdateException("회원 정보 업데이트 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional
    public boolean deleteUser(String userProviderId) {
        try {
            //S3에서 이미지 삭제
            // 사용자 조회
            User user = userRepository.findByUserProviderId(userProviderId)
                    .orElseThrow(() -> new UserNotFoundException());
            String userImg = user.getUserProfileImg();
            s3Service.deleteS3(userImg); //s3에서 이미지 삭제

            userRepository.deleteByUserProviderId(userProviderId);

        } catch (Exception e) {

            throw new UserDeletionException("Failed to delete user with provider ID: " + userProviderId, e);

        }

        return true;
    }

    public MyPageResponse getMyPageResponse(String userProviderId) {
        try {
            // 사용자 조회
            User user = userRepository.findByUserProviderId(userProviderId)
                    .orElseThrow(() -> new UserNotFoundException());

            // 사용자 스타일
            List<String> styles = new ArrayList<>();
            for (int i = 0; i < user.getUserStyles().size(); i++) {
                String style = user.getUserStyles().get(i).getStyle().getHikingStylesName();
                styles.add(style);
            }

            String badge =badgeRepository.findByBadgeId(user.getUserStaticBadge()).get().getBadgeImage() + " " + badgeRepository.findByBadgeId(user.getUserStaticBadge()).get().getBadgeName();

            MyPageResponse value = new MyPageResponse(
                    user.getUserProfileImg(),
                    badge,
                    user.getUserNickname(),
                    user.getFollowers().size(),
                    user.getFollowings().size(),
                    styles
            );

            return value;
        } catch (UserNotFoundException e) {
            // 사용자를 찾을 수 없는 경우
            throw e;
        } catch (Exception e) {
            // 일반적인 오류 발생 시
            throw new UserUpdateException("회원 정보 업데이트 중 오류가 발생했습니다.", e);
        }
    }


    // fcm 토큰 저장
    @Transactional
    public Boolean updateFCMById(User user, String fcmToken) {

        if (user == null) throw new NoSuchElementException();

        user.setFcmToken(fcmToken);
        userRepository.save(user);

        return true;
    }
}
