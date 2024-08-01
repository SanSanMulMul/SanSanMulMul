package com.sansantek.sansanmulmul.crew.service;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.dto.response.CrewResponse;
import com.sansantek.sansanmulmul.crew.repository.CrewRepository;
import com.sansantek.sansanmulmul.exception.style.GroupNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrewService {

    // repository
    private final CrewRepository crewRepository;

    public List<CrewResponse> getAllCrews() {
        List<CrewResponse> crews = new ArrayList<>();

        // DB에서 모든 그룹 가져오기
        List<Crew> crewList = crewRepository.findAll();

        for (Crew crew : crewList) {
            CrewResponse gr = new CrewResponse(
                    crew.getCrewId(),
                    crew.getCrewName(),
                    crew.getCrewStartDate(),
                    crew.getCrewEndDate(),
                    crew.getCrewMaxMembers(),
                    crew.getMountain().getMountainImg()
            );

            crews.add(gr);
        }

        if(crews.isEmpty())
            throw new GroupNotFoundException();

        return crews;
    }
}
