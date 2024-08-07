package com.sansantek.sansanmulmul.crew.controller;

import com.amazonaws.Response;
import com.sansantek.sansanmulmul.crew.dto.request.CrewUpdateRequest;
import com.sansantek.sansanmulmul.crew.service.CrewLeaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/crew")
@Tag(name = "그룹 방장 컨트롤러", description = "그룹 방장의 기능 수행")
public class CrewLeaderController {

    /*
    * 1. 그룹 수정
    * 2. 그룹 삭제
    * 3. 그룹 방장 위임
    * */

    //service
    private final CrewLeaderService crewLeaderService;

    /* 1. 그룹 수정 */
    @PutMapping("/{crewId}")
    @Operation(summary = "그룹 수정", description = "방장의 그룹 수정(산, 코스) 기능")
    public ResponseEntity<?> updateCrew(@PathVariable int crewId,
                                        Authentication authentication,
                                        @RequestBody CrewUpdateRequest crewUpdateRequest) {
        try {
            String leaderProviderId = authentication.getName();
            crewLeaderService.updateCrew(crewId, leaderProviderId, crewUpdateRequest);
            return ResponseEntity.ok().body("그룹이 수정되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /* 2. 그룹 삭제 */
    @DeleteMapping("/{crewId}")
    @Operation(summary = "그룹 삭제", description = "방장의 그룹 삭제 기능")
    public ResponseEntity<?> deleteCrew(@PathVariable int crewId,
                                     Authentication authentication) {
        try {
            String leaderProviderId = authentication.getName();
            crewLeaderService.deleteCrew(crewId, leaderProviderId);
            return ResponseEntity.ok().body("그룹이 삭제되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /* 3. 그룹 방장 위임 */
    @PutMapping("/{crewId}/leader")
    @Operation(summary = "그룹 방장 위임", description = "그룹 방장 위임 기능")
    public ResponseEntity<?> delegateLeader(@PathVariable int crewId,
                                            Authentication authentication,
                                            @RequestParam int nextLeaderId) {
        try {
            String currentProviderId = authentication.getName();
            crewLeaderService.delegateLeader(crewId, currentProviderId, nextLeaderId);
            return ResponseEntity.ok().body("방장이 위임되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
