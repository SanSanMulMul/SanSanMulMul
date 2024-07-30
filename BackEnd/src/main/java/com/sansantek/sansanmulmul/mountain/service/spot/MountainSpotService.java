package com.sansantek.sansanmulmul.mountain.service.spot;

import com.sansantek.sansanmulmul.mountain.domain.spot.MountainSpot;
import com.sansantek.sansanmulmul.mountain.repository.spot.MountainSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MountainSpotService {

    private final MountainSpotRepository mountainSpotRepository;

    public List<MountainSpot> getSpotsByDetail(String detail) {
        return mountainSpotRepository.findByMountainSpotDetail(detail);
    }

    public List<MountainSpot> getSpotsByMountainCode(int mountain_code) {
        return mountainSpotRepository.findByMountainMountainCode(mountain_code);
    }

    public List<MountainSpot> getAllSpots() {
        return mountainSpotRepository.findAll();
    }
}
