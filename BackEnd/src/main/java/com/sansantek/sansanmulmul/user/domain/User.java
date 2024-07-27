package com.sansantek.sansanmulmul.user.domain;

import com.sansantek.sansanmulmul.user.domain.badge.UserBadge;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.ArrayList;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "user_provider_id", nullable = false)
    private String userProviderId;

    @Column(name = "user_refresh_token")
    private String userRefreshToken;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_nickname", nullable = false)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBadge> userBadges = new ArrayList<>();

//    public User(String userProviderId, String userName, String userNickname, GenderStatus userGender, String userProfileImg, LocalDate userBirth, long userTotalLength, double userTotalElevation, long userTotalSteps, long userTotalKcal, long userTotalHiking, int userStoneCount) {
//        this.userProviderId = userProviderId;
//        this.userName = userName;
//        this.userNickname = userNickname;
//        this.userGender = userGender;
//        this.userProfileImg = userProfileImg;
//        this.userBirth = userBirth;
//        this.userTotalLength = userTotalLength;
//        this.userTotalElevation = userTotalElevation;
//        this.userTotalSteps = userTotalSteps;
//        this.userTotalKcal = userTotalKcal;
//        this.userTotalHiking = userTotalHiking;
//        this.userStoneCount = userStoneCount;
//    }

    public User(String userProviderId, String userName, String userNickname, GenderStatus userGender, String userProfileImg, LocalDate userBirth) {
        this.userProviderId = userProviderId;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userGender = userGender;
        this.userProfileImg = userProfileImg;
        this.userBirth = userBirth;
    }
 }
