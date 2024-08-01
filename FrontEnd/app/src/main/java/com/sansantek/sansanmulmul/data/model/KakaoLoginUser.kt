package com.sansantek.sansanmulmul.data.model

import java.time.LocalDate


data class KakaoLoginUser(val userProviderId: String, val userName: String, val userNickName: String, val userGender: String, val userProfileImg: String, val userBirth: LocalDate, val userIsAdmin: Boolean, val userStyles: List<Int>)
