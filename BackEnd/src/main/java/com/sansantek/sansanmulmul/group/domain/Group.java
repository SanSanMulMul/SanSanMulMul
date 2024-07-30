//package com.sansantek.sansanmulmul.group.domain;
//
//import com.sansantek.sansanmulmul.mountain.domain.Mountain;
//import com.sansantek.sansanmulmul.user.domain.User;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "`group`")
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class Group {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long groupId;
//
//    @ManyToOne
//    @JoinColumn(name = "leader_id", nullable = false)
//    private User leader;
//
//    @ManyToOne
//    @JoinColumn(name = "mountain_id", nullable = false)
//    private Mountain mountain;
//
//    @ManyToOne
//    @JoinColumn(name = "ascent_course_id", nullable = false)
//    private Course ascentCourse;
//
//    @ManyToOne
//    @JoinColumn(name = "descent_course_id", nullable = false)
//    private Course descentCourse;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(columnDefinition = "TEXT")
//    private String description;
//
//    @Column(nullable = false)
//    private int minAge;
//
//    @Column(nullable = false)
//    private int maxAge;
//
//    @Column(nullable = false, columnDefinition = "CHAR(1)")
//    private char genderRestriction;
//
//    @Column(nullable = false)
//    private int maxMembers;
//
//    @Column(nullable = false)
//    private LocalDateTime startDatetime;
//
//    @Column(nullable = false)
//    private LocalDateTime endDatetime;
//
//    @Column(nullable = false)
//    private boolean isFinished;
//
//    @Column(nullable = false)
//    private String shareLink;
//
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column(nullable = false)
//    private LocalDateTime updatedAt;
//
//    @Column(nullable = false)
//    private boolean isDeleted;
//}
