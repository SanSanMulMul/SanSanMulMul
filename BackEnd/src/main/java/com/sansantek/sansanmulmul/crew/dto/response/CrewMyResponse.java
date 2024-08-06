package com.sansantek.sansanmulmul.crew.dto.response;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CrewMyResponse {
    private int crewId;
    private String crewName;
    private String crewDescription;
    private LocalDateTime crewStartDate;
    private LocalDateTime crewEndDate;
    private boolean crewIsDone;
    private int crewMaxMembers;
    private int currentMembers;


    public static CrewMyResponse from(Crew crew, int currentMembers) {
        CrewMyResponse response = new CrewMyResponse();
        response.setCrewId(crew.getCrewId());
        response.setCrewName(crew.getCrewName());
        response.setCrewDescription(crew.getCrewDescription());
        response.setCrewStartDate(crew.getCrewStartDate());
        response.setCrewEndDate(crew.getCrewEndDate());
        response.setCrewIsDone(crew.isCrewIsDone());
        response.setCrewMaxMembers(crew.getCrewMaxMembers());
        response.setCurrentMembers(currentMembers); // 현재 인원 수 설정
        return response;
    }
}