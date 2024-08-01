package com.sansantek.sansanmulmul.group.controller;

import com.sansantek.sansanmulmul.group.dto.response.GroupStyleResponse;
import com.sansantek.sansanmulmul.group.service.GroupService;
import com.sansantek.sansanmulmul.group.service.style.GroupStyleService;
import com.sansantek.sansanmulmul.user.service.style.UserStyleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/group/style")
@Tag(name = "회원 정보 컨트롤러", description = "회원 정보관련 기능 수행")
public class GroupStyleController {

    // service
    private final GroupStyleService groupStyleService;

    @GetMapping
    @Operation(summary = "특정 등산 스타일 조회", description = "해당 등산 스타일에 해당하는 그룹 조회")
    public ResponseEntity<?> getHikingStyles(@RequestParam("styleId") int styleId) {
//        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {

            // 특정 스타일에 해당하는 그룹 정보 조회
            List<GroupStyleResponse> groupStyleResponseList = groupStyleService.getGroupList(styleId);

            // JSON으로 결과 전송
//            resultMap.put("groupStyleResponseList", groupStyleResponseList);

            return new ResponseEntity<>(groupStyleResponseList, status);

        } catch ( Exception e ) {
            status = HttpStatus.BAD_REQUEST; // 400
            
        return new ResponseEntity<>(e.getMessage(), status);
        }
    }

}
