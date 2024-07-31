package com.sansantek.sansanmulmul.mountain.service.summitstone;


import com.sansantek.sansanmulmul.mountain.domain.summitstone.Summitstone;
import com.sansantek.sansanmulmul.mountain.repository.summitstone.SummitstoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SummitStoneService {

    @Autowired
    private SummitstoneRepository summitstoneRepository;

    public List<Summitstone> getAllSummitstones() {
        return summitstoneRepository.findAll();
    }
}
