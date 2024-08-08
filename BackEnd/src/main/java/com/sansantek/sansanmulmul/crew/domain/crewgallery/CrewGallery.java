package com.sansantek.sansanmulmul.crew.domain.crewgallery;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.crewuser.CrewUserId;
import com.sansantek.sansanmulmul.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "crew_gallery")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
@Getter
@Setter
@IdClass(CrewUserId.class)
public class CrewGallery {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" ,nullable = false)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id" ,nullable = false)
    private Crew crew;

    @Column(name = "img_url", nullable = false)
    @Schema(description = "이미지 url", example = "http://t1.kakaocdn.net/account_images/default_profile.jpeg.twg.thumb.R640x640")
    private String imgUrl;

    @Column(name = "img_created_at", nullable = false, updatable = false)
    @Schema(description = "이미지 업로드 일시")
    private LocalDateTime imgCreatedAt;
}