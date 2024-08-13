package com.sansantek.sansanmulmul.data.repository

import com.sansantek.sansanmulmul.data.model.MountainPeakStone
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil

class MountainPeakStoneRepository {
  suspend fun addStone(accessToken: String, stoneId: Int): Boolean? {
    return try {
      val response = RetrofiltUtil.stoneService.addStone(accessToken, stoneId)
      if (response.isSuccessful) {
        response.body()
      } else {
        null
      }
    } catch (e: Exception) {
      null
    }
  }

  suspend fun searchAllStones(): MountainPeakStone? {
    return try {
      val response = RetrofiltUtil.stoneService.searchAllStones()
      if (response.isSuccessful) {
        response.body()
      } else {
        null
      }
    } catch (e: Exception) {
      null
    }
  }
}