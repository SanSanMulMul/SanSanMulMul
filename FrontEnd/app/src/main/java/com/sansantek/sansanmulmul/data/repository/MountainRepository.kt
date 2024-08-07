package com.sansantek.sansanmulmul.data.repository

import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainCourse
import com.sansantek.sansanmulmul.data.model.MountainSunriseSunset
import com.sansantek.sansanmulmul.data.model.MountainWeather
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil

class MountainRepository {
    suspend fun searchMountainList(searchKeyword: String): List<Mountain>? {
        return try {
            val response = RetrofiltUtil.mountainService.searchMountainList(searchKeyword)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // 산 상세 정보 조회 (이름, 이미지, 설명, 고도, 위/경도, 추천 계절, 스팟 리스트)
    suspend fun getMountainDetail(mountainId: Int): Mountain? {
        return try {
            val response = RetrofiltUtil.mountainService.getMountainDetailByID(mountainId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // 산 일출, 일몰 시간 조회
    suspend fun getMountainSunriseSunset(mountainId: Int): List<MountainSunriseSunset>? {
        return try {
            val response =
                RetrofiltUtil.mountainService.getMountainSunriseAndSunSetTimeByID(mountainId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // 산 날씨 조회
    suspend fun getMountainWeather(mountainId: Int): List<MountainWeather>? {
        return try {
            val response =
                RetrofiltUtil.mountainService.getMountainWeatherByID(mountainId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // 산 코스 정보 조회
    suspend fun getMountainCourse(mountainId: Int): MountainCourse? {
        return try {
            val response =
                RetrofiltUtil.mountainService.getMountainCourse(mountainId)
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