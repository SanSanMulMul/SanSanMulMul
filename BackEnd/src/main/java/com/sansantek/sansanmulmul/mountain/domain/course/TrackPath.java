package com.sansantek.sansanmulmul.mountain.domain.course;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "track_path")
@NoArgsConstructor
@Getter
@Setter
public class TrackPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_path_id", nullable = false)
    @Schema(description = "트랙 구간 고유 번호", example = "1")
    private Long trackPathId;

    @Column(name = "track_path_lat", nullable = false)
    @Schema(description = "트랙 구간 위도", example = "35.123412")
    private Double trackPathLat;

    @Column(name = "track_path_lon", nullable = false)
    @Schema(description = "트랙 구간 경도", example = "135.123412")
    private Double trackPathLon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", referencedColumnName = "track_id")
    private Track track;

}
