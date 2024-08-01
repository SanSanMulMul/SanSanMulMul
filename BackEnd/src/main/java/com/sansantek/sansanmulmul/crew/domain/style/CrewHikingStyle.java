package com.sansantek.sansanmulmul.crew.domain.style;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.user.domain.style.HikingStyle;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_hiking_styles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@IdClass(CrewHikingStyleId.class)
public class CrewHikingStyle {

    @Id
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Crew crew;

    @Id
    @ManyToOne
    @JoinColumn(name = "hiking_styles_id", nullable = false)
    private HikingStyle style;

}
