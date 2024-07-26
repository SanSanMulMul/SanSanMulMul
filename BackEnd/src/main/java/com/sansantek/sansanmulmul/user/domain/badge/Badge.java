package com.sansantek.sansanmulmul.user.domain.badge;

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
    private int badgeId;

    @Column(name = "badge_name", nullable = false)
    private String badgeName;

    @Column(name = "badge_description", nullable = false)
    private String badgeDescription;

    @Column(name = "badge_image", nullable = false)
    private String badgeImage;

}
