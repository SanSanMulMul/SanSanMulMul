package com.sansantek.sansanmulmul.record.service;

import com.sansantek.sansanmulmul.crew.repository.request.CrewUserRepository;
import com.sansantek.sansanmulmul.crew.service.request.CrewRequestService;
import com.sansantek.sansanmulmul.exception.record.RecordNotFoundException;
import com.sansantek.sansanmulmul.mountain.repository.course.CourseRepository;
import com.sansantek.sansanmulmul.mountain.repository.course.TrackPathRepository;
import com.sansantek.sansanmulmul.record.domain.HikingRecord;
import com.sansantek.sansanmulmul.record.dto.response.AllRecordResonse;
import com.sansantek.sansanmulmul.record.dto.response.DetailRecordResponse;
import com.sansantek.sansanmulmul.record.repository.HikingRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordService {

    private final HikingRecordRepository hikingRecordRepository;
    private final CourseRepository courseRepository;
    private final CrewRequestService crewRequestService;

    // 회원 전체의 기록 가져오기
    public List<AllRecordResonse> getAllRecords(int userId) {
        List<AllRecordResonse> records = new ArrayList<>();

        // 해당 회원의 userId로 등산 기록 찾기
        List<HikingRecord> userRecords = hikingRecordRepository.findByUser_UserId(userId);

        // RecordResonse로 변환
        for(HikingRecord record : userRecords) {
            AllRecordResonse response = new AllRecordResonse(
                    record.getMountain().getMountainName(),
                    record.getRecordStartTime(),
                    record.getMountain().getMountainImg()
            );

            records.add(response);
        }

        return records;
    }

    // 회원의 특정 기록 가져오기
    public DetailRecordResponse getDetailRecord(int recordId) {
        // 기록 조회
        HikingRecord hikingRecord = hikingRecordRepository.findByRecordId(recordId)
                .orElseThrow(() -> new RecordNotFoundException());

        // 멤버 조회


        return new DetailRecordResponse(
                // 산
                hikingRecord.getMountain().getMountainName(),
                hikingRecord.getMountain().getMountainImg(),

                // 코스
                hikingRecord.getCrew().getUpCourse().getCourseName(),
                hikingRecord.getCrew().getDownCourse().getCourseName(),
                hikingRecord.getCrew().getUpCourse().getCourseLength(),
                hikingRecord.getCrew().getDownCourse().getCourseLength(),
                courseRepository.findByCourseId(hikingRecord.getCrew().getUpCourse().getCourseId()),
                courseRepository.findByCourseId(hikingRecord.getCrew().getDownCourse().getCourseId()),

                // 참여 멤버
                crewRequestService.getCrewMembers(hikingRecord.getCrew().getCrewId()),

                // 기록
                hikingRecord.getRecordStartTime(),
                hikingRecord.getRecordDuration(),
                hikingRecord.getRecordSteps(),
                hikingRecord.getRecordElevation(),
                hikingRecord.getRecordKcal()
        );
    }
}
