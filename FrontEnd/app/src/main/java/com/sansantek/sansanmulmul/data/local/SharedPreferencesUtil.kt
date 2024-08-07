package com.sansantek.sansanmulmul.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sansantek.sansanmulmul.config.Const.Companion.BEFORE_HIKING
import com.sansantek.sansanmulmul.config.Const.Companion.SHARED_PREFERENCES_NAME
import com.sansantek.sansanmulmul.config.Const.Companion.SP_HIKING_RECORDING_STATE
import com.sansantek.sansanmulmul.config.Const.Companion.SP_SPEND_TIME_IS_RUNNING_KEY
import com.sansantek.sansanmulmul.config.Const.Companion.SP_SPEND_TIME_KEY
import com.sansantek.sansanmulmul.data.model.KakaoLoginToken

class SharedPreferencesUtil(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    // 카카오를 통한 로그인 토큰 저장 및 얻기
    fun saveKakaoLoginToken(token: KakaoLoginToken) {
        val editor = preferences.edit()
        val json = gson.toJson(token)
        editor.putString("kakao_login_token", json)
        editor.apply()
    }

    fun getKakaoLoginToken(): KakaoLoginToken? {
        val json = preferences.getString("kakao_login_token", null)
        return if (json != null) {
            gson.fromJson(json, KakaoLoginToken::class.java)
        } else {
            null
        }
    }

    // recording 서비스 실행 유무 저장하기
    fun saveRecordingServiceState(recordingStatus: String) {
        val editor = preferences.edit()
        editor.putString("isRecording", recordingStatus)
        editor.apply()
    }

    // recording 서비스 실행 유무 불러오기
    fun getRecordingServiceState(): String {
        return preferences.getString("isRecording", "종료") ?: "종료"
    }

//    //사용자 정보 저장
//    fun addUser(user:User){
//        val editor = preferences.edit()
//        editor.putString("id", user.id)
//        editor.putString("name", user.name)
//        editor.apply()
//    }

//    fun getUser(): User{
//        val id = preferences.getString("id", "")
//        if (id != ""){
//            val name = preferences.getString("name", "")
//            return User(id!!, name!!, "",0)
//        }else{
//            return User()
//        }
//    }

//    fun deleteUser(){
//        //preference 지우기
//        val editor = preferences.edit()
//        editor.clear()
//        editor.apply()
//    }

    // 등산 기록 탭 관련 SP
    fun saveHikingRecordingState(state: Int) {
        val editor = preferences.edit()

        if (preferences.contains(SP_HIKING_RECORDING_STATE)) {
            editor.putInt(SP_HIKING_RECORDING_STATE, state);
        } else {
            editor.putInt(SP_HIKING_RECORDING_STATE, state);
        }
        editor.apply()
    }

    fun getHikingRecordingState(): Int {
        return preferences.getInt(SP_HIKING_RECORDING_STATE, BEFORE_HIKING)
    }

    fun deleteHikingRecordingState() {
        val editor = preferences.edit()
        editor.remove(SP_HIKING_RECORDING_STATE)
        editor.clear()
        editor.apply()
    }


    fun saveHikingRecordingBaseTime(seconds: Long) {
        val editor = preferences.edit()

        if (preferences.contains(SP_SPEND_TIME_KEY)) {
            editor.putLong(SP_SPEND_TIME_KEY, seconds);
        } else {
            editor.putLong(SP_SPEND_TIME_KEY, seconds);
        }
        editor.apply()
    }

    fun getHikingRecordingBaseTime(): Long {
        return preferences.getLong(SP_SPEND_TIME_KEY, 0)
    }

    fun deleteHikingRecordingTime() {
        val editor = preferences.edit()
        editor.remove(SP_SPEND_TIME_KEY)
        editor.clear()
        editor.apply()
    }

    fun saveHikingRecordingStatus(recordingStatus: String) {
        val editor = preferences.edit()
        editor.putString(SP_SPEND_TIME_IS_RUNNING_KEY, recordingStatus)
        editor.apply()
    }

    fun getHikingRecordingStatus(): String {
        return preferences.getString(SP_SPEND_TIME_IS_RUNNING_KEY, "종료") ?: "종료"
    }


}