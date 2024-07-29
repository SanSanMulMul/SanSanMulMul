package com.sansantek.sansanmulmul.mountain.domain;


import com.sansantek.sansanmulmul.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_like_mountain")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@IdClass(UserMountainId.class)
public class UserMountain {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "mountain_id", nullable = false)
    private Mountain mountain;
}
