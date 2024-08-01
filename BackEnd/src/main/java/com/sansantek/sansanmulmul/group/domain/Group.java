package com.sansantek.sansanmulmul.group.domain;

import com.sansantek.sansanmulmul.group.domain.style.GroupHikingStyle;
import com.sansantek.sansanmulmul.mountain.domain.Mountain;
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
@Table(name = "`group`")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)
    @Schema(description = "그룹 고유 번호", example = "1")
    private int groupId;

    @ManyToOne
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader;

    @ManyToOne
    @JoinColumn(name = "mountain_id", nullable = false)
    private Mountain mountain;

//    @ManyToOne
//    @JoinColumn(name = "ascent_course_id", nullable = false)
//    private Course ascentCourse;
//
//    @ManyToOne
//    @JoinColumn(name = "descent_course_id", nullable = false)
//    private Course descentCourse;

    @Column(name = "group_name", nullable = false)
    @Schema(description = "그룹 이름", example = "한사랑 산악회")
    private String groupName;

    @Column(name = "group_description", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "그룹 설명", example = "모집합니다.")
    private String groupDescription;

    @Column(name = "group_min_age", nullable = false)
    @ColumnDefault("10")
    @Schema(description = "그룹 참여 최소 연령", example = "10")
    private int groupMinAge;

    @Column(name = "group_max_age", nullable = false)
    @ColumnDefault("90")
    @Schema(description = "그룹 참여 최대 연령", example = "90")
    private int groupMaxAge;

    @Column(name = "group_gender", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("A")
    @Schema(description = "그룹 허용 성별", example = "A")
    private GenderRestriction groupGender;

    @Column(name = "group_max_member", nullable = false)
    @ColumnDefault("10")
    @Schema(description = "그룹 참여 최대 인원", example = "10")
    private int groupMaxMembers;

    @Column(name = "group_start_date", nullable = false)
    @Schema(description = "그룹 시작 일시", example = "2024-08-01 23:59:59")
    private LocalDateTime groupStartDate;

    @Column(name = "group_end_date", nullable = false)
    @Schema(description = "그룹 종료 일시", example = "2024-08-02 23:59:59")
    private LocalDateTime groupEndDate;

    @Column(name = "group_is_done", nullable = false)
    @Schema(description = "그룹 종료 여부", example = "F")
    @ColumnDefault("false")
    private boolean groupIsDone;

    @Column(name = "group_link", nullable = false)
    @Schema(description = "그룹 카카오톡 공유 링크", example = "")
    private String groupLink;

    @Column(name = "group_created_at", nullable = false, updatable = false)
    @Schema(description = "그룹 생성 일시", example = "2024-08-01 23:59:59")
    private LocalDateTime groupCreatedAt;

    @Column(name = "group_modified_at", nullable = false)
    @Schema(description = "그룹 수정 일시", example = "2024-08-01 23:59:59")
    private LocalDateTime groupModifiedAt;

    @Column(name = "group_is_deleted", nullable = false)
    @ColumnDefault("false")
    @Schema(description = "그룹 삭제 유무", example = "false")
    private boolean groupIsDeleted;

    // 그룹 등산 스타일
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupHikingStyle> groupStyles = new ArrayList<>();
}
