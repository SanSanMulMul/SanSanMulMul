package com.sansantek.sansanmulmul.group.domain.style;

import com.sansantek.sansanmulmul.group.domain.Group;
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
@IdClass(GroupHikingStyleId.class)
public class GroupHikingStyle {

    @Id
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Id
    @ManyToOne
    @JoinColumn(name = "hiking_styles_id", nullable = false)
    private HikingStyle style;

}
