package com.sansantek.sansanmulmul.ui.view.register

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.KakaoLoginUser
import com.sansantek.sansanmulmul.databinding.FragmentRegisterStartBinding
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import java.time.LocalDate


private const val TAG = "RegisterStartFragment_싸피"
class RegisterStartFragment : BaseFragment<FragmentRegisterStartBinding>(
    FragmentRegisterStartBinding::bind,
    R.layout.fragment_register_start
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch { 
            val result = mountainService.getAllMountainList(1)
            Log.d(TAG, "onViewCreated: $result")
        }
        Log.d(TAG, "onViewCreated: ")
        binding.button.setOnClickListener {
            Log.d(TAG, "onViewCreated: 로그인 시도")
            // 카카오 로그인 시도
            loginWithKakao()
        }
    }

    private fun loginWithKakao() {
        // 카카오계정으로 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                Log.d(TAG, "loginWithKakao idToken: ${token.idToken}")
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
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.idToken}")
                    // 사용자 정보 요청 (기본)
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e(TAG, "사용자 정보 요청 실패", error)
                        }
                        else if (user != null) {
                            Log.i(TAG, "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                            user.kakaoAccount?.profile?.let {
                                val userId = user.id
                                val nickname = it.nickname!!
                                val image = it.thumbnailImageUrl!!
                                lifecycleScope.launch {
                                    try{
                                        token.idToken
                                        Log.d(TAG, "loginWithKakao: ${KakaoLoginUser(userId.toString(), nickname, nickname, "M", image, LocalDate.of(2000, 1, 11), true, mutableListOf(0))}")
                                        val result = userService.registerUser(KakaoLoginUser(userId.toString(), nickname, nickname, "M", image, LocalDate.of(2000, 1, 11), true, mutableListOf(0)))
                                        Log.d(TAG, "loginWithKakao:result.code : ${result.code()}    result.body : ${result.body()}")
                                    } catch (e: HttpException){
                                        // HTTP 상태 코드가 200-299가 아닌 경우 EX : 404 등
                                        Log.e(TAG, "loginWithKakao: 토큰 발급 에러" )
                                    } catch (e: IOException){
                                        Log.e(TAG, "loginWithKakao: Network Error ${e.message}", )
                                    }
                                    Log.d(TAG, "loginWithKakao: $")

                                }
                            }

                        }
                    }
                    GoMain()
                }
            }
        } else {
            // 카카오 계정으로 로그인 시도
            UserApiClient.instance.loginWithKakaoAccount(requireActivity(), callback = callback)
            // Redirect URI 설정
            val redirectUri = "http://localhost:8080/user/login" // 실제 리디렉션 URI를 여기에 입력하세요
            val clientId = "7b5d50287be135923631b5d5c05be956" // 실제 클라이언트 ID를 여기에 입력하세요
            val authCodeUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}\n"

            // authCodeUrl로 이동
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authCodeUrl))
            startActivity(intent)
        }
    }

    private fun getKakaoUserInfo(){

    }

    private fun GoMain() {
        // 메인 화면으로 이동하는 로직을 구현합니다.
        // 예: 메인 액티비티를 시작하거나 프래그먼트를 교체하는 코드
        Log.i(TAG, "GoMain() 호출")
    }
}
