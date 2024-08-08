package com.sansantek.sansanmulmul.mountain.domain.course;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "course")
@NoArgsConstructor
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    @Schema(description = "코스 고유 번호" , example = "1")
    private Long courseId;

    @Column(name = "course_name", nullable = false)
    @Schema(description = "코스 이름", example = "금오산 제 1코스")
    private String courseName;

    @Column(name = "course_length", nullable = false)
    @Schema(description = "코스길이", example = "1.23")
    private double courseLength;

    @Column(name = "course_uptime", nullable = false)
    @Schema(description = "상행소요시간", example = "15")
    private int courseUptime;

    @Column(name = "course_downtime", nullable = false)
    @Schema(description = "하행 소요시간",example = "12")
    private int courseDowntime;

    @Column(name = "course_level", nullable = false)
    @Schema(description = "코스 난이도", example = "EASY")
    @Enumerated(EnumType.STRING)
    private Level courseLevel;

    @Column(name = "course_best_track_id")
    @Schema(description = "코스대표구간",example = "")
    private Integer courseBestTrackId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_code", referencedColumnName = "mountain_code")
    @JsonIgnore
    private Mountain mountain;

    // 그룹(crew)의 상행/하행 코스 설정
//    @OneToMany(mappedBy = "upCourse")
//    private List<Crew> upCrews;
//    @OneToMany(mappedBy = "downCourse")
//    private List<Crew> downCrews;


}