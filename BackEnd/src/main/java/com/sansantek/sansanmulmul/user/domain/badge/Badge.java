package com.sansantek.sansanmulmul.user.domain.badge;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "badge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id", nullable = false)
    @Schema(description = "칭호 고유 번호", example = "1")
    private int badgeId;

    @Column(name = "badge_name", nullable = false)
    @Schema(description = "칭호 이름", example = "등린이")
    private String badgeName;

    @Column(name = "badge_description", nullable = false)
    @Schema(description = "칭호 설명", example = "처음 앱 가입 시")
    private String badgeDescription;

    @Column(name = "badge_image", nullable = false)
    @Schema(description = "칭호 고유 이미지", example = "👶")
    private String badgeImage;

}
