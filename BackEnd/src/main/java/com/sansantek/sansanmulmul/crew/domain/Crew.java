package com.sansantek.sansanmulmul.crew.domain;

import com.sansantek.sansanmulmul.crew.domain.crewgallery.CrewGallery;
import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyle;
import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.domain.course.Course;
import com.sansantek.sansanmulmul.record.domain.HikingRecord;
import com.sansantek.sansanmulmul.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "crew")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Crew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crew_id", nullable = false)
    @Schema(description = "그룹 고유 번호", example = "1")
    private int crewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "방장 유저id", example = "1")
    private User leader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_id", nullable = false)
    @ColumnDefault("14")
    @Schema(description = "산 고유 번호", example = "14")
    private Mountain mountain;

    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(description = "상행 코스 번호", example = "47190010101")
    @ColumnDefault("47190010101")
    @JoinColumn(name = "crew_up_course_id", nullable = false)
    private Course upCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @Schema(description = "하행 코스 번호", example = "47190010102")
    @ColumnDefault("47190010102")
    @JoinColumn(name = "crew_down_course_id", nullable = false)
    private Course downCourse;

    @Column(name = "crew_name", nullable = false)
    @Schema(description = "그룹 이름", example = "한사랑 산악회")
    private String crewName;

    @Column(name = "crew_description", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "그룹 설명", example = "모집합니다.")
    private String crewDescription;

    @Column(name = "crew_min_age", nullable = false)
    @ColumnDefault("10")
    @Schema(description = "그룹 참여 최소 연령", example = "10")
    private int crewMinAge;

    @Column(name = "crew_max_age", nullable = false)
    @ColumnDefault("90")
    @Schema(description = "그룹 참여 최대 연령", example = "90")
    private int crewMaxAge;

    @Column(name = "crew_gender", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("A")
    @Schema(description = "그룹 허용 성별", example = "A")
    private CrewRestriction crewGender;

    @Column(name = "crew_max_member", nullable = false)
    @ColumnDefault("10")
    @Schema(description = "그룹 참여 최대 인원", example = "10")
    private int crewMaxMembers;

    @Column(name = "crew_start_date", nullable = false)
    @Schema(description = "그룹 시작 일시", example = "2024-08-01 23:59:59")
    private LocalDateTime crewStartDate;

    @Column(name = "crew_end_date", nullable = false)
    @Schema(description = "그룹 종료 일시", example = "2024-08-02 23:59:59")
    private LocalDateTime crewEndDate;

    @Column(name = "crew_is_done", nullable = false)
    @Schema(description = "그룹 종료 여부", example = "F")
    @ColumnDefault("false")
    private boolean crewIsDone;

    @Column(name = "crew_link")
    @ColumnDefault("link is yet")
    @Schema(description = "그룹 카카오톡 공유 링크", example = "")
    private String crewLink;

    @Column(name = "crew_created_at", nullable = false, updatable = false)
    @Schema(description = "그룹 생성 일시", example = "2024-08-01 23:59:59")
    private LocalDateTime crewCreatedAt;

    @Column(name = "crew_modified_at")
    @Schema(description = "그룹 수정 일시", example = "2024-08-01 23:59:59")
    private LocalDateTime crewModifiedAt;

    @Column(name = "crew_is_deleted", nullable = false)
    @ColumnDefault("false")
    @Schema(description = "그룹 삭제 유무", example = "false")
    private boolean crewIsDeleted;

    // 그룹 등산 스타일
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrewHikingStyle> crewStyles = new ArrayList<>();

    // 그룹 기록
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HikingRecord> records = new ArrayList<>();

    // 그룹 갤러리 사진
    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrewGallery> pictures = new ArrayList<>();

    // 그룹 방장 변경
    public void changeLeader(User newLeader) {
        if (this.leader != null) {
            this.leader.getLeadingCrews().remove(this);
        }
        this.leader = newLeader;
        if (newLeader != null) {
            newLeader.getLeadingCrews().add(this);
        }
    }

    // 그룹 생성 시 사용
    public Crew(User leader, String crewName, String crewDescription, int crewMaxMembers, CrewRestriction crewGender, int crewMinAge, int crewMaxAge,
                LocalDateTime crewStartDate, LocalDateTime crewEndDate, Mountain mountain, Course upCourse, Course downCourse) {
        this.leader = leader;
        this.crewName = crewName;
        this.crewDescription = crewDescription;
        this.crewMaxMembers = crewMaxMembers;
        this.crewGender = crewGender;
        this.crewMinAge = crewMinAge;
        this.crewMaxAge = crewMaxAge;
        this.crewStartDate = crewStartDate;
        this.crewEndDate = crewEndDate;
        this.mountain = mountain;
        this.upCourse = upCourse;
        this.downCourse = downCourse;
        //날짜
        this.crewCreatedAt = LocalDateTime.now();
        this.crewModifiedAt = LocalDateTime.now();
    }

}