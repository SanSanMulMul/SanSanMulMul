package com.sansantek.sansanmulmul.ui.view

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.sansantek.sansanmulmul.config.BaseActivity
import com.sansantek.sansanmulmul.data.model.KakaoLoginToken
import com.sansantek.sansanmulmul.databinding.ActivityLoginBinding
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.register.RegisterStartFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "LoginActivity 싸피"

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {
    private var token : KakaoLoginToken? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        setContentView(binding.root)
        Log.d(TAG, "onCreate: checkAlreadyLogin ${checkAlreadyLogin()}")
        if(checkAlreadyLogin()){
            lifecycleScope.launch(Dispatchers.IO) {
                Log.d(TAG, "onCreate: token: $token")
                val newToken = userService.refreshToken(makeHeaderByAccessToken(token!!.accessToken))
                Log.d(TAG, "onCreate: result : ${newToken.code()}")
                if(newToken.code() == 200){
                    launch(Dispatchers.IO) {
                        newToken.body()?.let {
                            sharedPreferencesUtil.saveKakaoLoginToken(it)
                        }
                    }
                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    launch(Dispatchers.Main){
                        startActivity(intent)
                        this@LoginActivity.finish()}
                }
                else{
                    launch(Dispatchers.Main) {
                        supportFragmentManager.beginTransaction()
                            .replace(binding.fragmentView.id, RegisterStartFragment()).commit()
                    }
                }
            }
        }
        else{
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentView.id, RegisterStartFragment()).commit()
        }

    }
    private fun checkAlreadyLogin(): Boolean{
        token = sharedPreferencesUtil.getKakaoLoginToken()
        return token != null
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: Login Activity 종료")
        super.onDestroy()
    }
}


