package com.sansantek.sansanmulmul.crew.service.style;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyle;
import com.sansantek.sansanmulmul.crew.dto.response.CrewResponse;
import com.sansantek.sansanmulmul.crew.repository.CrewRepository;
import com.sansantek.sansanmulmul.crew.repository.style.CrewHikingStyleRepository;
import com.sansantek.sansanmulmul.exception.style.AlreadyStyleException;
import com.sansantek.sansanmulmul.user.domain.style.HikingStyle;
import com.sansantek.sansanmulmul.user.repository.style.HikingStyleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrewStyleService {

    private final CrewHikingStyleRepository crewStyleRepository;
    private final CrewRepository crewRepository;
    private final HikingStyleRepository hikingStyleRepository;

//    public List<CrewResponse> getCrewList(int styleId) {
//        List<CrewResponse> crewStyleResponseList = new ArrayList<>();
//
//        // styleId기반으로 그룹 찾기
//        List<CrewHikingStyle> crewHikingStyleList = crewStyleRepository.findByStyle_HikingStylesId(styleId);
//
//        // CrewStyleResponse 추출
//        for (CrewHikingStyle crewHikingStyle : crewHikingStyleList) {
//            CrewResponse gr = new CrewResponse(
//                    crewHikingStyle.getCrew().getCrewId(),
//                    crewHikingStyle.getCrew().getCrewName(),
//                    crewHikingStyle.getCrew().getCrewStartDate(),
//                    crewHikingStyle.getCrew().getCrewEndDate(),
//                    crewHikingStyle.getCrew().getCrewMaxMembers()
//            );
//
//            crewStyleResponseList.add(gr);
//        }
//
//        return crewStyleResponseList;
//    }

    @Transactional
    public void addStyle(int crewId, int hikingStyleId) {
        // 추가를 진행할 그룹 정보 조회
        Crew crew = crewRepository.findByCrewId(crewId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));

        // 추가를 진행할 등산 스타일 정보 조회
        HikingStyle hikingStyle = hikingStyleRepository.findByHikingStylesId(hikingStyleId)
                .orElseThrow(() -> new RuntimeException("해당 등산 스타일을 찾을 수 없습니다."));

        // 이미 추가되어 있는지 확인
        Optional<CrewHikingStyle> existingStyle = crewStyleRepository.findByCrewAndStyle(crew, hikingStyle);
        if (existingStyle.isPresent())
            throw new AlreadyStyleException();

        // crewHikingStyle 정보 생성
        CrewHikingStyle style = CrewHikingStyle.builder()
                .crew(crew)
                .style(hikingStyle)
                .build();

        // Crew의 crewStyles리스트에 추가
        crew.getCrewStyles().add(style);

        // Crew엔티티를 저장해 style 연관 관계 반영
        crewRepository.save(crew);
    }
}
