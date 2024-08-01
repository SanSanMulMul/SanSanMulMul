package com.sansantek.sansanmulmul.group.service.style;

import com.sansantek.sansanmulmul.group.domain.style.GroupHikingStyle;
import com.sansantek.sansanmulmul.group.dto.response.GroupStyleResponse;
import com.sansantek.sansanmulmul.group.repository.style.GroupHikingStyleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupStyleService {

    private final GroupHikingStyleRepository groupStyleRepository;

    public List<GroupStyleResponse> getGroupList(int styleId) {
        List<GroupStyleResponse> groupStyleResponseList = new ArrayList<>();

        // styleId기반으로 그룹 찾기
        List<GroupHikingStyle> groupHikingStyleList = groupStyleRepository.findByStyle_HikingStylesId(styleId);

        // GroupStyleResponse 추출
        for (GroupHikingStyle groupHikingStyle : groupHikingStyleList) {
            GroupStyleResponse gr = new GroupStyleResponse(
                    groupHikingStyle.getGroup().getGroupId(),
                    groupHikingStyle.getGroup().getGroupName(),
                    groupHikingStyle.getGroup().getGroupStartDate(),
                    groupHikingStyle.getGroup().getGroupEndDate(),
                    groupHikingStyle.getGroup().getGroupMaxMembers()
            );

            groupStyleResponseList.add(gr);
        }

        return groupStyleResponseList;
    }
}
