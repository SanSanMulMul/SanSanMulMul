package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.MountainDto
import com.sansantek.sansanmulmul.data.model.SunriseSunsetTimes
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailTabFirstInfoBinding
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.util.Util.dpToPx
import com.sansantek.sansanmulmul.ui.util.Util.formatNumberWithCommas
import com.sansantek.sansanmulmul.ui.util.Util.formatSunRiseSunSetTime
import kotlinx.coroutines.launch

private const val TAG = "MountainDetailTabFirstI"

class MountainDetailTabFirstInfoFragment : BaseFragment<FragmentMountainDetailTabFirstInfoBinding>(
  FragmentMountainDetailTabFirstInfoBinding::bind,
  R.layout.fragment_mountain_detail_tab_first_info
) {
  private var isExpanded = false

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    init()
  }

  @SuppressLint("ClickableViewAccessibility")
  private fun init() {
    initMountainData()

    // 산 설명 - 스크롤
    binding.tvMountainDetail.setOnClickListener(View.OnClickListener {
      val params: ViewGroup.LayoutParams? = binding.tvMountainDetail.layoutParams
      if (isExpanded) {
        if (params != null) {
          params.height = 150f.dpToPx(requireContext()).toInt()
        }
      } else {
        if (params != null) {
          params.height = WindowManager.LayoutParams.WRAP_CONTENT
        }
      }
      binding.tvMountainDetail.setLayoutParams(params)
      isExpanded = !isExpanded
    })
  }

  private fun initMountainData() {
    lifecycleScope.launch {
      val response = mountainService.getMountainDetailByID(1)
      if (response.isSuccessful) {
        setMountainDetailInfo(response.body())
        Log.d(TAG, "initMountainData: $response")
      }
    }
    lifecycleScope.launch {
      val response = mountainService.getMountainSunriseAndSunSetTimeByID(1)
      if (response.isSuccessful) {
        setMountainSunriseSunsetInfo(response.body())
        Log.d(TAG, "initMountainData: $response")
      }
    }
  }

  private fun setMountainDetailInfo(responseBody: MountainDto?) {
    if (responseBody != null) {
      with(binding) {
        tvMountainDetail.text = responseBody.mountainDescription ?: ""
        tvHeight.text = formatNumberWithCommas(responseBody.mountainHeight) + "m"
//        tvCourseCnt.text = responseBody.
      }
    } else {
      Toast.makeText(context, "response body가 비었습니다.", Toast.LENGTH_SHORT).show()
    }
  }

  private fun setMountainSunriseSunsetInfo(responseBody: List<SunriseSunsetTimes>?) {
    if (responseBody != null) {
      with(binding) {
        tvSunrise.text = formatSunRiseSunSetTime(responseBody[0].sunrise)
        tvSunset.text = formatSunRiseSunSetTime(responseBody[0].sunset)
      }
    } else {
      Toast.makeText(context, "response body가 비었습니다.", Toast.LENGTH_SHORT).show()
    }

  }

}