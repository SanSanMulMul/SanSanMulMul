package com.sansantek.sansanmulmul.record.domain;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "hiking_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class HikingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id", nullable = false)
    @Schema(description = "기록 고유 번호", example = "1")
    private int recordId;

    @ManyToOne
    @JoinColumn(name = "crew_id")
    @Schema(description = "그룹 고유 번호", example = "1")
    private Crew crew;

    @ManyToOne
    @JoinColumn(name = "mountain_id")
    @Schema(description = "산 고유 번호", example = "1")
    private Mountain mountain;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "회원 고유 번호", example = "1")
    private User user;

    @Column(name = "record_start_time", nullable = false)
    @Schema(description = "실제 등산 출발 일시", example = "YYYY-MM-DD 23:59:59")
    private LocalDateTime recordStartTime;

    @Column(name = "record_end_time", nullable = false)
    @Schema(description = "실제 등산 도착 일시", example = "YYYY-MM-DD 23:59:59")
    private LocalDateTime recordEndTime;

    @Column(name = "record_distance", nullable = false)
    @ColumnDefault("0")
    @Schema(description = "등산 전체 이동 거리(단위: m)", example = "0.0")
    private long recordDistance;

    @Column(name = "record_duration", nullable = false)
    @ColumnDefault("0")
    @Schema(description = "등산 산행 소요 시간(단위: 분)", example = "0")
    private long recordDuration;

    @Column(name = "record_steps", nullable = false)
    @ColumnDefault("0")
    @Schema(description = "등산 걸음 수(단위: 걸음)", example = "0")
    private long recordSteps;

    @Column(name = "record_elevation", nullable = false)
    @ColumnDefault("0.0")
    @Schema(description = "등산 고도(단위: m)", example = "0")
    private double recordElevation;

    @Column(name = "record_kcal", nullable = false)
    @ColumnDefault("0")
    @Schema(description = "등산 소요 칼로리(단위: Kcal)", example = "0")
    private int recordKcal;

    public HikingRecord(Crew crew, Mountain mountain, User user,
                        LocalDateTime recordStartTime, LocalDateTime recordEndTime,
                        long recordDistance, long recordDuration,
                        long recordSteps, double recordElevation, int recordKcal) {
        this.crew = crew;
        this.mountain = mountain;
        this.user = user;
        this.recordStartTime = recordStartTime;
        this.recordEndTime = recordEndTime;
        this.recordDistance = recordDistance;
        this.recordDuration = recordDuration;
        this.recordSteps = recordSteps;
        this.recordElevation = recordElevation;
        this.recordKcal = recordKcal;
    }
}
