package com.sansantek.sansanmulmul.crew.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CrewGalleryResponse {
    // Crew - crewId (없앰)
    // pictureID
    // User - 닉네임, 프사, [본인 사진인지 여부]
    // Crew_Gallery - imgurl , createdAt

//    private int crewId;
    private int picId;
    private String userNickname;
    private String userProfileImg;
    private boolean isOwner; //본인 사진인지 여부
    private String imgUrl;
    private LocalDateTime createdAt;

}
