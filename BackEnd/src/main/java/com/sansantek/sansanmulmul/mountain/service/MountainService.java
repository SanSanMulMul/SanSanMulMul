package com.sansantek.sansanmulmul.mountain.service;


import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.domain.spot.MountainSpot;
import com.sansantek.sansanmulmul.mountain.repository.MountainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class MountainService {

    @Autowired
    private MountainRepository mountainRepository;


    public List<Mountain> getAllMountains(){

        return mountainRepository.findAll();
    }

    public Mountain getMountainDetail(int mountain_id){

        return mountainRepository.findById(mountain_id).orElse(null);
    }
    public List<MountainSpot> getMountainSpotsWithDetail(int mountain_id, String detail) {
        Mountain mountain = mountainRepository.findById(mountain_id).orElse(null);
        if (mountain == null) {
            return null;
        }
        return mountain.getMountainSpots().stream()
                .filter(spot -> detail.equals(spot.getMountainSpotDetail()))
                .collect(Collectors.toList());
    }

    public List<MountainSpot> getMountainSpotsWithDetails(int mountain_id, String... details) {
        Mountain mountain = mountainRepository.findById(mountain_id).orElse(null);
        if (mountain == null) {
            return null;
        }
        return mountain.getMountainSpots().stream()
                .filter(spot -> {
                    for (String detail : details) {
                        if (detail.equals(spot.getMountainSpotDetail())) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }
    public List<Mountain> searchMountainsByName(String name) {
        return mountainRepository.findByMountainNameContaining(name);
    }
}