package com.sansantek.sansanmulmul.group.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
public class GroupResponse {

    private int groupId;
    private String groupName;
    private LocalDateTime groupStartDate;
    private LocalDateTime groupEndDate;
    private int groupMaxMembers;
    // 참여 인원, 멤버가 그룹 참여 상태
    private String mountainImg;
}
