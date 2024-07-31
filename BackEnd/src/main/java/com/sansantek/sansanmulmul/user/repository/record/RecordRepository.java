package com.sansantek.sansanmulmul.user.repository.record;

import com.sansantek.sansanmulmul.user.domain.record.HikingRecord;
import com.sansantek.sansanmulmul.user.dto.response.RecordResonse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Integer> {
    List<HikingRecord> findByUser_UserId(int userId);
    Optional<HikingRecord> findByRecordId(int recordId);
}
