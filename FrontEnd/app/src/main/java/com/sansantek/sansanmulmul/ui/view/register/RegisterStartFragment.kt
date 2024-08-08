package com.sansantek.sansanmulmul.ui.view.register

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.KakaoLoginToken
import com.sansantek.sansanmulmul.databinding.FragmentRegisterStartBinding
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.view.LoginActivity
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.viewmodel.LoginActivityViewModel
import kotlinx.coroutines.launch


private const val TAG = "RegisterStartFragment_싸피"

class RegisterStartFragment : BaseFragment<FragmentRegisterStartBinding>(
    FragmentRegisterStartBinding::bind,
    R.layout.fragment_register_start
) {
    private lateinit var kakaoLoginToken: KakaoLoginToken
    private val activityViewModel: LoginActivityViewModel by activityViewModels()
    private lateinit var activity: LoginActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as LoginActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            Log.d(TAG, "onViewCreated: 로그인 시도")
            // 카카오 로그인 시도
            loginWithKakao()
//                goRegister()
//            goMain()
        }
    }

    private fun loginWithKakao() {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(myContext)) {
            loginWithKakaoApp()
        } else {
            loginWithKakaoAccount()
        }
    }

    // 카카오 앱으로 로그인하는 로직
    private fun loginWithKakaoApp() {
        UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오톡으로 로그인 실패", error)
                // 사용자가 로그인 과정을 취소한 경우
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    return@loginWithKakaoTalk
                }
                // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                UserApiClient.instance.loginWithKakaoAccount(
                    activity,
                    callback = kakaoLoginCommonCallBack
                )
            } else if (token != null) {
                Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                Log.i(TAG, "카카오톡으로 로그인 성공 ${token.idToken}")
                processAuthAndNavigateByKakao()
            }
        }
    }

    // 회원인지 파악하고 아니면 회원가입 페이지, 맞으면 바로 메인 화면으로 이동
    private fun processAuthAndNavigateByKakao() {
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공 $user")
                // null 아닌 거 확인되면 LoginActivityViewModel에 user정보 넘겨서 세팅
                Log.d(TAG, "processAuthAndNavigateByKakaoApp: user ${user}")
                activityViewModel.setUser(user)
                Log.d(TAG, "processAuthAndNavigateByKakaoApp: ${activityViewModel.user}")
                processAuthAndNavigate()
            }
        }
    }

    private fun processAuthAndNavigate() {
        lifecycleScope.launch {
            if (isUser()) {
                goMain()
            } else {
                goRegister()
            }
        }
    }

    private suspend fun isUser(): Boolean {
        val user = activityViewModel.user
        val userId = user.id
        val nickname = user.kakaoAccount?.profile?.nickname ?: ""
        val image = user.kakaoAccount?.profile?.nickname ?: ""
        val result = userService.loginUser(userId.toString())
        if (result.code() == 200) {
            result.body()?.let {
                kakaoLoginToken = it
                sharedPreferencesUtil.saveKakaoLoginToken(kakaoLoginToken)
                Log.d(TAG, "isUser: $kakaoLoginToken")
            }
            return true
        }
        return false
    }

    private fun goMain() {
        // 메인 화면으로 이동하는 로직을 구현합니다.
        // 예: 메인 액티비티를 시작하거나 프래그먼트를 교체하는 코드
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity.finish()
        Log.i(TAG, "GoMain() 호출")
    }

    private fun goRegister() {
        // 회원 가입으로 이동하는 로직을 구현합니다.
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_view, ViewPagerFragment()).commitAllowingStateLoss()
        Log.i(TAG, "GoRegister() 호출")
    }

    private fun loginWithKakaoAccount() {
        Log.d(TAG, "loginWithKakao: 카카오 계정으로 로그인 시도")
        // 카카오 계정으로 로그인 시도
        UserApiClient.instance.loginWithKakaoAccount(activity, callback = kakaoLoginCommonCallBack)
        // Redirect URI 설정
        val redirectUri = "http://localhost:8080/user/login" // 실제 리디렉션 URI를 여기에 입력하세요
        val clientId = "7b5d50287be135923631b5d5c05be956" // 실제 클라이언트 ID를 여기에 입력하세요
        val authCodeUrl =
            "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}\n"

        // authCodeUrl로 이동
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authCodeUrl))
        startActivity(intent)
    }

    // 카카오계정으로 로그인 공통 callback 구성
    private val kakaoLoginCommonCallBack: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            Log.d(TAG, "loginWithKakao idToken: ${token.idToken}")
            processAuthAndNavigateByKakao()
//                GoMain()
        }
    }
}
