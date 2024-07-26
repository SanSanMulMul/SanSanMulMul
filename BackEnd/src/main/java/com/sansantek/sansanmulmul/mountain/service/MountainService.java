package com.sansantek.sansanmulmul.mountain.service;


import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.repository.MountainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MountainService {

    @Autowired
    private MountainRepository mountainRepository;

    public List<Mountain> getAllMountains(){
        return mountainRepository.findAll();
    }

    public Mountain getMountainDetail(Long mountain_id){
        return mountainRepository.findById(mountain_id).orElse(null);
    }
}
