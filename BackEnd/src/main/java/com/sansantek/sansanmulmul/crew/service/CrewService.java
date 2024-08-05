package com.sansantek.sansanmulmul.crew.service;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyle;
import com.sansantek.sansanmulmul.crew.dto.request.CrewRequest;
import com.sansantek.sansanmulmul.crew.dto.response.CrewDetailResponse;
import com.sansantek.sansanmulmul.crew.dto.response.CrewResponse;
import com.sansantek.sansanmulmul.crew.repository.CrewRepository;
import com.sansantek.sansanmulmul.crew.service.style.CrewStyleService;
import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.repository.MountainRepository;
import jakarta.transaction.Transactional;
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
    private final MountainRepository mountainRepository;

    // service
    private final CrewStyleService crewStyleService;

    // 모든 그룹 조회
    public List<CrewResponse> getAllCrews() {
        List<CrewResponse> crews = new ArrayList<>();

        List<Crew> crewsList = crewRepository.findAll();

       for (Crew crew : crewsList) {
           CrewResponse cr = new CrewResponse(
                   crew.getCrewId(),
                   crew.getCrewName(),
                   crew.getCrewStartDate(),
                   crew.getCrewEndDate(),
                   crew.getCrewMaxMembers(),
                   crew.getMountain().getMountainImg()
           );

           crews.add(cr);
       }

        return crews;
    }

    // crewId에 해당하는 그룹 조회
    public CrewDetailResponse getCrewDetail(int crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));

        CrewDetailResponse crewDetailResponse = new CrewDetailResponse(
            crew.getCrewId(),
                crew.getCrewName(),
                crew.getCrewStartDate(),
                crew.getCrewEndDate(),
                crew.getCrewDescription(),
                crew.getCrewStyles(),
                crew.getCrewMaxMembers(),
                crew.getMountain().getMountainId(),
                crew.getMountain().getMountainName(),
                crew.getMountain().getMountainDescription(),
                crew.getMountain().getMountainImg()
        );

        return crewDetailResponse;
    }

    // 그룹 생성
    @Transactional
    public void addCrew(int crewId, CrewRequest request) {
        Mountain mountain = mountainRepository.findByMountainId(request.getMountainId());

        // request를 Crew정보로 저장
        Crew crew = new Crew(
                request.getCrewName(),
                request.getCrewDescription(),
                request.getCrewMaxMembers(),
                request.getCrewGender(),
                request.getCrewMinAge(),
                request.getCrewMaxAge(),
                mountain
        );

        // 그룹 링크를 제외하고 save
        crewRepository.save(crew);

        // 그룹 등산 스타일에 추가
        for (CrewHikingStyle style: crew.getCrewStyles()) {
            
        }

        // 그룹 메세지 추가


        // 그룹 링크 추가

    }
}
