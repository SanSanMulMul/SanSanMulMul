package com.sansantek.sansanmulmul.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FcmType {
    OFFCOURSE("경로 이탈"),          //그룹 내 전부
    CHANGECOURSE("등산 코스 변경"),   //그룹 내 전부
    CHANGEDATE("등산 일정 변경"),     //그룹 내 전부
    NOTICE("공지"),                   //그룹 내 전부
    MESSAGE("메세지"),               // 보낸 사람 제외 그룹 내 전부
    DELEGATE("방장변경"),
    JOINREQUEST("그룹 가입 요청")     //ONLY방장
    //그룹 삭제 시 남은 사람들에게, 방장 위임시
    ;
    private final String type;
}