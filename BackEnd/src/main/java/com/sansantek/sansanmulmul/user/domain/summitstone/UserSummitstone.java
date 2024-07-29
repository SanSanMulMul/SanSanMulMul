package com.sansantek.sansanmulmul.user.domain.summitstone;

import com.sansantek.sansanmulmul.mountain.domain.summitstone.Summitstone;
import com.sansantek.sansanmulmul.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_summitstone")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@IdClass(UserSummitstoneId.class)
public class UserSummitstone {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "stone_id", nullable = false)
    private Summitstone summitstone;
}
