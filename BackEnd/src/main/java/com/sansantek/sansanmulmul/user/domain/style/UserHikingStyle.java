package com.sansantek.sansanmulmul.user.domain.style;

import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.badge.UserBadgeId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_hiking_styles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@IdClass(UserHikingStyleId.class)
public class UserHikingStyle {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "hiking_styles_id", nullable = false)
    private HikingStyle style;
}
