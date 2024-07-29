package com.sansantek.sansanmulmul.user.domain.style;

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
    private int hikingStylesId;

    @Column(name = "hiking_styles_name", nullable = false)
    private String hikingStylesName;
}
