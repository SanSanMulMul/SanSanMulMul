package com.sansantek.sansanmulmul.config

import com.sansantek.sansanmulmul.R

class Const {
  companion object {
    // Hiking Recording
    const val BANNED = -1 // 등산 일정 자체가 시작되지 않은 상태
    const val BEFORE_HIKING = 0 // 등산 일정은 시작됐으나 상행시작 버튼을 누르지 않은 상태
    const val HIKING = 1 // 상챙 버튼 누른 후 ~ 끝내기 버튼 누르기 전
    const val AFTER_HIKING = 2 // 끝내기 버튼 누른 후
    const val IS_QR_SCANNED_KEY = "isQRScanned"

    // Shared Preference
    const val SHARED_PREFERENCES_NAME = "sansanmulmul_preference"
    const val SP_HIKING_RECORDING_STATE = "hiking_recording_state"
    const val SP_HIKING_RECORDING_ONGOING_CREW_ID = "hiking_recording_ongoing_crew_id"
    const val SP_SPEND_TIME_KEY = "spend_time"
    const val SP_SPEND_TIME_IS_RUNNING_KEY = "spend_time_is_running"
    const val SP_IS_QR_SCANNED_KEY = "is_qr_scanned"

    // Camera permission code
    const val REQUEST_CAMERA_PERMISSION = 100
    const val REQUEST_IMAGE_CAPTURE = 1

    const val HIKINGSTYLESIZE = 6
    val HIKINGSTYLE = arrayListOf("", "설렁설렁", "열정열정", "등산은 식후경", "등산은 사진", "소심이", "괄괄이")
    val TITLE = arrayListOf(
      "",
      "👶 등린이",
      "🏃‍♂️ 엄홍길",
      "🦺 셰르파",
      "🌋 한라산 정복자",
      "🏞 지리산 정복자",
      "🏔 설악산 정복자",
      "🗻 무등산 정복자",
      "🛤 계룡산 정복자",
      "🧭 5대 명산 정복자",
      "🗺 영남 회장",
      "✈️ 1000",
      "🚀 10000",
      "⚡ 전국구",
      "🐿 낟다람쥐",
      "🌈 무지개"
    )

    // Mountain Detail - Course Tab
    const val INFO_TAB = 0
    const val COURSE_TAB = 1
    val HikingBackgroundTintList = listOf(
      android.R.color.transparent,
      R.color.pink,
      R.color.light_green,
      R.color.light_blue,
      R.color.light_purple,
      R.color.orange,
      R.color.light_yellow,
    )

    const val SCHEDULE = 1
    const val COMPLETE = 2
    const val ALL = 3
  }
}