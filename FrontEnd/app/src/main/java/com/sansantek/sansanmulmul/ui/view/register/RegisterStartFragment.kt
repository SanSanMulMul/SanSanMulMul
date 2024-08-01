package com.sansantek.sansanmulmul.ui.view.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentRegisterStartBinding
import com.sansantek.sansanmulmul.ui.view.MainActivity
import kotlinx.coroutines.launch


private const val TAG = "RegisterStartFragment_싸피"

class RegisterStartFragment : BaseFragment<FragmentRegisterStartBinding>(
  FragmentRegisterStartBinding::bind,
  R.layout.fragment_register_start
) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    lifecycleScope.launch {
//      val result = mountainService.getAllMountainList(1)
//      Log.d(TAG, "onViewCreated: $result")
    }
    Log.d(TAG, "onViewCreated: ")
    binding.button.setOnClickListener {
      Log.d(TAG, "onViewCreated: 로그인 시도")
      // 카카오 로그인 시도
//            loginWithKakao()
      startActivity(Intent(context, MainActivity::class.java)) // TODO : 이건 나중에 빼유~~~

    }
  }

  private fun loginWithKakao() {
    // 카카오계정으로 로그인 공통 callback 구성
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
      if (error != null) {
        Log.e(TAG, "카카오계정으로 로그인 실패", error)
      } else if (token != null) {
        Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
        GoMain()
      }
    }

    // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
    if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
      UserApiClient.instance.loginWithKakaoTalk(requireActivity()) { token, error ->
        if (error != null) {
          Log.e(TAG, "카카오톡으로 로그인 실패", error)

          // 사용자가 로그인 과정을 취소한 경우
          if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
            return@loginWithKakaoTalk
          }

          // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
          UserApiClient.instance.loginWithKakaoAccount(requireActivity(), callback = callback)
        } else if (token != null) {
          Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
          GoMain()
        }
      }
    } else {
      // 카카오 계정으로 로그인 시도
      UserApiClient.instance.loginWithKakaoAccount(requireActivity(), callback = callback)
      // Redirect URI 설정
      val redirectUri = "http://localhost:8080/user/login" // 실제 리디렉션 URI를 여기에 입력하세요
      val clientId = "7b5d50287be135923631b5d5c05be956" // 실제 클라이언트 ID를 여기에 입력하세요
      val authCodeUrl =
        "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}\n"

      // authCodeUrl로 이동
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authCodeUrl))
      startActivity(intent)
    }
  }

  private fun GoMain() {
    // 메인 화면으로 이동하는 로직을 구현합니다.
    // 예: 메인 액티비티를 시작하거나 프래그먼트를 교체하는 코드
    Log.i(TAG, "GoMain() 호출")
  }
}
