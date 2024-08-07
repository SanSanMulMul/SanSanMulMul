package com.sansantek.sansanmulmul.crew.service;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.crewuser.CrewUser;
import com.sansantek.sansanmulmul.crew.repository.CrewRepository;
import com.sansantek.sansanmulmul.crew.repository.request.CrewUserRepository;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrewLeaderService {

    /*
     * 1. 그룹 수정
     * 2. 그룹 삭제
     * 3. 그룹 방장 위임
     * */

    // repository
    private final CrewRepository crewRepository;
    private final UserRepository userRepository;
    private final CrewUserRepository crewUserRepository;

    // service

    /* 1. 그룹 수정 */


    /* 2. 그룹 삭제 */
    @Transactional
    public void deleteCrew(int crewId, String userProviderId) {
        //크루 확인
        Crew crew = crewRepository.findByCrewId(crewId)
                .orElseThrow(() -> new EntityNotFoundException("Crew not found"));
        //현재 사용자 확인
        User currentUser = userRepository.findByUserProviderId(userProviderId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자가 크루의 리더가 맞는지 확인
        if (!crew.getLeader().equals(currentUser)) {
            throw new RuntimeException("크루의 리더가 아닙니다.");
        }

        crewRepository.delete(crew);
    }


    /* 3. 그룹 방장 위임 */
    @Transactional
    public void delegateLeader(int crewId, String currentProviderId, int newLeaderId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new EntityNotFoundException("크루를 찾을 수 없습니다"));
        //현재 방장
        User currentLeader = userRepository.findByUserProviderId(currentProviderId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        //새 방장
        User newLeader = userRepository.findByUserId(newLeaderId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // CrewUser 테이블에서 현재 방장의 isLeader를 false로 변경
        CrewUser currentLeaderCrewUser = crewUserRepository.findByCrewAndUser(crew, currentLeader)
                .orElseThrow(() -> new EntityNotFoundException("CrewUser에서 현재 방장을 찾을 수 없습니다."));
        currentLeaderCrewUser.setLeader(false);
        crewUserRepository.save(currentLeaderCrewUser);

        // CrewUser 테이블에서 새 방장의 isLeader를 true로 변경
        CrewUser newLeaderCrewUser = crewUserRepository.findByCrewAndUser(crew, newLeader)
                .orElseThrow(() -> new EntityNotFoundException("CrewUser에서 새 방장 정보를 찾을 수 없습니다."));
        newLeaderCrewUser.setLeader(true);
        crewUserRepository.save(newLeaderCrewUser);

        // Crew 테이블의 leader 변경
        crew.changeLeader(newLeader);
        crewRepository.save(crew);
    }


}
