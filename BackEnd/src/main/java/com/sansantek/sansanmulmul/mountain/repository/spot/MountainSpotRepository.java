package com.sansantek.sansanmulmul.mountain.repository.spot;

import com.sansantek.sansanmulmul.mountain.domain.spot.MountainSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MountainSpotRepository extends JpaRepository<MountainSpot, Long> {
    List<MountainSpot> findByMountainSpotDetail(String detail);
    List<MountainSpot> findByMountainMountainCode(int mountain_code);
}