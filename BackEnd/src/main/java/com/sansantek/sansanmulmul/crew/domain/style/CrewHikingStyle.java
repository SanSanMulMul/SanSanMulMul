package com.sansantek.sansanmulmul.crew.domain.style;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.user.domain.style.HikingStyle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "crew_hiking_styles")
@IdClass(CrewHikingStyleId.class)
@Getter
@Setter
public class CrewHikingStyle {

    @Id
    @ManyToOne
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    @Id
    @ManyToOne
    @JoinColumn(name = "hiking_styles_id", nullable = false)
    private HikingStyle style;

}
