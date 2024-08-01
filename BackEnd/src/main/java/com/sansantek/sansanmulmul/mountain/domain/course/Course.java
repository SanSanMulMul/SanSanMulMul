//package com.sansantek.sansanmulmul.mountain.domain.course;
//
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.sansantek.sansanmulmul.mountain.domain.Mountain;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "course")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Getter
//@Setter
//@Builder
//@ToString
//public class Course {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "course_id", nullable = false)
//    private Long courseId;
//
//    @Column(name = "course_name", nullable = false)
//    private String courseName;
//
//    @Column(name = "course_length", nullable = false)
//    private double courseLength;
//
//    @Column(name = "course_uptime", nullable = false)
//    private int courseUptime;
//
//    @Column(name = "course_downtime", nullable = false)
//    private int courseDowntime;
//
//    @Column(name = "course_level", nullable = false)
//    private String courseLevel;
//
//    @Column(name = "course_best_track_id")
//    private int courseBestTrackId;
//
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "mountain_code", referencedColumnName = "mountain_code")
//    @JsonIgnore
//    private Mountain mountain;
//
//
//}
