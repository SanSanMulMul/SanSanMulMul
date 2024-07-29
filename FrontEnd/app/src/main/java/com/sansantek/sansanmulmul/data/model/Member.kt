package com.sansantek.sansanmulmul.data.model

import android.net.Uri

data class Member(val title:String, var name: String, var registration: Boolean, var imageUri: Uri? = null) {
}
