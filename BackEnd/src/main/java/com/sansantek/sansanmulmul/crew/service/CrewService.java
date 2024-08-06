package com.sansantek.sansanmulmul.crew.service;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.CrewRestriction;
import com.sansantek.sansanmulmul.crew.domain.crewuser.CrewUser;
import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyle;
import com.sansantek.sansanmulmul.crew.dto.request.CrewRequest;
import com.sansantek.sansanmulmul.crew.dto.response.CrewDetailResponse;
import com.sansantek.sansanmulmul.crew.dto.response.CrewMyResponse;
import com.sansantek.sansanmulmul.crew.dto.response.CrewResponse;
import com.sansantek.sansanmulmul.crew.repository.CrewRepository;
import com.sansantek.sansanmulmul.crew.repository.request.CrewUserRepository;
import com.sansantek.sansanmulmul.crew.repository.style.CrewHikingStyleRepository;
import com.sansantek.sansanmulmul.crew.service.style.CrewStyleService;
import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.repository.MountainRepository;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import com.sansantek.sansanmulmul.user.repository.style.HikingStyleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrewService {

    // repository
    private final CrewRepository crewRepository;
    private final MountainRepository mountainRepository;
    private final CrewHikingStyleRepository crewStyleRepository;
    private final HikingStyleRepository hikingStyleRepository;

    @Autowired
    private CrewUserRepository crewUserRepository;

    @Autowired
    private UserRepository userRepository;
    // service
    private final CrewStyleService crewStyleService;

    // 모든 그룹 조회
    public List<CrewResponse> getAllCrews() {
        List<CrewResponse> crews = new ArrayList<>();

        List<Crew> crewsList = crewRepository.findAll();
        for (Crew crew : crewsList) {

            // 1. 현재날짜 이후것 부터 가져와야함 (CrewStartDate사용)
//            if (crew.getCrewStartDate())


            // 2. 현재 그룹에 인원 수 가져와야함 -> getCurrentMemberCount에 업데이트 되어있어야함
            int currentMember = 1;
            // 3. 현재 사용자(유저)가 이 그룹에 참여하고있는지 유무가 있어야 함
            boolean isUserJoined = false;


            CrewResponse cr = CrewResponse.builder()
                    .crewId(crew.getCrewId())
                    .crewName(crew.getCrewName())
                    .crewStartDate(crew.getCrewStartDate())
                    .crewEndDate(crew.getCrewEndDate())
                    .crewMaxMembers(crew.getCrewMaxMembers())
                    .crewCurrentMembers(currentMember) // Assuming this method exists
                    .isUserJoined(isUserJoined) // This needs to be determined based on the current user
                    .mountainImg(crew.getMountain().getMountainImg())
                    .build();

            crews.add(cr);
        }

        return crews;
    }

    // 그룹 {스타일} 검색 시 그룹 조회
    public List<CrewResponse> getCrewsListbyStyle(int styleId) {
        List<CrewResponse> crewStyleResponseList = new ArrayList<>();

        // styleId기반으로 그룹 찾기
        List<CrewHikingStyle> crewHikingStyleList = crewStyleRepository.findByStyle_HikingStylesId(styleId);

        // CrewStyleResponse 추출
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

        return crewStyleResponseList;
    }


    // 그룹 {성별} 검색 시 그룹 조회
//    public List<CrewResponse> getCrewsListbyStyle(CrewRestriction gender) {


    // 그룹 {연령} 검색 시 그룹 조회
//    public List<CrewResponse> getCrewsListbyAge(int minAge, int maxAge) {


    //////////////////////////////////////////////
    // crewId에 해당하는 그룹 상세 조회
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
        for (CrewHikingStyle style : crew.getCrewStyles()) {

        }

        // 그룹 메세지 추가


        // 그룹 링크 추가

    }
    //현재 진행중인 크루><
    public List<Crew> getingCrews(User user) {
        return crewUserRepository.findByUserAndCrew_CrewIsDone(user, false).stream()
                .map(CrewUser::getCrew)
                .collect(Collectors.toList());
    }
    //종료된 크루><
    public List<Crew> getCompletedCrews(User user) {
        return crewUserRepository.findByUserAndCrew_CrewIsDone(user, true).stream()
                .map(CrewUser::getCrew)
                .collect(Collectors.toList());
    }

    // 현재 인원 수를 계산하는 메서드
    public int getCurrentMemberCount(int crewId) {
        return crewUserRepository.countByCrewCrewId(crewId);

    }
}
