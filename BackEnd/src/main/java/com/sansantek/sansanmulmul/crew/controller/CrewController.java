package com.sansantek.sansanmulmul.crew.controller;

import com.sansantek.sansanmulmul.crew.domain.Crew;
import com.sansantek.sansanmulmul.crew.dto.request.CrewRequest;
import com.sansantek.sansanmulmul.crew.dto.response.CrewDetailResponse;
import com.sansantek.sansanmulmul.crew.dto.response.CrewMyResponse;
import com.sansantek.sansanmulmul.crew.dto.response.CrewResponse;
import com.sansantek.sansanmulmul.crew.dto.response.CrewUserResponse;
import com.sansantek.sansanmulmul.crew.service.CrewService;
import com.sansantek.sansanmulmul.crew.service.style.CrewStyleService;
import com.sansantek.sansanmulmul.crew.service.request.CrewRequestService;
import com.sansantek.sansanmulmul.exception.auth.InvalidTokenException;
import com.sansantek.sansanmulmul.exception.style.GroupNotFoundException;
import com.sansantek.sansanmulmul.user.domain.User;
import com.sansantek.sansanmulmul.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/crew")
@Tag(name = "그룹 정보 컨트롤러", description = "그룹 정보관련 기능 수행")
public class CrewController {

    // service
    private final CrewService crewService;
    private final UserService userService;

    private final CrewStyleService crewStyleService;

    private final CrewRequestService crewRequestService;


    @GetMapping("/all")
    @Operation(summary = "그룹 정보 전체 조회", description = "그룹에 대한 정보를 전체 조회")
    public ResponseEntity<?> getAllCrews() {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            // 전체 그룹 가져오기
            List<CrewResponse> crewResponse = crewService.getAllCrews();

            return new ResponseEntity<>(crewResponse, status);

        } catch (GroupNotFoundException e) {
            status = HttpStatus.NOT_FOUND; // 404

            return new ResponseEntity<>(e.getMessage(), status);
        } catch (Exception e) {
            status = HttpStatus.BAD_REQUEST; // 400

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }

//    @GetMapping
//    @Operation(summary = "그룹 특정 등산 스타일 전체 조회", description = "해당 등산 스타일에 해당하는 그룹 전체 조회")
//    public ResponseEntity<?> getHikingStyles(@RequestParam("styleId") int styleId) {
////        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status = HttpStatus.ACCEPTED;
//
//        try {
//
//            // 특정 스타일에 해당하는 그룹 정보 조회
//            List<CrewResponse> crewStyleResponseList = crewStyleService.getCrewList(styleId);
//
//            // JSON으로 결과 전송
////            resultMap.put("crewStyleResponseList", crewStyleResponseList);
//
//            return new ResponseEntity<>(crewStyleResponseList, status);
//
//        } catch ( Exception e ) {
//            status = HttpStatus.BAD_REQUEST; // 400
//
//            return new ResponseEntity<>(e.getMessage(), status);
//        }
//    }

    @GetMapping("/detail")
    @Operation(summary = "그룹 상세 정보 조회", description = "해당 그룹에 대한 상세 정보를 조회")
    public ResponseEntity<?> getCrewDetail(@RequestParam("crewId") int crewId) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            CrewDetailResponse crewDetailResponse = crewService.getCrewDetail(crewId);

            return new ResponseEntity<>(crewDetailResponse, status);
        } catch (Exception e) {

            status = HttpStatus.BAD_REQUEST; // 400

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }

    @PostMapping
    @Operation(summary = "그룹 생성", description = "해당 사용자 그룹 생성")
    public ResponseEntity<?> createCrew
            (Authentication authentication,
             CrewRequest request) {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {

            // 토큰을 통해 userProvider 추출
            String userProviderId = authentication.getName();

            // 해당 사용자 가져오기
            User user = userService.getUser(userProviderId);

            // 해당 사용자 userId로 그룹 생성 - 방장
            crewService.addCrew(user.getUserId(), request);

            status = HttpStatus.CREATED; // 201

            return new ResponseEntity<>(status);

        } catch (InvalidTokenException e) {

            log.error("토큰 유효성 검사 실패: {}", e.getMessage());
            status = HttpStatus.UNAUTHORIZED; // 401

            return new ResponseEntity<>(e.getMessage(), status);
        } catch (Exception e) {

            log.error("그룹 생성 실패: {}", e.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }

    @GetMapping("/member/{crewId}")
    public ResponseEntity<?> getCrewMembers(@PathVariable int crewId) {
        try {
            List<CrewUserResponse> members = crewRequestService.getCrewMembers(crewId);
            return ResponseEntity.ok(members);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "진행 중인 그룹 조회", description = "회원의 진행 중인 그룹을 조회합니다.")
    @GetMapping("/ing")
    public ResponseEntity<?> getingCrews(Authentication authentication) {
        try {
            String userProviderId = authentication.getName();
            User user = userService.getUser(userProviderId);
            List<Crew> ongoingCrews = crewService.getingCrews(user);
            List<CrewMyResponse> response = ongoingCrews.stream()
                    .map(crew -> CrewMyResponse.from(crew, crewService.getCurrentMemberCount(crew.getCrewId())))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "종료된 그룹 조회", description = "회원의 종료된 그룹을 조회합니다.")
    @GetMapping("/complete")
    public ResponseEntity<?> getCompletedCrews(Authentication authentication) {
        try {
            String userProviderId = authentication.getName();
            User user = userService.getUser(userProviderId);
            List<Crew> completedCrews = crewService.getCompletedCrews(user);
            List<CrewMyResponse> response = completedCrews.stream()
                    .map(crew -> CrewMyResponse.from(crew, crewService.getCurrentMemberCount(crew.getCrewId())))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}