package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainCourse
import com.sansantek.sansanmulmul.data.model.MountainSunriseSunset
import com.sansantek.sansanmulmul.data.model.MountainWeather
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailTabFirstInfoBinding
import com.sansantek.sansanmulmul.databinding.ItemWeatherBinding
import com.sansantek.sansanmulmul.ui.util.Util.dpToPx
import com.sansantek.sansanmulmul.ui.util.Util.formatSunRiseSunSetTime
import com.sansantek.sansanmulmul.ui.util.Util.getNumberWithCommas
import com.sansantek.sansanmulmul.ui.viewmodel.MountainDetailViewModel

private const val TAG = "MountainDetailTabFirstI 싸피"

class MountainDetailTabFirstInfoFragment : BaseFragment<FragmentMountainDetailTabFirstInfoBinding>(
    FragmentMountainDetailTabFirstInfoBinding::bind,
    R.layout.fragment_mountain_detail_tab_first_info
) {
    private var isExpanded = false
    private val mountainDetailViewModel: MountainDetailViewModel by activityViewModels()
    private lateinit var course: MountainCourse

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init() {
        initMountainData()
        initMountainDescriptionToggle()
    }

    private fun initMountainData() {
        val mountainID = mountainDetailViewModel.mountainID.value
        if (mountainID != null) {
            mountainDetailViewModel.fetchMountainDetail(mountainID)
            mountainDetailViewModel.fetchMountainSunriseSunset(mountainID)
            mountainDetailViewModel.fetchMountainWeather(mountainID)
            mountainDetailViewModel.fetchMountainCourse(mountainID)  // 추가
        }

        mountainDetailViewModel.mountainDetail.observe(viewLifecycleOwner) { mountainDetail ->
            val mountainCourse = mountainDetailViewModel.mountainCourse.value
            mountainDetail?.let { setMountainDetailInfo(it, mountainCourse) }
        }

        mountainDetailViewModel.mountainCourse.observe(viewLifecycleOwner) { mountainCourse ->
            mountainDetailViewModel.mountainDetail.value?.let { mountainDetail ->
                setMountainDetailInfo(mountainDetail, mountainCourse)
            }
        }

        mountainDetailViewModel.mountainSunriseSunset.observe(viewLifecycleOwner) {
            mountainDetailViewModel.mountainSunriseSunset.value?.let {
                setMountainSunriseSunsetInfo(it[0])
            }
        }

        mountainDetailViewModel.mountainWeather.observe(viewLifecycleOwner) {
            mountainDetailViewModel.mountainWeather.value?.let { setMountainWeatherInfo(it) }
        }
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

    private fun setMountainDetailInfo(mountain: Mountain, mountainCourse: MountainCourse?) {
        if (mountain != null) {
            with(binding) {
                tvMountainDetail.text = mountain.mountainDescription ?: ""
                tvHeight.text = getNumberWithCommas(mountain.mountainHeight) + "m"
                tvCourseCnt.text = mountainCourse?.courseCount?.takeIf { it > 0 }?.toString() ?: "데이터 없음"

            }
        } else {
            Toast.makeText(context, "response body가 비었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setMountainSunriseSunsetInfo(mountainSunriseSunset: MountainSunriseSunset?) {
        if (mountainSunriseSunset != null) {
            with(binding) {
                tvSunrise.text = formatSunRiseSunSetTime(mountainSunriseSunset.sunrise)
                tvSunset.text = formatSunRiseSunSetTime(mountainSunriseSunset.sunset)
            }
        } else {
            Toast.makeText(context, "response body가 비었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setMountainWeatherInfo(mountainWeatherList: List<MountainWeather>?) {
        if (mountainWeatherList != null) {
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


                    setWeatherIcon(
                        mountainWeatherList[i - 1].description,
                        weatherBinding.ivWeatherImg
                    )
                    weatherBinding.tvWeatherDate.text = mountainWeatherList[i - 1].dayOfWeek
                    weatherBinding.tvWeatherLowerTemperature.text =
                        mountainWeatherList[i - 1].min.toInt().toString() + "℃"
                    weatherBinding.tvWeatherHighestTemperature.text =
                        mountainWeatherList[i - 1].max.toInt().toString() + "℃"
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