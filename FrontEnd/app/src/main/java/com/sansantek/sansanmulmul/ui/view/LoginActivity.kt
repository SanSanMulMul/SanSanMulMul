package com.sansantek.sansanmulmul.ui.view

import android.app.Application
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
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

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "{NATIVE_APP_KEY}")
    }
}