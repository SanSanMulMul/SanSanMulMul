package com.sansantek.sansanmulmul.crew.domain.crewrequest;


import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="crew_request")
@NoArgsConstructor
@Getter
@Setter
public class CrewRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    @Schema(description = "가입요청 고유번호" , example = "1")
    private int requestId;

    @Column(name = "crew_request_status",nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "요청상태 ", example = "R")
    private CrewRequestStatus crewRequestStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", referencedColumnName = "crew_id")
    private Crew crew;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , referencedColumnName = "user_id")
    private User user;






}
