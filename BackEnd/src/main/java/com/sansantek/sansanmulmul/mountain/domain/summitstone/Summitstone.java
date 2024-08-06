package com.sansantek.sansanmulmul.mountain.domain.summitstone;

import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "summitstone")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Summitstone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stone_id", nullable = false)
    @Schema(description = "정상석 고유 번호", example = "1")
    private int stoneId;

    @OneToOne
    @JoinColumn(name = "mountain_id", nullable = false)
    private Mountain mountain; // Mountain 엔티티를 참조

    @Column(name = "stone_name", nullable = false)
    @Schema(description = "정상석 이름", example = "가야산")
    private String stoneName;

    @Column(name = "stone_img", nullable = false)
    @Schema(description = "정상석 이미지", example = "Img")
    private String stoneImg;
}
