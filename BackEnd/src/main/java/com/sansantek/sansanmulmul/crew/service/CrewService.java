package com.sansantek.sansanmulmul.crew.service;

import com.sansantek.sansanmulmul.common.ApiResponse;
import com.sansantek.sansanmulmul.common.service.S3Service;
import com.sansantek.sansanmulmul.crew.domain.Crew;

import com.sansantek.sansanmulmul.crew.domain.crewgallery.CrewGallery;
import com.sansantek.sansanmulmul.crew.domain.crewuser.CrewUser;
import com.sansantek.sansanmulmul.crew.domain.style.CrewHikingStyle;
import com.sansantek.sansanmulmul.crew.dto.request.CrewCreateRequest;
import com.sansantek.sansanmulmul.crew.dto.response.CrewGalleryResponse;
import com.sansantek.sansanmulmul.crew.dto.response.crewdetail.CrewDetailCommonResponse;
import com.sansantek.sansanmulmul.crew.dto.response.crewdetail.CrewDetailResponse;
import com.sansantek.sansanmulmul.crew.dto.response.CrewResponse;
import com.sansantek.sansanmulmul.crew.dto.response.crewdetail.CrewHikingDetailResponse;
import com.sansantek.sansanmulmul.crew.dto.response.crewdetail.CrewUserResponse;
import com.sansantek.sansanmulmul.crew.repository.CrewGalleryRepository;
import com.sansantek.sansanmulmul.crew.repository.CrewRepository;
import com.sansantek.sansanmulmul.crew.repository.CrewHikingStyleRepository;
import com.sansantek.sansanmulmul.crew.repository.request.CrewUserRepository;
import com.sansantek.sansanmulmul.exception.auth.UserNotFoundException;
import com.sansantek.sansanmulmul.exception.user.UserDeletionException;
import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.domain.course.Course;
import com.sansantek.sansanmulmul.mountain.repository.MountainRepository;
import com.sansantek.sansanmulmul.mountain.repository.course.CourseRepository;
import com.sansantek.sansanmulmul.mountain.service.course.CourseService;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.domain.style.HikingStyle;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import com.sansantek.sansanmulmul.user.repository.style.HikingStyleRepository;
import com.sansantek.sansanmulmul.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final CrewGalleryRepository crewGalleryRepository;

    // service
    private final UserService userService;
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final S3Service s3Service;

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
                // 5. 방장 정보
                User leader = crew.getLeader();
                CrewResponse cr = CrewResponse.builder()
                        .crewId(crew.getCrewId())
                        .crewName(crew.getCrewName())
                        .mountainName(crew.getMountain().getMountainName())
                        .crewStartDate(crew.getCrewStartDate())
                        .crewEndDate(crew.getCrewEndDate())
                        .crewMaxMembers(crew.getCrewMaxMembers())
                        .crewCurrentMembers(currentMember)
                        .isUserJoined(isUserJoined)
                        .mountainImg(crew.getMountain().getMountainImg())
                        .crewMinAge(crew.getCrewMinAge())
                        .crewMaxAge(crew.getCrewMaxAge())
                        .crewGender(crew.getCrewGender())
                        .crewStyles(styles)
                        .userStaticBadge(leader.getUserStaticBadge())
                        .userNickname(leader.getUserNickname())
                        .userProfileImg(leader.getUserProfileImg())
                        .build();

                crews.add(cr);
            }
        }

        return crews;
    }

    // 그룹 {스타일 } 검색 시 그룹 조회
/*    @Transactional(readOnly = true)
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
    }*/

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

    @Transactional
    public void updateCrewUser(Crew crew, int userId) {
        //현재 사용자 확인
        User leader = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        //그룹 생성자 = 방장
        CrewUser crewUser = CrewUser.builder()
                .crew(crew)
                .user(leader)
                .isLeader(true)
                .build();
        crewUserRepository.save(crewUser);
    }

    ////////////////////////////////////////////////////////////

    /* 3. 그룹 상세 보기 */
    // (1) 상단 공통된 부분 (유저 정보 isLeader 포함)
    public CrewDetailCommonResponse getCrewDetailCommon(int crewId, int userId) {
        // crewId에 해당하는 그룹 상세 조회
        // 1. 해당 crew 가져옴
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));

        // 2. 현재 그룹에 속한 인원 수 가져옴
        int currentMember = crewUserRepository.countByCrew_CrewId(crew.getCrewId());
        // 3. 현재 사용자(유저)가 이 그룹의 리더인지 확인
        boolean isLeader = crewRepository.existsByCrewIdAndLeader_UserId(crewId, userId);

        CrewDetailCommonResponse crewDetailCommonResponse = CrewDetailCommonResponse.builder()
                .crewId(crewId)
                .crewName(crew.getCrewName())
                .crewStartDate(crew.getCrewStartDate())
                .crewEndDate(crew.getCrewEndDate())
                .crewCurrentMembers(currentMember)
                .crewMaxMembers(crew.getCrewMaxMembers())
                .mountainImg(crew.getMountain().getMountainImg())
                .isLeader(isLeader)
                .build();

        return crewDetailCommonResponse;
    }

    // (2) [탭1] 그룹 정보 + 그룹 내 속하는 멤버들
    public CrewDetailResponse getCrewDetailCrewInfo(int crewId) {
        // crewId에 해당하는 그룹 상세 조회
        // 1. 해당 crew 가져옴
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));
        // 2. 해당 crew의 hikingStyle들 Integer List로
        List<Integer> styles = new ArrayList<>();
        for (int i = 0; i < crew.getCrewStyles().size(); i++) {
            int styleId = crew.getCrewStyles().get(i).getStyle().getHikingStylesId();
            styles.add(styleId);
        }
        // 3. 그룹 내 속하는 멤버들
        List<CrewUser> crewUsers = crewUserRepository.findByCrew(crew);
        List<CrewUserResponse> crewUserResponses = new ArrayList<>();
        for (CrewUser crewUser : crewUsers) {
            User user = crewUser.getUser();
            boolean isLeader = crewRepository.existsByCrewIdAndLeader_UserId(crewId, user.getUserId()); //방장여부
            CrewUserResponse userResponse = CrewUserResponse.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .userNickname(user.getUserNickname())
                    .userGender(user.getUserGender().toString())
                    .userProfileImg(user.getUserProfileImg())
                    .userStaticBadge(user.getUserStaticBadge())
                    .isLeader(crewUser.isLeader())
                    .build();
            crewUserResponses.add(userResponse);
        }
        CrewDetailResponse crewDetailResponse = CrewDetailResponse.builder()
                .crewDescription(crew.getCrewDescription())
                .crewHikingStyles(styles)
                .members(crewUserResponses)
                .build();

        return crewDetailResponse;
    }

    // (3) [탭2] 등산 정보
    public CrewHikingDetailResponse getCrewDetailHikingInfo(int crewId) {
        // crewId에 해당하는 그룹 상세 조회
        // 1. 해당 crew 가져옴
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));
        // 2. 산
        Mountain mountain = crew.getMountain();
        // 3. 상행 코스, 하행 코스
        Course upCourse = crew.getUpCourse();
        Course downCourse = crew.getDownCourse();
        // 4. 상행 코스, 하행 코스 - 좌표모음
        // TrackPaths 정보 조회
        Map<String, Object> upCourseDetail = courseService.getCourseDetail(upCourse.getMountain().getMountainId(), upCourse.getCourseId());
        Map<String, Object> downCourseDetail = courseService.getCourseDetail(downCourse.getMountain().getMountainId(), downCourse.getCourseId());

        // TrackPaths만 가져오기
        List<Map<String, Object>> upTrackPaths = (List<Map<String, Object>>) upCourseDetail.get("tracks");
        List<Map<String, Object>> downTrackPaths = (List<Map<String, Object>>) downCourseDetail.get("tracks");


        CrewHikingDetailResponse crewHikingDetailResponse = CrewHikingDetailResponse.builder()
                .mountainId(mountain.getMountainId())
                .mountainName(mountain.getMountainName())
                .mountainDescription(mountain.getMountainDescription())
                .mountainLat(mountain.getMountainLat())
                .mountainLon(mountain.getMountainLon())
                .upCourseId(upCourse.getCourseId())
                .upCourseName(upCourse.getCourseName())
                .upCourseLevel(upCourse.getCourseLevel())
                .upCoursetime(upCourse.getCourseUptime())
                .upCourseLength(upCourse.getCourseLength())
                .downCourseId(downCourse.getCourseId())
                .downCourseName(downCourse.getCourseName())
                .downCourseLevel(downCourse.getCourseLevel())
                .downCoursetime(downCourse.getCourseDowntime())
                .downCourseLength(downCourse.getCourseLength())
                .upCourseTrackPaths(upTrackPaths)
                .downCourseTrackPaths(downTrackPaths)
                .build();

        return crewHikingDetailResponse;
    }

    /* 4. [탭3] 그룹 갤러리 */
    // 4-1. 이미지 업로드 (C)
    @Transactional
    public boolean uploadImg(int crewId, User user, MultipartFile image)  throws IOException {

        // 1. 해당 crew 가져옴
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));
        // user가 해당 crew에 가입되어있지 않은 경우 에러 처리
        boolean isMember = crewUserRepository.existsByCrewAndUser(crew, user);
        if (!isMember) {
//            return false;
            throw new RuntimeException("해당 그룹에 가입되어 있지 않습니다.");
        }
        // 2. 이미지를 s3에 업로드함
        String imgUrl = "";
        if (image != null) {
            imgUrl = s3Service.uploadS3(image, "group/"+Integer.toString(user.getUserId())+Integer.toString(crewId));
        }

        // 3. 이미지 객체 하나 생성
        CrewGallery crewPic = CrewGallery.builder()
                .crew(crew)
                .user(user)
                .imgUrl(imgUrl)
                .imgCreatedAt(LocalDateTime.now())
                .build();

        // 4. crewGallery 레파지토리에 이미지 객체 저장
        CrewGallery savedCrewPic = crewGalleryRepository.save(crewPic);

        // 5. 저장이 성공적으로 이루어졌는지 확인
        if (savedCrewPic == null || savedCrewPic.getImgUrl() == null) {
            return false;
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DB에 이미지 저장에 실패했습니다.");
        }
        return true;
    }

    // 4-2. 갤러리 전부 가져오기 (R)
    public List<CrewGalleryResponse> getCrewDetailGallery(int crewId, User user) {
        // 1. 해당 crew 가져옴
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));
        // user가 해당 crew에 가입되어있지 않은 경우 에러 처리
        boolean isMember = crewUserRepository.existsByCrewAndUser(crew, user);
        if (!isMember) {
            throw new RuntimeException("해당 그룹에 가입되어 있지 않습니다.");
        }

        // 2. crewId에 해당하는 모든 CrewGallery 가져오기
        List<CrewGallery> crewGalleries = crewGalleryRepository.findByCrew(crew);

        // 3. 응답 dto 리스트 생성
        List<CrewGalleryResponse> responses = new ArrayList<>();

        for (CrewGallery gallery : crewGalleries) {
            boolean isOwner = gallery.getUser().equals(user);

            CrewGalleryResponse response = CrewGalleryResponse.builder()
                    .picId(gallery.getPictureId())
                    .userNickname(gallery.getUser().getUserNickname())
                    .userProfileImg(gallery.getUser().getUserProfileImg())
                    .isOwner(isOwner)
                    .imgUrl(gallery.getImgUrl())
                    .createdAt(gallery.getImgCreatedAt())
                    .build();

            responses.add(response);
        }

        return responses;
    }

    // 4-3. 갤러리에서 본인이 올린 사진 지우기 (D)
    @Transactional
    public boolean deleteImg(int crewId, User user, int picId) throws IOException{
        try {
            // 1. 해당 crew 가져옴
            Crew crew = crewRepository.findById(crewId)
                    .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));
            // user가 해당 crew에 가입되어있지 않은 경우 에러 처리
            boolean isMember = crewUserRepository.existsByCrewAndUser(crew, user);
            if (!isMember) {
//            return false;
                throw new RuntimeException("해당 그룹에 가입되어 있지 않습니다.");
            }
            // 지울 img가 현재 사용자가 올린 것이 아니라면 에러 처리
            boolean isOwner = crewGalleryRepository.findByPictureId(picId).getUser().equals(user);
            if (!isOwner) {
//            return false;
                throw new RuntimeException("해당 사진을 올린 유저가 아닙니다.");
            }

            // 2. 지울 imgurl
            String imgUrl = crewGalleryRepository.findByPictureId(picId).getImgUrl();

            // 3-1. S3에서 이미지 삭제
            s3Service.deleteS3(imgUrl); //s3에서 이미지 삭제

            // 3-2. DB에서 삭제
            crewGalleryRepository.deleteById(picId);
            return true;
        } catch (Exception e) {

            throw new UserDeletionException("이미지 삭제 실패 | 이미지id :  " + picId, e);

        }

    }



    ////////////////////////////////////////////////////////////
    /* [그룹 목록] // 내거 보여주는 목록 */

    /* 2. '내' 진행 중 그룹 */
    public List<CrewResponse> getMyOnGoingCrews(User user) {
        List<CrewUser> myCrews =  crewUserRepository.findByUser(user);

        List<CrewResponse> myResponses = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now(); //현재시간
        for (CrewUser crewUser : myCrews) {
            Crew myCrew = crewUser.getCrew(); //크루
            // 1. 현재날짜 이후것 부터 가져와야함 (CrewStartDate사용)
            if (myCrew.getCrewEndDate().isAfter(now)) {
                // 2. 현재 그룹에 속한 인원 수 가져옴
                int currentMember = crewUserRepository.countByCrew_CrewId(myCrew.getCrewId());
                // 3. 현재 사용자(유저)가 이 그룹에 참여하고있는지 확인
                boolean isUserJoined = crewUserRepository.existsByCrewAndUser(myCrew, user);
                // 4. 그룹 스타일 Integer List로
                List<Integer> styles = new ArrayList<>();
                for (int i = 0; i < myCrew.getCrewStyles().size(); i++) {
                    int styleId = myCrew.getCrewStyles().get(i).getStyle().getHikingStylesId();
                    styles.add(styleId);
                }
                // 5. 방장 정보
                User leader = myCrew.getLeader(); // 크루의 방장 정보
                // 6. <응답 객체 생성>
                CrewResponse response = CrewResponse.builder()
                        .crewId(myCrew.getCrewId())
                        .crewName(myCrew.getCrewName())
                        .mountainName(myCrew.getMountain().getMountainName())
                        .crewStartDate(myCrew.getCrewStartDate())
                        .crewEndDate(myCrew.getCrewEndDate())
                        .crewMaxMembers(myCrew.getCrewMaxMembers())
                        .crewCurrentMembers(currentMember)
                        .isUserJoined(isUserJoined)
                        .mountainImg(myCrew.getMountain().getMountainImg())
                        .crewMinAge(myCrew.getCrewMinAge())
                        .crewMaxAge(myCrew.getCrewMaxAge())
                        .crewGender(myCrew.getCrewGender())
                        .crewStyles(styles)
                        .userStaticBadge(leader.getUserStaticBadge())
                        .userNickname(leader.getUserNickname())
                        .userProfileImg(leader.getUserProfileImg())
                        .build();

                // 6. 응답에 추가
                myResponses.add(response);
            }

        }
        return myResponses;
    }


    /* 3. '내' 완료된 그룹 */
    public List<CrewResponse> getMyCompletedCrews(User user) {
        List<CrewUser> myCrews =  crewUserRepository.findByUser(user);

        List<CrewResponse> myResponses = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now(); //현재시간
        for (CrewUser crewUser : myCrews) {
            Crew myCrew = crewUser.getCrew(); //크루
            // 1. 현재날짜 이후것 부터 가져와야함 (CrewStartDate사용)
            if (myCrew.getCrewEndDate().isBefore(now)) {
                // 2. 현재 그룹에 속한 인원 수 가져옴
                int currentMember = crewUserRepository.countByCrew_CrewId(myCrew.getCrewId());
                // 3. 현재 사용자(유저)가 이 그룹에 참여하고있는지 확인
                boolean isUserJoined = crewUserRepository.existsByCrewAndUser(myCrew, user);
                // 4. 그룹 스타일 Integer List로
                List<Integer> styles = new ArrayList<>();
                for (int i = 0; i < myCrew.getCrewStyles().size(); i++) {
                    int styleId = myCrew.getCrewStyles().get(i).getStyle().getHikingStylesId();
                    styles.add(styleId);
                }
                // 5. 방장 정보
                User leader = myCrew.getLeader(); // 크루의 방장 정보
                // 6. <응답 객체 생성>
                CrewResponse response = CrewResponse.builder()
                        .crewId(myCrew.getCrewId())
                        .crewName(myCrew.getCrewName())
                        .mountainName(myCrew.getMountain().getMountainName())
                        .crewStartDate(myCrew.getCrewStartDate())
                        .crewEndDate(myCrew.getCrewEndDate())
                        .crewMaxMembers(myCrew.getCrewMaxMembers())
                        .crewCurrentMembers(currentMember)
                        .isUserJoined(isUserJoined)
                        .mountainImg(myCrew.getMountain().getMountainImg())
                        .crewMinAge(myCrew.getCrewMinAge())
                        .crewMaxAge(myCrew.getCrewMaxAge())
                        .crewGender(myCrew.getCrewGender())
                        .crewStyles(styles)
                        .userStaticBadge(leader.getUserStaticBadge())
                        .userNickname(leader.getUserNickname())
                        .userProfileImg(leader.getUserProfileImg())
                        .build();

                // 6. 응답에 추가
                myResponses.add(response);
            }

        }
        return myResponses;
    }



        /////////////////////////////////////////////
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
    // 사용자가 그룹에 가입되어 있는지 확인
    public boolean isUserInCrew(String userProviderId, int crewId) {
        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));
        return crewUserRepository.existsByCrewAndUser(crew, user);
    }

}
