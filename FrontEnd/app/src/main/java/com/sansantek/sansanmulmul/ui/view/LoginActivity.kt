package com.sansantek.sansanmulmul.ui.view

import android.app.Application
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.sansantek.sansanmulmul.config.BaseActivity
import com.sansantek.sansanmulmul.databinding.ActivityLoginBinding
import com.sansantek.sansanmulmul.ui.view.register.RegisterStartFragment

private const val TAG = "MainActivity 싸피"

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentView.id, RegisterStartFragment()).commit()
    }
}


