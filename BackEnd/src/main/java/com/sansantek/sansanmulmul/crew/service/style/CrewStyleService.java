package com.sansantek.sansanmulmul.crew.service.style;

import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyle;
import com.sansantek.sansanmulmul.crew.dto.response.CrewStyleResponse;
import com.sansantek.sansanmulmul.crew.repository.style.CrewHikingStyleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrewStyleService {

    private final CrewHikingStyleRepository CrewStyleRepository;

    public List<CrewStyleResponse> getCrewList(int styleId) {
        List<CrewStyleResponse> crewStyleResponseList = new ArrayList<>();

        // styleId기반으로 그룹 찾기
        List<CrewHikingStyle> crewHikingStyleList = CrewStyleRepository.findByStyle_HikingStylesId(styleId);

        // CrewStyleResponse 추출
        for (CrewHikingStyle crewHikingStyle : crewHikingStyleList) {
            CrewStyleResponse gr = new CrewStyleResponse(
                    crewHikingStyle.getCrew().getCrewId(),
                    crewHikingStyle.getCrew().getCrewName(),
                    crewHikingStyle.getCrew().getCrewStartDate(),
                    crewHikingStyle.getCrew().getCrewEndDate(),
                    crewHikingStyle.getCrew().getCrewMaxMembers()
            );

            crewStyleResponseList.add(gr);
        }

        return crewStyleResponseList;
    }
}
