package com.sansantek.sansanmulmul.crew.domain.crewuser;


import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.crewrequest.CrewRequestStatus;
import com.sansantek.sansanmulmul.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "crew_user")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
@Getter
@Setter
@IdClass(CrewUserId.class)
public class CrewUser {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" ,nullable = false)
    private User user;


    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id" ,nullable = false)
    private Crew crew;

    @Column(name = "is_leader",nullable = false)
    @Schema(description = "사용자가 이 그룹의 방장인지 여부", example = "F")
    private boolean isLeader;
}
