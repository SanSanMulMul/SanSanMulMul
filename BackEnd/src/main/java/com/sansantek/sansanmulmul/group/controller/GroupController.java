package com.sansantek.sansanmulmul.group.controller;

import com.sansantek.sansanmulmul.group.dto.response.GroupResponse;
import com.sansantek.sansanmulmul.group.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
@Tag(name = "그룹 정보 컨트롤러", description = "그룹 정보관련 기능 수행")
public class GroupController {

    // service
    private final GroupService groupService;

    @GetMapping("/all")
    @Operation(summary = "그룹 정보 전체 조회", description = "그룹에 대한 정보를 전체 조회")
    public ResponseEntity<?> getAllGroups() {
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            List<GroupResponse> groupResponses = groupService.getAllGroups();

            return new ResponseEntity<>(groupResponses, status);

        } catch (Exception e) {
            status = HttpStatus.BAD_REQUEST;

            return new ResponseEntity<>(e.getMessage(), status);
        }
    }
}
