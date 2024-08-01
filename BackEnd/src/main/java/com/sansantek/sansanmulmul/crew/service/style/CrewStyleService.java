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

    private final CrewHikingStyleRepository groupStyleRepository;

    public List<CrewStyleResponse> getGroupList(int styleId) {
        List<CrewStyleResponse> crewStyleResponseList = new ArrayList<>();

        // styleId기반으로 그룹 찾기
        List<CrewHikingStyle> crewHikingStyleList = groupStyleRepository.findByStyle_HikingStylesId(styleId);

        // CrewStyleResponse 추출
        for (CrewHikingStyle crewHikingStyle : crewHikingStyleList) {
            CrewStyleResponse gr = new CrewStyleResponse(
                    crewHikingStyle.getCrew().getGroupId(),
                    crewHikingStyle.getCrew().getGroupName(),
                    crewHikingStyle.getCrew().getGroupStartDate(),
                    crewHikingStyle.getCrew().getGroupEndDate(),
                    crewHikingStyle.getCrew().getGroupMaxMembers()
            );

            crewStyleResponseList.add(gr);
        }

        return crewStyleResponseList;
    }
}
