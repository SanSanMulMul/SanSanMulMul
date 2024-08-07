package com.sansantek.sansanmulmul.record.repository;

import com.sansantek.sansanmulmul.record.domain.HikingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HikingRecordRepository extends JpaRepository<HikingRecord, Integer> {
    List<HikingRecord> findByUser_UserId(int userId);
    Optional<HikingRecord> findByRecordId(int recordId);
}
