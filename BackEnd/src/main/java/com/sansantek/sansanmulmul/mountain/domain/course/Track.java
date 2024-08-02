package com.sansantek.sansanmulmul.mountain.domain.course;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "track")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id", nullable = false)
    @Schema(description = "트랙 고유번호", example = "1")
    private int trackId;

    @Column(name = "track_no", nullable = false)
    @Schema(description = "트랙 순번", example = "1")
    private int trackNo;

    @Column(name = "track_name")
    @Schema(description = "구간 이름", example = "화동리구간")
    private String trackName;

    @Column(name = "track_length",nullable = false)
    @Schema(description = "구간 길이", example = "3.44")
    private Double trackLength;

    @Column(name = "track_level",nullable = false)
    @Schema(description = "구간 난이도", example = "EASY")
    private String trackLevel;

    @Column(name = "track_uptime", nullable = false)
    @Schema(description = "구간 상행시간", example = "40")
    private int trackUptime;

    @Column(name = "track_downtime", nullable = false)
    @Schema(description = "구간 하행시간", example = "40")
    private int trackDowntime;

    @Column(name = "track_best_path")
    @Schema(description = "구간 대표 좌표", example = "123124")
    private Long trackBestPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_code", referencedColumnName = "mountain_code")
    @JsonIgnore
    private Mountain mountain;


}
