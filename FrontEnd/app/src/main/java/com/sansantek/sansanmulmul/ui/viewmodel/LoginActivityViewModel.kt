package com.sansantek.sansanmulmul.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kakao.sdk.user.model.User

private const val TAG = "LoginActivityViewModel_싸피"
class LoginActivityViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {
    private var _user: User = User(null, null, null, null, null, null, null)
    val user: User
        get() = _user

    fun setUser(user: User){
        _user = user
    }

    private var _userGender: MutableLiveData<String> = MutableLiveData("")
    val userGender: LiveData<String>
        get() = _userGender

    fun setUserGender(gender: String){
        Log.d(TAG, "setUserGender: $gender")
        _userGender.value = gender
    }

}