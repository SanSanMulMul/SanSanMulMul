package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.MountainDto
import com.sansantek.sansanmulmul.data.model.MountainWeather
import com.sansantek.sansanmulmul.data.model.SunriseSunsetTimes
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailTabFirstInfoBinding
import com.sansantek.sansanmulmul.databinding.ItemWeatherBinding
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
    // 산 설명, 고도, 코스 개수
    lifecycleScope.launch {
      val response = mountainService.getMountainDetailByID(1)
      if (response.isSuccessful) {
        setMountainDetailInfo(response.body())
      }
    }

    // 일출, 일몰 시간
    lifecycleScope.launch {
      val response = mountainService.getMountainSunriseAndSunSetTimeByID(1)
      if (response.isSuccessful) {
        setMountainSunriseSunsetInfo(response.body()?.get(0))
      }
    }

    // 날씨
    lifecycleScope.launch {
      val response = mountainService.getMountainWeatherByID(1)
      if (response.isSuccessful) {
        setMountainWeatherInfo(response.body())
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

  private fun setMountainSunriseSunsetInfo(responseBody: SunriseSunsetTimes?) {
    if (responseBody != null) {
      with(binding) {
        tvSunrise.text = formatSunRiseSunSetTime(responseBody.sunrise)
        tvSunset.text = formatSunRiseSunSetTime(responseBody.sunset)
      }
    } else {
      Toast.makeText(context, "response body가 비었습니다.", Toast.LENGTH_SHORT).show()
    }
  }

  private fun setMountainWeatherInfo(responseBody: List<MountainWeather>?) {
    if (responseBody != null) {
      with(binding) {
        for (i in 1..5) {
          val weatherView = root.findViewById<View>(
            resources.getIdentifier(
              "weather$i",
              "id",
              requireActivity().packageName
            )
          )
          val weatherBinding = ItemWeatherBinding.bind(weatherView)

          weatherBinding.tvWeatherDate.text = responseBody[i - 1].dayOfWeek
          weatherBinding.tvWeatherLowerTemperature.text =
            responseBody[i - 1].min.toInt().toString() + "℃"
          weatherBinding.tvWeatherHighestTemperature.text =
            responseBody[i - 1].max.toInt().toString() + "℃"
        }

      }
    } else {
      Toast.makeText(context, "response body가 비었습니다.", Toast.LENGTH_SHORT).show()
    }
  }

}