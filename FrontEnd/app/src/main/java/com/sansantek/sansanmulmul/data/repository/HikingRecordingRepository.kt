package com.sansantek.sansanmulmul.data.repository

import android.util.Log
import com.sansantek.sansanmulmul.data.model.HikingHistory
import com.sansantek.sansanmulmul.data.model.HikingRecordingCoord
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil

private const val TAG = "싸피_HikingRecordingRepository"

class HikingRecordingRepository {

  suspend fun postMyCoord(accessToken: String, coordRequestBody: HikingRecordingCoord): String? {
    return try {
      val response =
        RetrofiltUtil.hikingRecordingService.saveMyCoord(accessToken, coordRequestBody)
      if (response.isSuccessful) {
        response.body()
      } else {
        Log.d(TAG, "싸피_HikingRecordingRepository: postMyCoord response is null")
        null
      }
    } catch (e: Exception) {
      null
    }
  }

  suspend fun amILeader(accessToken: String, crewId: Int): Boolean? {
    return try {
      val response =
        RetrofiltUtil.hikingRecordingService.amILeader(accessToken, crewId)
      if (response.isSuccessful) {
        response.body()
      } else {
        Log.d(TAG, "싸피_HikingRecordingRepository: amILeader response is null")
        null
      }
    } catch (e: Exception) {
      null
    }
  }

  suspend fun addHikingHistory(accessToken: String, history: HikingHistory): Boolean? {
    return try {
      val response =
        RetrofiltUtil.hikingRecordingService.addHikingHistory(accessToken, history)
      if (response.isSuccessful) {
        response.body()
      } else {
//        Log.d(TAG, "싸피_HikingRecordingRepository: amILeader response is null")
        null
      }
    } catch (e: Exception) {
      null
    }
  }
}