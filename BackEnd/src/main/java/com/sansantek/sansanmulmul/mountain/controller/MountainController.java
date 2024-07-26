package com.sansantek.sansanmulmul.mountain.controller;


import com.sansantek.sansanmulmul.mountain.domain.Mountain;
import com.sansantek.sansanmulmul.mountain.service.MountainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mountain")
public class MountainController {

    @Autowired
    private MountainService mountainService;

    @GetMapping
    public List<Mountain> getAllMountains(){
        return mountainService.getAllMountains();

    }
    @GetMapping("/{mountain_id}")
    public Mountain getMountainDetail(@PathVariable Long mountain_id){
        return mountainService.getMountainDetail(mountain_id);
    }

}
