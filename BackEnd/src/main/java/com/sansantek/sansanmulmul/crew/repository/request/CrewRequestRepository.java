package com.sansantek.sansanmulmul.crew.repository.request;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.domain.crewrequest.CrewRequest;
import com.sansantek.sansanmulmul.crew.domain.crewrequest.CrewRequestStatus;
import com.sansantek.sansanmulmul.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewRequestRepository extends JpaRepository<CrewRequest, Integer> {

    // 특정 크루와 사용자에 대한 가입 요청 조회
    Optional<CrewRequest> findByCrewAndUser(Crew crew, User user);

    // 특정 크루에 대한 모든 가입 요청 조회
    List<CrewRequest> findAllByCrew(Crew crew);

    // 특정 사용자의 모든 가입 요청 조회
    List<CrewRequest> findAllByUser(User user);

    // 특정 상태의 가입 요청 조회
    List<CrewRequest> findAllByCrewRequestStatus(CrewRequestStatus status);

    // 특정 크루와 상태에 대한 가입 요청 조회
    List<CrewRequest> findAllByCrewAndCrewRequestStatus(Crew crew, CrewRequestStatus status);

    // 특정 사용자와 상태에 대한 가입 요청 조회
    List<CrewRequest> findAllByUserAndCrewRequestStatus(User user, CrewRequestStatus status);

    // 특정 크루, 사용자, 상태에 대한 가입 요청 조회
    Optional<CrewRequest> findByCrewAndUserAndCrewRequestStatus(Crew crew, User user, CrewRequestStatus status);

    // 특정 크루와 사용자에 대한 가입 요청 존재 여부 확인
    boolean existsByCrewAndUser(Crew crew, User user);

    // 특정 크루, 사용자, 상태에 대한 가입 요청 존재 여부 확인
    boolean existsByCrewAndUserAndCrewRequestStatus(Crew crew, User user, CrewRequestStatus status);

}