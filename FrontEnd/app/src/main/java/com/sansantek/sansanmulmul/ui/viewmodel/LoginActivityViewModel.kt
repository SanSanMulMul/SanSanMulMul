package com.sansantek.sansanmulmul.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kakao.sdk.user.model.User
import com.sansantek.sansanmulmul.data.model.KakaoLoginToken
import com.sansantek.sansanmulmul.data.model.KakaoLoginUser

private const val TAG = "싸피_LoginActivityViewModel"

class LoginActivityViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private var _user: User = User(null, null, null, null, null, null, null)
    val user: User
        get() = _user

    fun setUser(user: User) {
        _user = user
    }

    private lateinit var _uri: Uri
    val uri: Uri
        get() = _uri

    fun setUri(uri: Uri) {
        _uri = uri
    }

    private var _userGender: MutableLiveData<String> = MutableLiveData("")
    val userGender: LiveData<String>
        get() = _userGender

    fun setUserGender(gender: String) {
        Log.d(TAG, "setUserGender: $gender")
        _userGender.value = gender
    }

    private var _userProviderId: MutableLiveData<String> = MutableLiveData("")
    val userProviderId: LiveData<String>
        get() = _userProviderId

    fun setUserProviderId(id: String) {
        _userProviderId.value = id
    }

    private var _userName: MutableLiveData<String> = MutableLiveData("")
    val userName: LiveData<String>
        get() = _userName

    fun setUserName(name: String) {
        _userName.value = name
    }

    private var _userNickName: MutableLiveData<String> = MutableLiveData("")
    val userNickname: LiveData<String>
        get() = _userNickName

    fun setUserNickName(nickName: String) {
        _userNickName.value = nickName
    }

    private var _userProfileImg: MutableLiveData<String> = MutableLiveData("")
    val userProfileImg: LiveData<String>
        get() = _userProfileImg

    fun setUserProfileImg(uri: String) {
        _userProfileImg.value = uri
    }

    private var _userBirth: MutableLiveData<String> = MutableLiveData("")
    val userBirth: LiveData<String>
        get() = _userBirth

    fun setUserBirth(birth: String) {
        _userBirth.value = birth
    }

    private var _userAdmin = false

    private var _userStyles: MutableLiveData<List<Int>> = MutableLiveData<List<Int>>(listOf())
    val userStyles: LiveData<List<Int>>
        get() = _userStyles

    fun setUserStyles(styleList: List<Int>) {
        _userStyles.value = styleList
    }

    private var _kakaoLoginUser: KakaoLoginUser =
        KakaoLoginUser("", "", "", "", "", "", false, listOf(0))
    val kakaoLoginUser: KakaoLoginUser
        get() = _kakaoLoginUser

    fun setKakaoLoginUser(kakaoLoginUser: KakaoLoginUser) {
        _kakaoLoginUser = kakaoLoginUser
    }

    private var _loginToken: KakaoLoginToken = KakaoLoginToken("", "")
    val loginToken: KakaoLoginToken
        get() = _loginToken

    fun setLoginToken(kakaoLoginToken: KakaoLoginToken) {
        _loginToken = kakaoLoginToken
    }

}