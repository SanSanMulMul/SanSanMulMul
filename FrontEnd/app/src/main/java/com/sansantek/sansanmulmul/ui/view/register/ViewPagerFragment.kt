package com.sansantek.sansanmulmul.ui.view.register

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.KakaoLoginUser
import com.sansantek.sansanmulmul.databinding.FragmentLoginViewPagerBinding
import com.sansantek.sansanmulmul.ui.adapter.LoginViewPagerAdapter
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.util.Util.getRealPathFromURI
import com.sansantek.sansanmulmul.ui.viewmodel.LoginActivityViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


private const val TAG = "ViewPagerFragment 싸피"

class ViewPagerFragment : BaseFragment<FragmentLoginViewPagerBinding>(
    FragmentLoginViewPagerBinding::bind,
    R.layout.fragment_login_view_pager
) {
    private val activityViewModel: LoginActivityViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = LoginViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        binding.springDotsIndicator.attachTo(binding.viewPager)

        checkViewPageLimit()

        binding.nextButton.setOnClickListener {
            Log.d(
                TAG,
                "onViewCreated: ${binding.viewPager.currentItem} ${binding.viewPager.adapter!!.itemCount} "
            )
            if (binding.viewPager.currentItem == binding.viewPager.adapter!!.itemCount - 2) {
                // 회원가입 완료 페이지 나오기 바로 전 => 등산 스타일 정하는 페이지에서 다음 버튼을 눌렀을 때
                lifecycleScope.launch {
                    activityViewModel.setKakaoLoginUser(
                        KakaoLoginUser(
                            activityViewModel.user.id.toString(),
                            activityViewModel.userName.value!!,
                            activityViewModel.userNickname.value!!,
                            activityViewModel.userGender.value!!,
                            activityViewModel.user.kakaoAccount!!.profile!!.profileImageUrl!!,
                            activityViewModel.userBirth.value!!,
                            false,
                            activityViewModel.userStyles.value!!
                        )
                    )
                    Log.d(TAG, "onViewCreated: ${activityViewModel.kakaoLoginUser}")

                    getRealPathFromURI(requireContext(), activityViewModel.uri).let { file ->
                        val body = if (file == null) {
                            MultipartBody.Part.createFormData(
                                "image",
                                "",
                                RequestBody.create("image/*".toMediaTypeOrNull(), ByteArray(0))
                            )
                        } else {
                            val file = File(file)
                            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                            MultipartBody.Part.createFormData("image", file.name, requestBody)
                        }
                        val result = userService.registerUser(body, activityViewModel.kakaoLoginUser)
                        Log.d(TAG, "onViewCreated: 회원가입 결과 $result")
                        if (result.code() == 200) {
                            Log.d(TAG, "onViewCreated: 회원가입 성공! ${result.body()}")
                            result.body()?.let { it ->
                                val loginResult = userService.loginUser(it.userId.toString())
                                if (loginResult.code() == 200) {
                                    Log.d(TAG, "onViewCreated: 로그인 성공 ${loginResult.body()}")
                                    loginResult.body()?.let { token ->
                                        // 토큰 정보 저장
                                        sharedPreferencesUtil.saveKakaoLoginToken(token)
                                        activityViewModel.setLoginToken(token)
                                    }

                                }
                            }
                        }
                    }
                }
            }
            binding.viewPager.currentItem += 1
            checkViewPageLimit()
        }
        binding.prevButton.setOnClickListener {
            binding.viewPager.currentItem -= 1
            checkViewPageLimit()
        }


    }

    private fun checkViewPageLimit() {
        val itemCount = binding.viewPager.adapter?.itemCount ?: 0
        Log.d(TAG, "checkViewPageLimit: ${binding.viewPager.childCount}")
        if (binding.viewPager.currentItem == itemCount - 1) {
            binding.nextButton.visibility = View.GONE
            binding.prevButton.visibility = View.GONE
            binding.progressButton.visibility = View.GONE
        } else if (binding.viewPager.currentItem == 0) {
            binding.nextButton.visibility = View.VISIBLE
            binding.prevButton.visibility = View.INVISIBLE
        } else {
            binding.nextButton.visibility = View.VISIBLE
            binding.prevButton.visibility = View.VISIBLE
        }
    }

    fun changeNextButtonVisibility(visibility: Int) {
        binding.nextButton.visibility = visibility
    }

    fun enableNextButton(enable: Boolean) {
        if (enable) {
            binding.nextButton.imageTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.white)
            binding.nextButton.background.setTintList(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.green
                )
            )
        } else {
            binding.nextButton.imageTintList = ColorStateList.valueOf(Color.parseColor("#FF373737"))
            binding.nextButton.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.gray_border_rounded_box)
        }
        binding.nextButton.isEnabled = enable
    }

    fun changePrevButtonVisibility(visibility: Int) {
        binding.prevButton.visibility = visibility
    }

    fun enablePrevButton(enable: Boolean) {
        binding.prevButton.isEnabled = enable
    }
}