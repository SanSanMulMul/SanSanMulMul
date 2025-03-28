package com.sansantek.sansanmulmul.ui.util

import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.retrofit
import com.sansantek.sansanmulmul.data.network.api.ChatService
import com.sansantek.sansanmulmul.data.network.api.CourseService
import com.sansantek.sansanmulmul.data.network.api.CrewService
import com.sansantek.sansanmulmul.data.network.api.FCMService
import com.sansantek.sansanmulmul.data.network.api.HikingRecordingService
import com.sansantek.sansanmulmul.data.network.api.MountainService
import com.sansantek.sansanmulmul.data.network.api.NewsService
import com.sansantek.sansanmulmul.data.network.api.RecordService
import com.sansantek.sansanmulmul.data.network.api.StoneService
import com.sansantek.sansanmulmul.data.network.api.UserService

class RetrofiltUtil {
  companion object {
    // API를 호출하기 위한 MountainService 인터페이스 내의 함수들을 기능하게 하는 service 생성
    val mountainService = retrofit.create(MountainService::class.java)
    val courseService = retrofit.create(CourseService::class.java)
    val userService = retrofit.create(UserService::class.java)
    val newsService = retrofit.create(NewsService::class.java)
    val crewService = retrofit.create(CrewService::class.java)
    val chatService = retrofit.create(ChatService::class.java)
    val hikingRecordingService = retrofit.create(HikingRecordingService::class.java)
    val fcmService = retrofit.create(FCMService::class.java)
    val recordService = retrofit.create(RecordService::class.java)
    val stoneService = retrofit.create(StoneService::class.java)
  }
}