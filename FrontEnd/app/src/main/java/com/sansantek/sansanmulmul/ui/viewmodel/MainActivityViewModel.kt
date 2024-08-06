package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.sansantek.sansanmulmul.data.model.KakaoLoginToken
import com.sansantek.sansanmulmul.data.model.User

class MainActivityViewModel : ViewModel(){
    private lateinit var _user: User
    val user : User
        get() = _user

    fun setUser(user:User){
        _user = user
    }

    private var _token : KakaoLoginToken? = sharedPreferencesUtil.getKakaoLoginToken()
    val token : KakaoLoginToken?
        get() = _token

    fun setToken(token: KakaoLoginToken){
        _token
    }

    private var _hikingStyles : MutableLiveData<List<Int>> = MutableLiveData()
    val hikingStyles : LiveData<List<Int>>
        get() = _hikingStyles

    fun setHikingStyles(newHikingStyle : List<Int>){
        _hikingStyles.value = newHikingStyle
    }



}