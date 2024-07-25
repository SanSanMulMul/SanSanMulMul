package com.sansantek.sansanmulmul.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Column(name = "user_total_length", nullable = false)
    private long userTotalLength;

    @Column(name = "user_total_elevation", nullable = false)
    private double userTotalElevation;

    @Column(name = "user_total_steps", nullable = false)
    private long userTotalSteps;

    @Column(name = "user_total_kcal", nullable = false)
    private long userTotalKcal;

    @Column(name = "user_total_hiking", nullable = false)
    private long userTotalHiking;

    @Column(name = "user_stone_count", nullable = false)
    private int userStoneCount;

    public User(String userProviderId, String userName, String userNickname, GenderStatus userGender, String userProfileImg, LocalDate userBirth, long userTotalLength, double userTotalElevation, long userTotalSteps, long userTotalKcal, long userTotalHiking, int userStoneCount) {
        this.userProviderId = userProviderId;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userGender = userGender;
        this.userProfileImg = userProfileImg;
        this.userBirth = userBirth;
        this.userTotalLength = userTotalLength;
        this.userTotalElevation = userTotalElevation;
        this.userTotalSteps = userTotalSteps;
        this.userTotalKcal = userTotalKcal;
        this.userTotalHiking = userTotalHiking;
        this.userStoneCount = userStoneCount;
    }

    public User(String userProviderId, String userName, String userNickname, GenderStatus userGender, String userProfileImg, LocalDate userBirth) {
        this.userProviderId = userProviderId;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userGender = userGender;
        this.userProfileImg = userProfileImg;
        this.userBirth = userBirth;
    }
 }
