package com.sansantek.sansanmulmul.user.domain;

import com.sansantek.sansanmulmul.user.domain.badge.UserBadge;
import com.sansantek.sansanmulmul.user.domain.follow.Follow;
import com.sansantek.sansanmulmul.user.domain.style.UserHikingStyle;
import com.sansantek.sansanmulmul.user.domain.summitstone.UserSummitstone;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
    private int userId;

    @Column(name = "user_provider_id", nullable = false)
    private String userProviderId;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_refresh_token")
    private String userRefreshToken;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_nickname", nullable = false, unique = true)
    private String userNickname;

    @Column(name = "user_gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderStatus userGender;

    @Column(name = "user_profile_img")
    private String userProfileImg;

    @Column(name = "user_birth", nullable = false)
    private LocalDate userBirth;

    @Column(name = "user_static_badge", nullable = false)
    @ColumnDefault("1")
    private int userStaticBadge;

    @Column(name = "user_total_length", nullable = false)
    @ColumnDefault("0.0")
    private double userTotalLength;

    @Column(name = "user_total_elevation", nullable = false)
    @ColumnDefault("0.0")
    private double userTotalElevation;

    @Column(name = "user_total_steps", nullable = false)
    @ColumnDefault("0")
    private long userTotalSteps;

    @Column(name = "user_total_kcal", nullable = false)
    @ColumnDefault("0")
    private long userTotalKcal;

    @Column(name = "user_total_hiking", nullable = false)
    @ColumnDefault("0")
    private long userTotalHiking;

    @Column(name = "user_stone_count", nullable = false)
    @ColumnDefault("0")
    private int userStoneCount;

    @Column(name = "user_is_admin")
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

    public User(String userProviderId, String userPassword, String userName, String userNickname, GenderStatus userGender, String userProfileImg, LocalDate userBirth, boolean userIsAdmin) {
        this.userProviderId = userProviderId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userGender = userGender;
        this.userProfileImg = userProfileImg;
        this.userBirth = userBirth;
        this.userIsAdmin = userIsAdmin;
    }
}
