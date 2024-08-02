package com.sansantek.sansanmulmul.user.domain.style;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hiking_styles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class HikingStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hiking_styles_id", nullable = false)
    @Schema(description = "등산 스타일 고유 번호", example = "1")
    private int hikingStylesId;

    @Column(name = "hiking_styles_name", nullable = false)
    @Schema(description = "등산 스타일 이름", example = "설렁설렁")
    private String hikingStylesName;
}
