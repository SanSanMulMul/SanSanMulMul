package com.sansantek.sansanmulmul.data.model

import android.media.Image
import com.kakao.sdk.common.KakaoSdk.type

data class MessageData(
    val type: String,
    val from: String,
    val to: String,
    val content: String,
    val sendTime: Long,
    val image: Image,
)
