package com.sansantek.sansanmulmul.crew.domain.crewuser;


import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name = "user_id" ,nullable = false)
    private User user;


    @Id
    @ManyToOne
    @JoinColumn(name = "crew_id" ,nullable = false)
    private Crew crew;

    @Column(name = "is_leader", nullable = false)
    @Schema(description = "방장유무" , example = "1")
    private Boolean isLeader;
}
