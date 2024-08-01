package com.sansantek.sansanmulmul.group.service;

import com.sansantek.sansanmulmul.group.domain.Group;
import com.sansantek.sansanmulmul.group.dto.response.GroupResponse;
import com.sansantek.sansanmulmul.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    // repository
    private final GroupRepository groupRepository;

    public List<GroupResponse> getAllGroups() {
        List<GroupResponse> groups = new ArrayList<>();

        // DB에서 모든 그룹 가져오기
        List<Group> groupList = groupRepository.findAll();

        for (Group group : groupList) {
            GroupResponse gr = new GroupResponse(
                    group.getGroupId(),
                    group.getGroupName(),
                    group.getGroupStartDate(),
                    group.getGroupEndDate(),
                    group.getGroupMaxMembers(),
                    group.getMountain().getMountainImg()
            );

            groups.add(gr);
        }

        return groups;
    }
}
