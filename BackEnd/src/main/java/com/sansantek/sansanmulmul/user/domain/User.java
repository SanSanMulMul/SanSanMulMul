package com.sansantek.sansanmulmul.user.domain;

import com.sansantek.sansanmulmul.user.domain.badge.UserBadge;
import com.sansantek.sansanmulmul.user.domain.follow.Follow;
import com.sansantek.sansanmulmul.user.domain.style.UserHikingStyle;
import com.sansantek.sansanmulmul.user.domain.summitstone.UserSummitstone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class User /* implements UserDetails */ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    @Schema(description = "회원 고유 번호", example = "1")
    private int userId;

    @Column(name = "user_provider_id", nullable = false)
    @Schema(description = "회원 카카오 아이디", example = "33213515")
    private String userProviderId;

    @Column(name = "user_password", nullable = false)
    @Schema(description = "회원 비밀번호", example = "")
    @ColumnDefault("")
    private String userPassword;

    @Column(name = "user_refresh_token")
    @Schema(description = "회원 refreshToken", example = "ex7534487435468~~")
    private String userRefreshToken;

    @Column(name = "user_name", nullable = false)
    @Schema(description = "회원 이름", example = "김싸피")
    private String userName;

    @Column(name = "user_nickname", nullable = false, unique = true)
    @Schema(description = "회원 닉네임", example = "싸린이")
    private String userNickname;

    @Column(name = "user_gender", nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "회원 성별", example = "M")
    private GenderStatus userGender;

    @Column(name = "user_profile_img", nullable = false)
    @Schema(description = "회원 카카오 프로필 이미지", example = "http://t1.kakaocdn.net/account_images/default_profile.jpeg.twg.thumb.R640x640")
    private String userProfileImg;

    @Column(name = "user_birth", nullable = false)
    @Schema(description = "회원 생일", example = "2024-08-01")
    private LocalDate userBirth;

    @Column(name = "user_static_badge", nullable = false)
    @Schema(description = "회원 고정 칭호", example = "1")
    @ColumnDefault("1")
    private int userStaticBadge;

    @Column(name = "user_total_length", nullable = false)
    @Schema(description = "회원 누적 이동거리", example = "0.0")
    @ColumnDefault("0.0")
    private double userTotalLength;

    @Column(name = "user_total_elevation", nullable = false)
    @Schema(description = "회원 누적 고도", example = "0.0")
    @ColumnDefault("0.0")
    private double userTotalElevation;

    @Column(name = "user_total_steps", nullable = false)
    @Schema(description = "회원 누적 걸음수", example = "0")
    @ColumnDefault("0")
    private long userTotalSteps;

    @Column(name = "user_total_kcal", nullable = false)
    @Schema(description = "회원 누적 소모 칼로리", example = "0")
    @ColumnDefault("0")
    private long userTotalKcal;

    @Column(name = "user_total_hiking", nullable = false)
    @Schema(description = "회원 누적 산행 횟수", example = "0")
    @ColumnDefault("0")
    private long userTotalHiking;

    @Column(name = "user_stone_count", nullable = false)
    @Schema(description = "회원 획득 비석 개수", example = "0")
    @ColumnDefault("0")
    private int userStoneCount;

    @Column(name = "user_is_admin")
    @Schema(description = "회원 관리자 유무", example = "false")
    @ColumnDefault("false")
    private boolean userIsAdmin;

    // 회원 칭호
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBadge> userBadges = new ArrayList<>();

    // 회원 인증 정상석
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSummitstone> userSummitstones = new ArrayList<>();

    // 회원 팔로잉
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followings = new ArrayList<>();

    // 회원 팔로워
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    // 회원 등산 스타일
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserHikingStyle> userStyles = new ArrayList<>();


    public User(String userProviderId, String userPassword, String userName, String userNickname, GenderStatus userGender, String userProfileImg, LocalDate userBirth, int userStaticBadge, boolean userIsAdmin) {
        this.userProviderId = userProviderId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userGender = userGender;
        this.userProfileImg = userProfileImg;
        this.userBirth = userBirth;
        this.userStaticBadge = userStaticBadge;
        this.userIsAdmin = userIsAdmin;
    }

        public User(String userProviderId, String userPassword, String userName, String userNickname, GenderStatus userGender, String userProfileImg, LocalDate userBirth, int userStaticBadge, boolean userIsAdmin, List<UserHikingStyle> userStyles) {
        this.userProviderId = userProviderId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userGender = userGender;
        this.userProfileImg = userProfileImg;
        this.userBirth = userBirth;
        this.userStaticBadge = userStaticBadge;
        this.userIsAdmin = userIsAdmin;
        this.userStyles = userStyles;
    }
}
