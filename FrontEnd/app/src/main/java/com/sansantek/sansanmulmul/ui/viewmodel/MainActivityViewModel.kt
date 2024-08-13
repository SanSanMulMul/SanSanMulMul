package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sansantek.sansanmulmul.config.ApplicationClass
import com.sansantek.sansanmulmul.data.model.FollowUser
import com.sansantek.sansanmulmul.data.model.KakaoLoginToken
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MyPageInfo
import com.sansantek.sansanmulmul.data.model.User

class MainActivityViewModel : ViewModel() {
  private lateinit var _user: User
  val user: User
    get() = _user

  fun setUser(user: User) {
    _user = user
  }

  fun isUserInitialized(): Boolean {
    return ::_user.isInitialized
  }

  private var _token: KakaoLoginToken? =
    ApplicationClass.sharedPreferencesUtil.getKakaoLoginToken()
  val token: KakaoLoginToken?
    get() = _token

  fun setToken(token: KakaoLoginToken) {
    _token
  }

  private var _hikingStyles: MutableLiveData<List<Int>> = MutableLiveData()
  val hikingStyles: LiveData<List<Int>>
    get() = _hikingStyles

  fun setHikingStyles(newHikingStyle: List<Int>) {
    _hikingStyles.value = newHikingStyle
  }

  private var _followerList: MutableLiveData<List<FollowUser>> = MutableLiveData()
  val followerList: LiveData<List<FollowUser>>
    get() = _followerList

  fun setFollowerList(followerList: List<FollowUser>) {
    _followerList.value = followerList
  }

  private var _followingList: MutableLiveData<List<FollowUser>> = MutableLiveData()
  val followingList: LiveData<List<FollowUser>>
    get() = _followingList

  fun setFollowingList(followingList: List<FollowUser>) {
    _followingList.value = followingList
  }

  private var _likedMountainList: MutableLiveData<List<Mountain>> = MutableLiveData()
  val likedMountainListList: LiveData<List<Mountain>>
    get() = _likedMountainList

  fun setLikedMountainList(likedMountainList: List<Mountain>) {
    _likedMountainList.value = likedMountainList
  }

  private var _userProfileImgUrl: String = ""
  val userProfileImgUrl: String
    get() = _userProfileImgUrl

  fun setUserProfileImgUrl(userProfileImgUrl: String) {
    _userProfileImgUrl = userProfileImgUrl
  }

  private var _userTitle = 0
  val userTitle: Int
    get() = _userTitle

  fun setUserTitle(userTitle: Int) {
    _userTitle = userTitle
  }

  private var _myPageInfo: MutableLiveData<MyPageInfo> = MutableLiveData()
  val myPageInfo: LiveData<MyPageInfo>
    get() = _myPageInfo

  fun setMyPageInfo(myPageInfo: MyPageInfo) {
    _myPageInfo.value = myPageInfo
  }

}