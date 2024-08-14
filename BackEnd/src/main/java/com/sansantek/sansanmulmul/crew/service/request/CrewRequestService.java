package com.sansantek.sansanmulmul.crew.service.request;

import com.sansantek.sansanmulmul.common.util.FcmMessage;
import com.sansantek.sansanmulmul.common.util.FcmType;
import com.sansantek.sansanmulmul.common.util.FcmUtil;
import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.crewalarm.CrewAlarm;
import com.sansantek.sansanmulmul.crew.domain.crewrequest.CrewRequest;
import com.sansantek.sansanmulmul.crew.domain.crewrequest.CrewRequestStatus;
import com.sansantek.sansanmulmul.crew.domain.crewuser.CrewUser;
import com.sansantek.sansanmulmul.crew.dto.response.CrewRequestResponse;
import com.sansantek.sansanmulmul.crew.dto.response.crewdetail.CrewUserResponse;
import com.sansantek.sansanmulmul.crew.repository.CrewAlarmRepository;
import com.sansantek.sansanmulmul.crew.repository.CrewRepository;
import com.sansantek.sansanmulmul.crew.repository.request.CrewRequestRepository;
import com.sansantek.sansanmulmul.crew.repository.request.CrewUserRepository;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrewRequestService {

    private final CrewRepository crewRepository;
    private final UserRepository userRepository;
    private final CrewRequestRepository crewRequestRepository;
    private final CrewUserRepository crewUserRepository;
    private final CrewAlarmRepository crewAlarmRepository;
    private final FcmUtil fcmUtil;


    @Transactional
    public CrewRequest requestJoinCrew(int crewId, String userProviderId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("그룹를 찾을 수 없습니다."));

        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new RuntimeException("회원를 찾을 수 없습니다."));

        // 사용자가 크루의 리더인지 확인
        if (crew.getLeader().equals(user)) {
            throw new RuntimeException("그룹의 방장입니다.");
        }

        // 이미 요청이 존재하는지 확인
        if (crewRequestRepository.existsByCrewAndUser(crew, user)) {
            throw new RuntimeException("이미 가입 요청이 존재합니다.");
        }

        // 그룹의 최대 인원 수 초과 시 가입 못한다고 에러 던지기


        CrewRequest crewRequest = new CrewRequest();
        crewRequest.setCrew(crew);
        crewRequest.setUser(user);
        crewRequest.setCrewRequestStatus(CrewRequestStatus.R);

        // 가입요청 FCM알림
        // FcmDTO 생성
        String title = fcmUtil.makeFcmTitle(
                crew.getCrewName(), FcmType.JOINREQUEST.getType()
        );
        String body = fcmUtil.makeJoinRequestBody(
                user.getUserNickname(), crew.getCrewName()
        );
        FcmMessage.FcmDTO fcmDTO = fcmUtil.makeFcmDTO(title, body);
        // FCM발송
        fcmSendtoCrew(crew, fcmDTO);

        // 가입 요청 알림 : 알람 테이블 update //
        // 알람 객체 하나 생성
        String alarmtitle = "가입 요청";
        String alarmbody = user.getUserNickname() + " 님이 그룹 가입을 요청했습니다! 멤버 목록에서 수락 또는 거절할 수 있습니다" ;
        CrewAlarm alarm = CrewAlarm.builder()
                .crew(crew)
                .alarmTitle(alarmtitle)
                .alarmBody(alarmbody)
                .alarmCreatedAt(LocalDateTime.now())
                .build();

        //CrewAlarm 레파지토리에 알림 객체 저장
        crewAlarmRepository.save(alarm);

        return crewRequestRepository.save(crewRequest);
    }

    @Transactional
    public CrewRequest processJoinRequest(int requestId, CrewRequestStatus status, String userProviderId) {
        CrewRequest crewRequest = crewRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("가입 요청을 찾을 수 없습니다."));

        User leader = crewRequest.getCrew().getLeader();

        if (!leader.getUserProviderId().equals(userProviderId)) {
            throw new RuntimeException("그룹 방장만 가입 요청을 처리할 수 있습니다.");
        }

        crewRequest.setCrewRequestStatus(status);
        crewRequestRepository.save(crewRequest);

        // 가입 요청이 승인된 경우 CrewUser에 추가
        if (status == CrewRequestStatus.A) {
            CrewUser crewUser = CrewUser.builder()
                    .crew(crewRequest.getCrew())
                    .user(crewRequest.getUser())
                    .isLeader(false)
                    .build();
            crewUserRepository.save(crewUser);
        }

        return crewRequest;
    }

    @Transactional
    public void OutUser(int crewId, int userId, String leaderProviderId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        User leader = crew.getLeader();
        if (!leader.getUserProviderId().equals(leaderProviderId)) {
            throw new RuntimeException("그룹 방장만 사용자를 강퇴할 수 있습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        CrewUser crewUser = crewUserRepository.findByCrewAndUser(crew, user)
                .orElseThrow(() -> new RuntimeException("그룹 회원을 찾을 수 없습니다."));

        crewUserRepository.delete(crewUser);
    }

    @Transactional
    public List<CrewUserResponse> getCrewMembers(int crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        List<CrewUser> crewUsers = crewUserRepository.findByCrew(crew);
        List<CrewUserResponse> userResponses = new ArrayList<>();
        for (CrewUser crewUser : crewUsers) {
            User user = crewUser.getUser();
            CrewUserResponse userResponse = CrewUserResponse.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .userNickname(user.getUserNickname())
                    .userGender(user.getUserGender().toString())
                    .userProfileImg(user.getUserProfileImg())
                    .userStaticBadge(user.getUserStaticBadge())
                    .build();
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    public List<CrewUser> getCrewMembersInfo(int crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("크루를 찾을 수 없습니다."));

        List<CrewUser> crewUsers = crewUserRepository.findByCrew(crew);

        return crewUsers;
    }

    @Transactional
    public List<CrewRequestResponse> getCrewRequests(int crewId, String userProviderId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        User leader = crew.getLeader();
        if (!leader.getUserProviderId().equals(userProviderId)) {
            throw new RuntimeException("방장만 이 정보를 볼 수 있습니다.");
        }

        List<CrewRequest> requests = crewRequestRepository.findByCrewAndCrewRequestStatus(crew, CrewRequestStatus.R);
        List<CrewRequestResponse> responseList = new ArrayList<>();

        for (CrewRequest request : requests) {
            User user = request.getUser();
            CrewRequestResponse response = new CrewRequestResponse(
                    request.getRequestId(),
                    user.getUserName(),
                    user.getUserNickname(),
                    request.getCrewRequestStatus().name(),
                    user.getUserGender().toString(),
                    user.getUserProfileImg(),
                    user.getUserStaticBadge()
            );
            responseList.add(response);
        }

        return responseList;

    }
    @Transactional
    public void exitCrew(int crewId, int userId, String userProviderId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 리더는 방을 나갈 수 없도록 체크
        if (crew.getLeader().getUserProviderId().equals(userProviderId)) {
            throw new RuntimeException("방장은 방을 나갈 수 없습니다. 방장을 위임하세요");
        }

        CrewUser crewUser = crewUserRepository.findByCrewAndUser(crew, user)
                .orElseThrow(() -> new RuntimeException("그룹 회원을 찾을 수 없습니다."));

        crewUserRepository.delete(crewUser);
    }

    // 그룹 내 회원들에게 전체 알림
    public void fcmSendtoCrew(Crew crew, FcmMessage.FcmDTO fcmDTO){

        List<CrewUser> crewUsers = crewUserRepository.findByCrew(crew);// 그룹 내 속하는 멤버들

        fcmUtil.multiFcmSend(
                crewUsers.stream()
                        .map(CrewUser::getUser)
                        .toList(),
                fcmDTO
        );
    }
}
