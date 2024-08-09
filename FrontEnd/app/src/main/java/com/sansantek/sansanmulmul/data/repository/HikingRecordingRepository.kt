package com.sansantek.sansanmulmul.data.repository

import android.util.Log
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
}