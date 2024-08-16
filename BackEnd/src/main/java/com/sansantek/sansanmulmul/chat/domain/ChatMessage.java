package com.sansantek.sansanmulmul.chat.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id" , nullable = false)
    @Schema(description = "메세지 고유번호" , example = "1")
    private int messageId;

    @Column(name = "message_content", nullable = false ,columnDefinition = "TEXT")
    @Schema(description = "메세지 내용", example = "안녕하세요")
    private String messageContent;

    @Column(name = "message_send_time", nullable = false)
    @Schema(description = "메세지 보낸 시간" , example = "2024-08-07")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "crew_id", referencedColumnName = "crew_id")
    @JsonIgnore
    private Crew crew;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
//    @JsonIgnore
    private User user;
}
