package com.sansantek.sansanmulmul.crew.service;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.repository.CrewRepository;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    /* 1. 그룹 수정 */


    /* 2. 그룹 삭제 */


    /* 3. 그룹 방장 위임 */
    public void updateCrewLeader(int crewId, int newLeaderId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new EntityNotFoundException("Crew not found"));
        User newLeader = userRepository.findById(newLeaderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        crew.changeLeader(newLeader);
        crewRepository.save(crew);
    }


}
