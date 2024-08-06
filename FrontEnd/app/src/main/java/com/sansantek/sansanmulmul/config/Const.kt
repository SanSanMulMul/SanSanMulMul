package com.sansantek.sansanmulmul.config

class Const {
    companion object {
        // Hiking Recording
        const val BANNED = -1
        const val BEFORE_HIKING = 0
        const val HIKING = 1
        const val AFTER_HIKING = 2

        // Shared Preference
        const val SHARED_PREFERENCES_NAME = "sansanmulmul_preference"
        const val SP_HIKING_RECORDING_STATE = "hiking_recording_state"
        const val SP_SPEND_TIME_KEY = "spend_time"
        const val SP_SPEND_TIME_IS_RUNNING_KEY = "spend_time_is_running"

        // Camera permission code
        const val REQUEST_CAMERA_PERMISSION = 100
        const val REQUEST_IMAGE_CAPTURE = 1

        val HIKINGSTYLE = arrayListOf("", "설렁설렁", "열정열정", "등산은 식후경", "등산은 사진", "소심이", "괄괄이")
        val TITLE = arrayListOf("등린이", "엄홍길", "셰르파", "한라산 정복자", "지리산 정복자", "설악산 정복자", "무등산 정복자", "계룡산 정복자", "5대 명산 정복자", "영남 회장", "1000🚩", "10000🚩", "전국구", "낟다람쥐", "무지개🏳‍🌈")
    }
}