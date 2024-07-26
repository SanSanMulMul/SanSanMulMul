package com.sansantek.sansanmulmul.user.domain.badge;

import com.sansantek.sansanmulmul.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_badge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@IdClass(UserBadgeId.class)
public class UserBadge {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

}
