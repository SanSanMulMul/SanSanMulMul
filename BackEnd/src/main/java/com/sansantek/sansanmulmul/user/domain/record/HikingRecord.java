package com.sansantek.sansanmulmul.user.domain.record;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.user.domain.User;
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
    private int recordId;

    @ManyToOne
    @JoinColumn(name = "mountain_id", nullable = false)
    private Mountain mountain;
    
    // 등산 그룹 멤버

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "record_start_time", nullable = false)
    private LocalDateTime recordStartTime;

    @Column(name = "record_up_distance", nullable = false)
    @ColumnDefault("0")
    private long recordUpDistance;

    @Column(name = "record_down_distance", nullable = false)
    @ColumnDefault("0")
    private long recordDownDistance;

    @Column(name = "record_duration", nullable = false)
    @ColumnDefault("0")
    private long recordDuration;

    @Column(name = "record_steps", nullable = false)
    @ColumnDefault("0")
    private long recordSteps;

    @Column(name = "record_elevation", nullable = false)
    @ColumnDefault("0.0")
    private double recordElevation;

    @Column(name = "record_kcal", nullable = false)
    @ColumnDefault("0")
    private long recordKcal;
}
