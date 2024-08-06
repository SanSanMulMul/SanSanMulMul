package com.sansantek.sansanmulmul.mountain.service.summitstone;


import com.sansantek.sansanmulmul.mountain.domain.summitstone.Summitstone;
import com.sansantek.sansanmulmul.mountain.repository.summitstone.SummitstoneRepository;
import com.sansantek.sansanmulmul.user.dto.response.StoneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummitStoneService {

    @Autowired
    private SummitstoneRepository summitstoneRepository;

    // 모든 정상석 조회
    public List<StoneResponse> getStoneList() {
        List<StoneResponse> stoneList = new ArrayList<>();

        List<Summitstone> summitstones = summitstoneRepository.findAll();

        for (Summitstone summitstone : summitstones) {
            StoneResponse sr = new StoneResponse(
                    summitstone.getStoneId(),
                    summitstone.getMountain().getMountainName(),
                    summitstone.getStoneName(),
                    summitstone.getStoneImg()
            );

            stoneList.add(sr);
        }

        return stoneList;
    }
}
