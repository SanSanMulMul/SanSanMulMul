package com.sansantek.sansanmulmul.crew.controller;

import com.sansantek.sansanmulmul.crew.service.CrewLeaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
