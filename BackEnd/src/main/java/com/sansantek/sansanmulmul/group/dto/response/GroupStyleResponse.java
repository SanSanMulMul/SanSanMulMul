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
public class GroupStyleResponse {

    private int groupId;
    private String groupName;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    // 참여중인 멤버 수
    private int maxMembers;

}
