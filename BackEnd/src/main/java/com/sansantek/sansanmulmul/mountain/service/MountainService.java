package com.sansantek.sansanmulmul.mountain.service;


import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.domain.spot.MountainSpot;
import com.sansantek.sansanmulmul.mountain.dto.response.NewsResponse;
import com.sansantek.sansanmulmul.mountain.repository.MountainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Arrays;
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
    public List<Mountain> getSpring() {
        return mountainRepository.findByMountainWeatherIn(Arrays.asList("SPRING", "ALL"));
    }
    public List<Mountain> getSummer() {
        return mountainRepository.findByMountainWeatherIn(Arrays.asList("SUMMER", "ALL"));
    }
    public List<Mountain> getFall() {
        return mountainRepository.findByMountainWeatherIn(Arrays.asList("FALL", "ALL"));
    }
    public List<Mountain> getWinter() {
        return mountainRepository.findByMountainWeatherIn(Arrays.asList("WINTER", "ALL"));
    }

    public List<NewsResponse> getMountainName() {
        List<NewsResponse> mountainNameList = new ArrayList<>();

        for (Mountain mountain : mountainRepository.findAll())
            mountainNameList.add(new NewsResponse(mountain.getMountainName(), mountain.getMountainImg()));

        return mountainNameList;
    }

    public NewsResponse getMountainName(String mountainName) {
        Mountain mountain = mountainRepository.findByMountainName(mountainName);

        return new NewsResponse(mountain.getMountainName(), mountain.getMountainImg());
    }
}