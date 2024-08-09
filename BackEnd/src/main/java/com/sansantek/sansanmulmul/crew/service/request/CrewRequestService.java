package com.sansantek.sansanmulmul.crew.service.request;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.crewrequest.CrewRequest;
import com.sansantek.sansanmulmul.crew.domain.crewrequest.CrewRequestStatus;
import com.sansantek.sansanmulmul.crew.domain.crewuser.CrewUser;
import com.sansantek.sansanmulmul.crew.dto.response.CrewRequestResponse;
import com.sansantek.sansanmulmul.crew.dto.response.crewdetail.CrewUserResponse;
import com.sansantek.sansanmulmul.crew.repository.CrewRepository;
import com.sansantek.sansanmulmul.crew.repository.request.CrewRequestRepository;
import com.sansantek.sansanmulmul.crew.repository.request.CrewUserRepository;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrewRequestService {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private UserRepository userRepository;

    private final CrewRequestRepository crewRequestRepository;
    @Autowired
    private CrewUserRepository crewUserRepository;

    public CrewRequestService(CrewRequestRepository crewRequestRepository) {
        this.crewRequestRepository = crewRequestRepository;
    }

    @Transactional
    public CrewRequest requestJoinCrew(int crewId, String userProviderId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("크루를 찾을 수 없습니다."));

        User user = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자가 크루의 리더인지 확인
        if (crew.getLeader().equals(user)) {
            throw new RuntimeException("크루의 리더입니다.");
        }

        // 이미 요청이 존재하는지 확인
        if (crewRequestRepository.existsByCrewAndUser(crew, user)) {
            throw new RuntimeException("이미 가입 요청이 존재합니다.");
        }

        CrewRequest crewRequest = new CrewRequest();
        crewRequest.setCrew(crew);
        crewRequest.setUser(user);
        crewRequest.setCrewRequestStatus(CrewRequestStatus.R);

        return crewRequestRepository.save(crewRequest);
    }

    @Transactional
    public CrewRequest processJoinRequest(int requestId, CrewRequestStatus status, String userProviderId) {
        CrewRequest crewRequest = crewRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("가입 요청을 찾을 수 없습니다."));

        User leader = crewRequest.getCrew().getLeader();

        if (!leader.getUserProviderId().equals(userProviderId)) {
            throw new RuntimeException("크루 리더만 가입 요청을 처리할 수 있습니다.");
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
                .orElseThrow(() -> new RuntimeException("크루를 찾을 수 없습니다."));

        User leader = crew.getLeader();
        if (!leader.getUserProviderId().equals(leaderProviderId)) {
            throw new RuntimeException("크루 리더만 사용자를 강퇴할 수 있습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        CrewUser crewUser = crewUserRepository.findByCrewAndUser(crew, user)
                .orElseThrow(() -> new RuntimeException("크루 회원을 찾을 수 없습니다."));

        crewUserRepository.delete(crewUser);
    }

    @Transactional
    public List<CrewUserResponse> getCrewMembers(int crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("크루를 찾을 수 없습니다."));

        List<CrewUser> crewUsers = crewUserRepository.findByCrew(crew);
        List<CrewUserResponse> userResponses = new ArrayList<>();
        for (CrewUser crewUser : crewUsers) {
            User user = crewUser.getUser();
            CrewUserResponse userResponse = new CrewUserResponse(
                    user.getUserId(),
                    user.getUserName(),
                    user.getUserNickname(),
                    user.getUserGender().toString(),
                    user.getUserProfileImg(),
                    user.getUserStaticBadge()
            );
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
                .orElseThrow(() -> new RuntimeException("크루를 찾을 수 없습니다."));

        User leader = crew.getLeader();
        if (!leader.getUserProviderId().equals(userProviderId)) {
            throw new RuntimeException("리더만 이 정보를 볼 수 있습니다.");
        }

        List<CrewRequest> requests = crewRequestRepository.findByCrewAndCrewRequestStatus(crew, CrewRequestStatus.R);
        List<CrewRequestResponse> responseList = new ArrayList<>();

        for (CrewRequest request : requests) {
            CrewRequestResponse response = new CrewRequestResponse(
                    request.getRequestId(),
                    request.getUser().getUserName(),
                    request.getUser().getUserNickname(),
                    request.getCrewRequestStatus().name()
            );
            responseList.add(response);
        }

        return responseList;
    }
}