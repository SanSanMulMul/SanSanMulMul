package com.sansantek.sansanmulmul.data.repository

import com.sansantek.sansanmulmul.data.model.CrewMountainDetail
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil

class CrewRepository {
  suspend fun getCrewMountainDetail(crewId: Int): CrewMountainDetail? {
    return try {
      val response = RetrofiltUtil.crewService.getCrewMountainDetail(crewId)
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