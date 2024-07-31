package com.sansantek.sansanmulmul.user.domain.follow;

import com.sansantek.sansanmulmul.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "follow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@IdClass(FollowId.class)
public class Follow {

    @Id
    @ManyToOne
    @MapsId("follower")  // FollowId의 follower 필드와 매핑
    @JoinColumn(name = "follower_user_id", nullable = false)
    private User follower;

    @Id
    @ManyToOne
    @MapsId("following") // FollowId의 following 필드와 매핑
    @JoinColumn(name = "following_user_id", nullable = false)
    private User following;
}
