package com.sansantek.sansanmulmul.crew.service;

import com.sansantek.sansanmulmul.crew.domain.Crew;

import com.sansantek.sansanmulmul.crew.domain.crewrequest.CrewRequest;
import com.sansantek.sansanmulmul.crew.domain.crewuser.CrewUser;
import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyle;
import com.sansantek.sansanmulmul.crew.dto.request.CrewCreateRequest;
import com.sansantek.sansanmulmul.crew.dto.response.CrewDetailResponse;
import com.sansantek.sansanmulmul.crew.dto.response.CrewMyResponse;
import com.sansantek.sansanmulmul.crew.dto.response.CrewResponse;
import com.sansantek.sansanmulmul.crew.repository.CrewRepository;
import com.sansantek.sansanmulmul.crew.repository.CrewHikingStyleRepository;
import com.sansantek.sansanmulmul.crew.repository.request.CrewUserRepository;
import com.sansantek.sansanmulmul.exception.style.AlreadyStyleException;
import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.domain.course.Course;
import com.sansantek.sansanmulmul.mountain.repository.MountainRepository;
import com.sansantek.sansanmulmul.mountain.repository.course.CourseRepository;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.style.HikingStyle;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import com.sansantek.sansanmulmul.user.repository.style.HikingStyleRepository;
import com.sansantek.sansanmulmul.user.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrewService {

    // repository
    private final CrewRepository crewRepository;
    private final MountainRepository mountainRepository;
    private final CrewHikingStyleRepository crewStyleRepository;
    private final UserRepository userRepository;
    private final CrewUserRepository crewUserRepository;
    private final HikingStyleRepository hikingStyleRepository;

    // service
    private final UserService userService;
    private final CourseRepository courseRepository;

    /* 1. 그룹 전체 조회 */
    // 모든 그룹 조회
    public List<CrewResponse> getAllCrews(String userProviderId) {
        List<CrewResponse> crews = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now(); //현재시간

        //현재 사용자 확인
        User currentUser = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        List<Crew> crewList = crewRepository.findAll();
        for (Crew crew : crewList) {

            // 1. 현재날짜 이후것 부터 가져와야함 (CrewStartDate사용)
            if (crew.getCrewStartDate().isAfter(now)) {
                // 2. 현재 그룹에 속한 인원 수 가져옴
                int currentMember = crewUserRepository.countByCrew_CrewId(crew.getCrewId());
                // 3. 현재 사용자(유저)가 이 그룹에 참여하고있는지 확인
                boolean isUserJoined = crewUserRepository.existsByCrewAndUser(crew, currentUser);
                // 4. 그룹 스타일 Integer List로
                List<Integer> styles = new ArrayList<>();
                for (int i = 0; i < crew.getCrewStyles().size(); i++) {
                    int styleId = crew.getCrewStyles().get(i).getStyle().getHikingStylesId();
                    styles.add(styleId);
                }

                CrewResponse cr = CrewResponse.builder()
                        .crewId(crew.getCrewId())
                        .crewName(crew.getCrewName())
                        .mountainName(crew.getMountain().getMountainName())
                        .crewStartDate(crew.getCrewStartDate())
                        .crewEndDate(crew.getCrewEndDate())
                        .crewMaxMembers(crew.getCrewMaxMembers())
                        .crewCurrentMembers(currentMember) // Assuming this method exists
                        .isUserJoined(isUserJoined) // This needs to be determined based on the current user
                        .mountainImg(crew.getMountain().getMountainImg())
                        .crewMinAge(crew.getCrewMinAge())
                        .crewMaxAge(crew.getCrewMaxAge())
                        .crewGender(crew.getCrewGender())
                        .crewStyles(styles)
                        .build();

                crews.add(cr);
            }
        }

        return crews;
    }

    // 그룹 {스타일 } 검색 시 그룹 조회
    @Transactional(readOnly = true)
    public List<CrewResponse> getCrewListbyStyle(int styleId, String userProviderId) {
        List<CrewResponse> crews = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now(); //현재시간

        //현재 사용자 확인
        User currentUser = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // styleId기반으로 그룹 찾기
        List<CrewHikingStyle> crewHikingStyleList = crewStyleRepository.findByStyle_HikingStylesId(styleId);

        // CrewStyleResponse 추출
        for (CrewHikingStyle crewHikingStyle : crewHikingStyleList) {
            Crew crew = crewHikingStyle.getCrew();

            // 1. 현재날짜 이후것 부터 가져와야함 (CrewStartDate사용)
            if (crew.getCrewStartDate().isAfter(now)) {
                // 2. 현재 그룹에 속한 인원 수 가져옴
                int currentMember = crewUserRepository.countByCrew_CrewId(crew.getCrewId());
                // 3. 현재 사용자(유저)가 이 그룹에 참여하고있는지 확인
                boolean isUserJoined = crewUserRepository.existsByCrewAndUser(crew, currentUser);


                CrewResponse cr = CrewResponse.builder()
                        .crewId(crew.getCrewId())
                        .crewName(crew.getCrewName())
                        .crewStartDate(crew.getCrewStartDate())
                        .crewEndDate(crew.getCrewEndDate())
                        .crewMaxMembers(crew.getCrewMaxMembers())
                        .crewCurrentMembers(currentMember)
                        .isUserJoined(isUserJoined)
                        .mountainImg(crew.getMountain().getMountainImg())
                        .build();

                crews.add(cr);
            }
        }

        return crews;
    }

    // 그룹 {성별} 검색 시 그룹 조회
//    public List<CrewResponse> getCrewsListbyStyle(CrewRestriction gender) {


    // 그룹 {연령} 검색 시 그룹 조회
//    public List<CrewResponse> getCrewsListbyAge(int minAge, int maxAge) {


    ////////////////////////////////////////////////////////////

    /* 2. 그룹 생성 */
    @Transactional
    public Crew createCrew(int userId, CrewCreateRequest request) { //방장userId, 요청dto

        //방장
        User leader = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        //산
        Mountain mountain = mountainRepository.findByMountainId(request.getMountainId())
                .orElseThrow(() -> new RuntimeException("해당 산을 찾을 수 없습니다."));

        //코스 (상행, 하행)
        Course upCourse = courseRepository.findByCourseId(request.getUpCourseId());
        Course downCourse = courseRepository.findByCourseId(request.getDownCourseId());

        // 생성요청request를 Crew정보로 저장 (그룹 등산 스타일 제외)
        Crew crew = new Crew(
                leader,
                request.getCrewName(),
                request.getCrewDescription(),
                request.getCrewMaxMembers(),
                request.getCrewGender(),
                request.getCrewMinAge(),
                request.getCrewMaxAge(),
                request.getCrewStartDate(),
                request.getCrewEndDate(),
                mountain,
                upCourse,
                downCourse
        );

        // 그룹 링크를 제외하고 save
        crewRepository.save(crew);

        return crew;

    }

    @Transactional
    public void addStyles(Crew crew, List<Integer> hikingStyles) {
        // 추가를 진행할 그룹 정보 조회
//        Crew crew = crewRepository.findByCrewId(crewId)
//                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));

//        // 이미 추가되어 있는지 확인 <- 그룹 수정 단계에서 필요
//        Optional<CrewHikingStyle> existingStyle = crewStyleRepository.findByCrewAndStyle(crew, hikingStyle);
//        if (existingStyle.isPresent())
//            throw new AlreadyStyleException();

        //그룹에 hikingstyle 추가
        HikingStyle hikingStyle = null;
        CrewHikingStyle crewHikingStyle = null;

        for (int hikingStyleId : hikingStyles) {
            // 추가를 진행할 등산 스타일 정보 조회
            hikingStyle = hikingStyleRepository.findByHikingStylesId(hikingStyleId)
                    .orElseThrow(() -> new RuntimeException("해당 등산 스타일을 찾을 수 없습니다."));

            //crewHikingStyle 정보 생성
            crewHikingStyle = crewHikingStyle.builder()
                    .crew(crew)
                    .style(hikingStyle)
                    .build();

            crew.getCrewStyles().add(crewHikingStyle);
        }


        // Crew엔티티를 저장해 style 연관 관계 반영
        crewRepository.save(crew);
    }

    public void updateCrewUser(Crew crew, int userId) {
        //현재 사용자 확인
        User leader = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        CrewUser crewUser = CrewUser.builder()
                .crew(crew)
                .user(leader)
                .isLeader(false)
                .build();
        crewUserRepository.save(crewUser);
    }

    ////////////////////////////////////////////////////////////

    /* 3. 그룹 상세 보기 */
    // crewId에 해당하는 그룹 상세 조회
//    public CrewDetailResponse getCrewDetail(int crewId) {
//        Crew crew = crewRepository.findById(crewId)
//                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));
//
//        CrewDetailResponse crewDetailResponse = new CrewDetailResponse(
//            crew.getCrewId(),
//                crew.getCrewName(),
//                crew.getCrewStartDate(),
//                crew.getCrewEndDate(),
//                crew.getCrewDescription(),
//                crew.getCrewStyles(),
//                crew.getCrewMaxMembers(),
//                crew.getMountain().getMountainId(),
//                crew.getMountain().getMountainName(),
//                crew.getMountain().getMountainDescription(),
//                crew.getMountain().getMountainImg()
//        );
//
//        return crewDetailResponse;
//    }

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

    public Crew getCrewById(int crewId) {
        Optional<Crew> crew = crewRepository.findById(crewId);
        if (crew.isPresent()) {
            return crew.get();
        } else {
            throw new IllegalArgumentException("Crew not found");
        }
    }
}
