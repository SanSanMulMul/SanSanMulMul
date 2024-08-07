package com.sansantek.sansanmulmul.record.service;

import com.sansantek.sansanmulmul.exception.record.RecordNotFoundException;
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

    public List<AllRecordResonse> getRecords(int userId) {
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

    public DetailRecordResponse getDetailRecord(int recordId) {
        // 기록 조회
        HikingRecord hikingRecord = hikingRecordRepository.findByRecordId(recordId)
                .orElseThrow(() -> new RecordNotFoundException());

        return new DetailRecordResponse(
                hikingRecord.getRecordStartTime(),
                hikingRecord.getMountain().getMountainName(),
                hikingRecord.getMountain().getMountainImg(),
//                hikingRecord.getCrew().getAscentCourse().getCourseName(),
//                hikingRecord.getCrew().getDescentCourse().getCourseName(),
                // 코스 좌표
                // 참여 멤버
                hikingRecord.getRecordSteps(),
                hikingRecord.getRecordElevation(),
                hikingRecord.getRecordKcal()
        );
    }
}
