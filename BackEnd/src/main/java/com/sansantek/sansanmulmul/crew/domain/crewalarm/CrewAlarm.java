package com.sansantek.sansanmulmul.crew.domain.crewalarm;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "crew_alarm")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CrewAlarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id", nullable = false)
    @Schema(description = "그룹 알림 고유번호" , example = "1")
    private int pictureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id" ,nullable = false)
    private Crew crew;

    @Column(name = "alarm_title", nullable = false)
    @Schema(description = "알림 제목", example = "가입 요청")
    private String alarmTitle;

    @Column(name = "alarm_body", nullable = false)
    @Schema(description = "알림 제목", example = "{username}님이 가입 요청 하셨습니다.")
    private String alarmBody;

    @Column(name = "alarm_created_at", nullable = false, updatable = false)
    @Schema(description = "알림 생성 일시")
    private LocalDateTime alarmCreatedAt;

}
