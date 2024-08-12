package com.sansantek.sansanmulmul.record.controller;import com.sansantek.sansanmulmul.exception.auth.InvalidTokenException;import com.sansantek.sansanmulmul.record.dto.request.LocationRequest;import com.sansantek.sansanmulmul.record.dto.request.RecordRequest;import com.sansantek.sansanmulmul.record.dto.response.DetailRecordResponse;import com.sansantek.sansanmulmul.record.dto.response.LocationResponse;import com.sansantek.sansanmulmul.user.domain.User;import com.sansantek.sansanmulmul.record.domain.HikingRecord;import com.sansantek.sansanmulmul.record.dto.response.AllRecordResonse;import com.sansantek.sansanmulmul.user.service.UserService;import com.sansantek.sansanmulmul.record.service.RecordService;import com.sansantek.sansanmulmul.user.service.badge.BadgeService;import io.swagger.v3.oas.annotations.Operation;import io.swagger.v3.oas.annotations.tags.Tag;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.http.HttpStatus;import org.springframework.http.ResponseEntity;import org.springframework.security.core.Authentication;import org.springframework.web.bind.annotation.*;import java.util.HashMap;import java.util.List;import java.util.Map;@Slf4j@RestController@RequiredArgsConstructor@RequestMapping("/record")@Tag(name = "등산 기록 컨트롤러", description = "등산 기록의 관한 모든 기능 수행")public class RecordController {    // service    private final UserService userService;    private final RecordService recordService;    private final BadgeService badgeService;    @GetMapping("/chk")    @Operation(summary = "방장 유무 확인", description = "해당 회원이 그룹의 방장인지 확인")    public ResponseEntity<?> chkLeader            (Authentication authentication,             @RequestParam int crewId) {        HttpStatus status = HttpStatus.ACCEPTED;        try {            // 토큰을 통한 userProviderId 추출            String userProviderId = authentication.getName();            // 방장 유무 확인            boolean chk = recordService.chkLeader(userProviderId, crewId);            status = HttpStatus.OK; // 200            return new ResponseEntity<>(chk, status);        } catch (InvalidTokenException e) {            log.error("토큰 유효성 검사 실패: {}", e.getMessage());            status = HttpStatus.UNAUTHORIZED; // 401            return new ResponseEntity<>(false, status);        } catch (Exception e) {            log.error("그룹 방장 확인 실패: {}", e.getMessage());            status = HttpStatus.BAD_REQUEST; // 400            return new ResponseEntity<>(false, status);        }    }    @GetMapping("/search")    @Operation(summary = "기록 종료 여부 확인", description = "해당 회원이 그룹에서 개인 기록이 종료되었는지 확인")    public ResponseEntity<?> chkRecord            (Authentication authentication,             @RequestParam int crewId) {        HttpStatus status = HttpStatus.ACCEPTED; // 202        try {            String userProviderId = authentication.getName();            boolean chk = recordService.chkRecord(userProviderId, crewId);            status = HttpStatus.OK;            return new ResponseEntity<>(chk, status);        } catch (InvalidTokenException e) {            log.error("토큰 유효성 검사 실패: {}", e.getMessage());            status = HttpStatus.UNAUTHORIZED; // 401            return new ResponseEntity<>(false, status);        } catch (Exception e) {            log.error("기록 추가 실패: {}", e.getMessage());            status = HttpStatus.BAD_REQUEST; // 400            return new ResponseEntity<>(false, status);        }    }    @PostMapping("/coord")    @Operation(summary = "회원 좌표 저장", description = "해당 회원의 실시간 좌표를 저장")    public ResponseEntity<?> saveCoord            (Authentication authentication,              @RequestBody LocationRequest request) {        HttpStatus status = HttpStatus.ACCEPTED; // 202        try {            // 토큰을 통한 userProviderId 추출            String userProviderId = authentication.getName();            // 사용자 정보 가져오기            User user = userService.getUser(userProviderId);            // 사용자 실시간 좌표 저장            recordService.saveCoord(user.getUserId(), request);            status = HttpStatus.OK; // 200            return new ResponseEntity<>(status);        } catch (InvalidTokenException e) {            log.error("토큰 유효성 검사 실패: {}", e.getMessage());            status = HttpStatus.UNAUTHORIZED; // 401            return new ResponseEntity<>(e.getMessage(), status);        } catch (Exception e) {            log.error("회원 좌표 저장 실패: {}", e.getMessage());            status = HttpStatus.NOT_FOUND; // 400            return new ResponseEntity<>(e.getMessage(), status);        }    }    @GetMapping("/coord")    @Operation(summary = "그룹 회원들 좌표 조회", description = "해당 그룹 내 회원들의 좌표 조회")    public ResponseEntity<?> viewCoord            (@RequestParam int crewId) {        HttpStatus status = HttpStatus.ACCEPTED;        try {            // 등산 참여 멤버 좌표 조회            List<LocationResponse> response = recordService.sendMembersCoord(crewId);            status = HttpStatus.OK; // 200            return new ResponseEntity<>(response, status);        } catch (Exception e) {            log.error("그룹 회원 좌표 조회 실패: {}", e.getMessage());            status = HttpStatus.NOT_FOUND; // 400            return new ResponseEntity<>(e.getMessage(), status);        }    }    @PostMapping    @Operation(summary = "등산 기록 추가")    public ResponseEntity<?> addUserRecord            (Authentication authentication,             @RequestBody RecordRequest request) {        HttpStatus status = HttpStatus.ACCEPTED;        try {            // 토큰을 통한 userProviderId 추출            String userProviderId = authentication.getName();            // 해당 사용자 가져오기            User user = userService.getUser(userProviderId);            // 사용자 기록하기            int recordId = recordService.addRecords(user.getUserId(), request);            // 칭호 확인            badgeService.chkBadge(user.getUserId(), recordId);            status = HttpStatus.CREATED; // 201            return new ResponseEntity<>(true, status);        } catch (InvalidTokenException e) {            log.error("토큰 유효성 검사 실패: {}", e.getMessage());            status = HttpStatus.UNAUTHORIZED; // 401            return new ResponseEntity<>(false, status);        } catch (Exception e) {            log.error("기록 추가 실패: {}", e.getMessage());            status = HttpStatus.BAD_REQUEST; // 400            return new ResponseEntity<>(false, status);        }    }    @GetMapping("/all")    @Operation(summary = "회원 전체 등산 기록 조회", description = "액세스 토큰을 사용해 회원 전체 등산 기록 조회")    public ResponseEntity<?> getUserRecord            (Authentication authentication) {        HttpStatus status = HttpStatus.ACCEPTED;        try {            // 토큰을 통한 userProviderId 추출            String userProviderId = authentication.getName();            // 해당 사용자 가져오기            User user = userService.getUser(userProviderId);            // 해당 사용자 등산 기록 가져오기            List<AllRecordResonse> userRecordList = recordService.getAllRecords(user.getUserId());            status = HttpStatus.OK; // 200            return new ResponseEntity<>(userRecordList, status);        } catch (InvalidTokenException e) {            log.error("토큰 유효성 검사 실패: {}", e.getMessage());            status = HttpStatus.UNAUTHORIZED; // 401            return new ResponseEntity<>(e.getMessage(), status);        } catch (Exception e) {            log.error("등산 기록 조회 실패: {}", e.getMessage());            status = HttpStatus.NOT_FOUND; // 400            return new ResponseEntity<>(e.getMessage(), status);        }    }    @GetMapping("/detail")    @Operation(summary = "회원 해당 등산 기록 상세 조회", description = "액세스 토큰을 사용해 회원의 recordId에 해당하는 등산 기록 상세 조회")    public ResponseEntity<?> getUserRecordDetails            (/*Authentication authentication, */             @RequestParam int recordId) {        HttpStatus status = HttpStatus.ACCEPTED;        try {            Map<Object, Object> resultMap = new HashMap<>();            // 해당 사용자 해당 등산 기록 가져오기            DetailRecordResponse userRecord = recordService.getDetailRecord(recordId);            status = HttpStatus.OK; // 200            return new ResponseEntity<>(userRecord, status);        } catch (InvalidTokenException e) {            log.error("토큰 유효성 검사 실패: {}", e.getMessage());            status = HttpStatus.UNAUTHORIZED; // 401            return new ResponseEntity<>(e.getMessage(), status);        } catch (Exception e) {            log.error("기록 상세 조회 실패: {}", e.getMessage());            status = HttpStatus.BAD_REQUEST; // 400            return new ResponseEntity<>(e.getMessage(), status);        }    }    @GetMapping("/{userId}/all")    @Operation(summary = "해당 회원 전체 등산 기록 조회", description = "액세스 토큰을 사용해 해당 회원의 전체 등산 기록 조회")    public ResponseEntity<?> getUserByUserIdRecord            (@PathVariable int userId) {        HttpStatus status = HttpStatus.ACCEPTED;        try {            // 해당 사용자 가져오기            User user = userService.getUser(userId);            // 해당 사용자 등산 기록 가져오기            List<AllRecordResonse> userRecordList = recordService.getAllRecords(user.getUserId());            status = HttpStatus.OK; // 200            return new ResponseEntity<>(userRecordList, status);        } catch (InvalidTokenException e) {            log.error("토큰 유효성 검사 실패: {}", e.getMessage());            status = HttpStatus.UNAUTHORIZED; // 401            return new ResponseEntity<>(e.getMessage(), status);        } catch (Exception e) {            log.error("등산 기록 조회 실패: {}", e.getMessage());            status = HttpStatus.NOT_FOUND; // 400            return new ResponseEntity<>(e.getMessage(), status);        }    }}