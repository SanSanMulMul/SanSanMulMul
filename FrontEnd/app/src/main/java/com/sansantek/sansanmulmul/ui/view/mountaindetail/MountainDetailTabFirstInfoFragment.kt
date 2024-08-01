package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
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
import com.sansantek.sansanmulmul.ui.util.Util.formatSunRiseSunSetTime
import com.sansantek.sansanmulmul.ui.util.Util.getNumberWithCommas
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

  private fun init() {
    initMountainData()
    initMountainDescriptionToggle()
  }

  private fun initMountainDescriptionToggle() {
    binding.tvMountainDetail.setOnClickListener {
      val params: ViewGroup.LayoutParams? = binding.tvMountainDetail.layoutParams
      if (isExpanded) {
        if (params != null) {
          binding.tvMountainDetail.ellipsize = TextUtils.TruncateAt.END
          binding.tvMountainDetail.maxLines = 6
          params.height = 150f.dpToPx(requireContext()).toInt()
        }
      } else {
        if (params != null) {
          binding.tvMountainDetail.maxLines = Integer.MAX_VALUE
          params.height = WindowManager.LayoutParams.WRAP_CONTENT
          binding.tvMountainDetail.ellipsize = null

        }
      }
      binding.tvMountainDetail.setLayoutParams(params)
      isExpanded = !isExpanded
    }
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
        tvHeight.text = getNumberWithCommas(responseBody.mountainHeight) + "m"
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


          setWeatherIcon(responseBody[i - 1].description, weatherBinding.ivWeatherImg)
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

  private fun setWeatherIcon(weatherDescription: String, ivWeather: ImageView) {
    when (weatherDescription) {
      "01d", "01n" -> {
        ivWeather.setImageResource(R.drawable.weather_sunny)
      }

      "02d", "02n", "03n", "03d", "04d", "04n" -> {
        ivWeather.setImageResource(R.drawable.weather_cloud)
      }

      "09d", "09n", "10d", "10n" -> {
        ivWeather.setImageResource(R.drawable.weather_rainy)
      }

      "11d", "11n" -> {
        ivWeather.setImageResource(R.drawable.weather_lightning)
      }

      "13d", "13n" -> {
        ivWeather.setImageResource(R.drawable.weather_snow)
      }

      "50d", "50n" -> {
        ivWeather.setImageResource(R.drawable.weather_mist)
      }

      else -> {
        ivWeather.setImageDrawable(null)
      }
    }

  }

}