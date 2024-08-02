package com.sansantek.sansanmulmul.mountain.domain.course;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "course_tracks")
@NoArgsConstructor
@Getter
@Setter
public class CourseTracks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_tracks_id", nullable = false)
    @Schema(description = "코스 트랙 고유 번호", example = "1")
    private Integer courseTracksId;

    @Column(name = "course_tracks_sequence", nullable = false)
    @Schema(description = "코스 트랙연결 번호", example = "1")
    private Integer courseTracksSequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", referencedColumnName = "track_id")
    @JsonIgnore
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @JsonIgnore
    private Course course;
}
