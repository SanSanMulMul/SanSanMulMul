package com.sansantek.sansanmulmul.crew.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
public class CrewResponse {

    private int crewId;
    private String crewName;
    private LocalDateTime crewStartDate;
    private LocalDateTime crewEndDate;
    private int crewMaxMembers;
    // 참여 인원, 멤버가 그룹 참여 상태
    private String mountainImg;
}
