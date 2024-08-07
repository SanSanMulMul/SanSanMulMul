package com.sansantek.sansanmulmul.user.service.record;

import com.sansantek.sansanmulmul.user.domain.record.HikingRecord;
import com.sansantek.sansanmulmul.user.dto.response.RecordResonse;
import com.sansantek.sansanmulmul.user.repository.record.HikingRecordRepository;
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

    public List<RecordResonse> getRecords(int userId) {
        List<RecordResonse> records = new ArrayList<>();

        // 해당 회원의 userId로 등산 기록 찾기
        List<HikingRecord> userRecords = hikingRecordRepository.findByUser_UserId(userId);

        // RecordResonse로 변환
        for(HikingRecord record : userRecords) {
            RecordResonse response = new RecordResonse(record.getRecordId(), record.getMountain().getMountainName());

            records.add(response);
        }

        return records;
    }

    public Optional<HikingRecord> getDetailRecord(int recordId) {
        return hikingRecordRepository.findByRecordId(recordId);
    }
}
