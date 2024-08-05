package com.sansantek.sansanmulmul.crew.controller;


import com.sansantek.sansanmulmul.crew.domain.crewrequest.CrewRequest;
import com.sansantek.sansanmulmul.crew.domain.crewrequest.CrewRequestStatus;
import com.sansantek.sansanmulmul.crew.service.request.CrewRequestService;
import com.sansantek.sansanmulmul.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crew")
public class CrewRequestController {

    private final CrewRequestService crewRequestService;

    public CrewRequestController(CrewRequestService crewRequestService) {
        this.crewRequestService = crewRequestService;
    }

    @PostMapping("/{crewId}/join")
    @Operation(summary = "그룹 가입요청", description = "그룹에 대한 가입요청")
    public ResponseEntity<?> requestJoinCrew(@PathVariable int crewId,
                                             Authentication authentication) {
        try {
            String userProviderId = authentication.getName();
            CrewRequest crewRequest = crewRequestService.requestJoinCrew(crewId, userProviderId);
            return ResponseEntity.ok().body("크루 가입 요청이 성공적으로 전송되었습니다. 요청 ID: " + crewRequest.getRequestId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{requestId}/accept")
    @Operation(summary = "그룹 가입요청승인", description = "그룹에 대한 가입요청승인")
    public ResponseEntity<?> acceptJoinRequest(@PathVariable int requestId,
                                               Authentication authentication) {
        try {
            String leaderProviderId = authentication.getName();
            CrewRequest crewRequest = crewRequestService.processJoinRequest(requestId, CrewRequestStatus.A, leaderProviderId);
            return ResponseEntity.ok().body("크루 가입 요청이 승인되었습니다. 요청 ID: " + crewRequest.getRequestId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{requestId}/refuse")
    @Operation(summary = "그룹 가입요청거절", description = "그룹에 대한 가입요청거절")
    public ResponseEntity<?> refuseJoinRequest(@PathVariable int requestId,
                                               Authentication authentication) {
        try {
            String leaderProviderId = authentication.getName();
            CrewRequest crewRequest = crewRequestService.processJoinRequest(requestId, CrewRequestStatus.D, leaderProviderId);
            return ResponseEntity.ok().body("크루 가입 요청이 거절되었습니다. 요청 ID: " + crewRequest.getRequestId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{crewId}/{userId}")
    @Operation(summary = "그룹 회원강퇴", description = "그룹회원 강퇴기능")
    public ResponseEntity<?> OutUser(@PathVariable int crewId,
                                         @PathVariable int userId,
                                         Authentication authentication) {
        try {
            String leaderProviderId = authentication.getName();
            crewRequestService.OutUser(crewId, userId, leaderProviderId);
            return ResponseEntity.ok().body("사용자가 크루에서 강퇴되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

