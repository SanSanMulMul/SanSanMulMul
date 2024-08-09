package com.sansantek.sansanmulmul.crew.repository.request;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.crewuser.CrewUser;
import com.sansantek.sansanmulmul.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewUserRepository extends JpaRepository<CrewUser, Long> {
    Optional<CrewUser> findByCrewAndUser(Crew crew, User user);

    List<CrewUser> findByCrew(Crew crew);
    //그룹(crewId)에 참여중인 user 수 가져오기
    int countByCrew_CrewId(int crewId);

    //현재 사용자(user)가 그룹(crew)에 참여중인지 여부
    boolean existsByCrewAndUser(Crew crew, User user);

    //내가 가입한 crew들 전부 가져오기
    List<CrewUser> findByUser(User user);
    //내가 가입한 crew들 중 종료여부에 따라 (crew의 isDone컬럼) 그룹들 가져오기
    List<CrewUser> findByUserAndCrew_CrewIsDone(User user, boolean isDone);
    int countByCrewCrewId(int crewId);
}
