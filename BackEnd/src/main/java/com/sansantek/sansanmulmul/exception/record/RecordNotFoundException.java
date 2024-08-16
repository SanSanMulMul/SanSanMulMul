package com.sansantek.sansanmulmul.exception.record;

public class RecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecordNotFoundException() {
        super("기록 조회에 실패했습니다.\n 기록 아이디를 다시 확인해주세요.");
    }
}
