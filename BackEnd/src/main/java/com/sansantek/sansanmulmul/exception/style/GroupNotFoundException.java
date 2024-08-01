package com.sansantek.sansanmulmul.exception.style;

public class GroupNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GroupNotFoundException() {
        super("해당 스타일의 그룹이 존재하지 않습니다.");
    }
}
