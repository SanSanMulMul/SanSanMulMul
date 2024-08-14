package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PolylineOverlay
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.data.model.CrewMountainDetail
import com.sansantek.sansanmulmul.data.model.DownCourseTrackPath
import com.sansantek.sansanmulmul.data.model.MountainWeather
import com.sansantek.sansanmulmul.data.model.UpCourseTrackPath
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabSecondHikingInfoFragment2Binding
import com.sansantek.sansanmulmul.databinding.ItemWeatherBinding
import com.sansantek.sansanmulmul.ui.util.Util
import com.sansantek.sansanmulmul.ui.viewmodel.GroupDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainDetailViewModel

private const val TAG = "GroupDetailTabSecondHikingInfoFragment_싸피"

class GroupDetailTabSecondHikingInfoFragment(private val crew: Crew) :
  BaseFragment<FragmentGroupDetailTabSecondHikingInfoFragment2Binding>(
    FragmentGroupDetailTabSecondHikingInfoFragment2Binding::bind,
    R.layout.fragment_group_detail_tab_second_hiking_info_fragment2
  ), OnMapReadyCallback {
  private val groupDetailViewModel: GroupDetailViewModel by viewModels()
  private val mountainDetailViewModel: MountainDetailViewModel by viewModels()
  private var naverMap: NaverMap? = null
  private val polylineOverlays = mutableListOf<PolylineOverlay>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initTabLayout()
    initMap()
    checkSelectedTabAndSetTabBG()
    initMountainData(binding.cvLayoutTab.selectedTabPosition)
    initMapButtonToggle()
  }

  private fun initTabLayout() {
    binding.cvLayoutTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab?) {
        Log.d(TAG, "onTabSelected: ${binding.cvLayoutTab.selectedTabPosition}")
        checkSelectedTabAndSetTabBG()
        groupDetailViewModel.crewMountainDetail.value?.let {
          updateUpOrDownCourseUI(
            it,
            binding.cvLayoutTab.selectedTabPosition
          )
        }
      }

      override fun onTabUnselected(tab: TabLayout.Tab?) {
        Log.d(TAG, "onTabSelected: ${binding.cvLayoutTab.selectedTabPosition}")
        checkSelectedTabAndSetTabBG()
      }

      override fun onTabReselected(tab: TabLayout.Tab?) {
        checkSelectedTabAndSetTabBG()
      }
    })
  }

  private fun initMap() {
    val mapFragment = childFragmentManager.findFragmentById(R.id.map) as MapFragment?
      ?: MapFragment.newInstance().also {
        childFragmentManager.beginTransaction().add(R.id.map, it).commit()
      }

    mapFragment.getMapAsync(this)
  }

  private fun initMapButtonToggle() {
    val layoutMap = binding.layoutMap
    val layoutDescription = binding.layoutDescription

    binding.btnToDescription.setOnClickListener {
      // 설명보기 화면으로 들어온 상태
      layoutDescription.visibility = View.VISIBLE
      binding.btnToDescription.isClickable = false
      binding.btnToDescription.isEnabled = false
    }

    binding.btnToMap.setOnClickListener {
      // 지도 화면으로 들어온 상태
      layoutDescription.visibility = View.GONE
      layoutMap.visibility = View.VISIBLE
      binding.btnToDescription.isClickable = true
      binding.btnToDescription.isEnabled = true
    }

  }

  private fun initMountainData(selectedTabPosition: Int) {
    groupDetailViewModel.fetchCrewMountainDetail(crew.crewId)

    groupDetailViewModel.crewMountainDetail.observe(viewLifecycleOwner) {
      val crewMountainDetail = groupDetailViewModel.crewMountainDetail.value
      if (crewMountainDetail != null) {
        mountainDetailViewModel.fetchMountainWeather(crewMountainDetail.mountainId)
        updateUI(crewMountainDetail)
        updateUpOrDownCourseUI(crewMountainDetail, selectedTabPosition)
      }
    }

    mountainDetailViewModel.mountainWeather.observe(viewLifecycleOwner) {
      mountainDetailViewModel.mountainWeather.value?.let { updateWeatherUI(it) }
    }
  }

  private fun updateWeatherUI(mountainWeatherList: List<MountainWeather>?) {
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

  private fun updateUpOrDownCourseUI(
    crewMountainDetail: CrewMountainDetail,
    selectedTabPosition: Int
  ) {
    with(binding) {
      if (selectedTabPosition == 0) { // 상행
        tvCourseName.text = crewMountainDetail.upCourseName
        btnDifficulty.also {
          it.text = parseDifficultyName(crewMountainDetail.upCourseLevel)
          it.backgroundTintList =
            ColorStateList.valueOf(getDifficultyColor(crewMountainDetail.upCourseLevel))
        }
        drawUpCourseOnMap(crewMountainDetail.upCourseTrackPaths, crewMountainDetail.upCourseLevel)

      } else { // 하행
        tvCourseName.text = crewMountainDetail.downCourseName
        btnDifficulty.also {
          it.text = parseDifficultyName(crewMountainDetail.downCourseLevel)
          it.backgroundTintList =
            ColorStateList.valueOf(getDifficultyColor(crewMountainDetail.downCourseLevel))
        }
        drawDownCourseOnMap(
          crewMountainDetail.downCourseTrackPaths,
          crewMountainDetail.downCourseLevel
        )
      }
    }

  }

  private fun updateUI(crewMountainDetail: CrewMountainDetail) {
    with(binding) {
      // 산 이름, 산 설명
      tvMountainName.text = crewMountainDetail.mountainName
      tvDescription.text = crewMountainDetail.mountainDescription

      // 예상 소요 시간
      tvExpectedReachTimeTotal.text =
        Util.formatMinutesToHoursAndMinutes(crewMountainDetail.upCoursetime + crewMountainDetail.downCoursetime)
      tvExpectedReachTimeUphill.text =
        Util.formatMinutesToHoursAndMinutes(crewMountainDetail.upCoursetime)
      tvExpectedReachTimeDownhill.text =
        Util.formatMinutesToHoursAndMinutes(crewMountainDetail.downCoursetime)

      // 난이도
      tvDifficultyUphill.let {
        Log.d(TAG, "updateUI: ${crewMountainDetail.upCourseLevel}")
        it.text = parseDifficultyName(crewMountainDetail.upCourseLevel)
        it.backgroundTintList =
          ColorStateList.valueOf(getDifficultyColor(crewMountainDetail.upCourseLevel))
      }
      tvDifficultyDownhill.let {
        it.text = parseDifficultyName(crewMountainDetail.downCourseLevel)
        it.backgroundTintList =
          ColorStateList.valueOf(getDifficultyColor(crewMountainDetail.downCourseLevel))
      }

      // 거리
      tvTotalDistance.text =
        "총 ${crewMountainDetail.upCourseLength + crewMountainDetail.downCourseLength}km"
      tvDistanceUphill.text = "${crewMountainDetail.upCourseLength}km"
      tvDistanceDownhill.text = "${crewMountainDetail.downCourseLength}km"
    }
  }

  private fun drawUpCourseOnMap(course: List<UpCourseTrackPath>, difficulty: String) {
    clearPolylines()
    var zIterator = 1
    val boundsBuilder = LatLngBounds.Builder()

    course.forEach { track ->
      val path =
        track.trackPaths.map { LatLng(it.trackPathLat, it.trackPathLon) }
      path.forEach { boundsBuilder.include(it) }

      val polyline = PolylineOverlay().apply {
        coords = path
        color = getPolyLineColor(difficulty)
        width = 20
        zIndex = zIterator++
      }
      polyline.map = naverMap
      polylineOverlays.add(polyline)
    }

    val latLngBounds = boundsBuilder.build()
    naverMap?.moveCamera(CameraUpdate.fitBounds(latLngBounds, 100))
  }

  private fun drawDownCourseOnMap(course: List<DownCourseTrackPath>, difficulty: String) {
    clearPolylines()

    var zIterator = 1
    val boundsBuilder = LatLngBounds.Builder()

    course.forEach { track ->
      val path =
        track.trackPaths.map { LatLng(it.trackPathLat, it.trackPathLon) }
      path.forEach { boundsBuilder.include(it) }

      val polyline = PolylineOverlay().apply {
        coords = path
        color = getPolyLineColor(difficulty)
        width = 20
        zIndex = zIterator++
      }
      polyline.map = naverMap
      polylineOverlays.add(polyline)
    }

    val latLngBounds = boundsBuilder.build()
    naverMap?.moveCamera(CameraUpdate.fitBounds(latLngBounds, 100))
  }

  private fun clearPolylines() {
    polylineOverlays.forEach { it.map = null }
    polylineOverlays.clear()
  }

  private fun getPolyLineColor(difficulty: String): Int {
    return when (difficulty) {
      "EASY" -> resources.getColor(R.color.chip_course_difficulty_easy)
      "MEDIUM" -> resources.getColor(R.color.chip_course_difficulty_medium)
      "HARD" -> resources.getColor(R.color.chip_course_difficulty_hard)
      else -> {
        resources.getColor(R.color.grey)
      }
    }
  }

  private fun parseDifficultyName(difficulty: String): String {
    return when (difficulty) {
      "HARD" -> "어려움"
      "MEDIUM" -> "보통"
      "EASY" -> "쉬움"
      else -> "데이터 없음"
    }
  }

  private fun getDifficultyColor(difficulty: String): Int {
    Log.d(TAG, "getDifficultyColor: difficulty = $difficulty")
    return when (difficulty) {
      "HARD" -> resources.getColor(R.color.difficulty_hard)
      "MEDIUM" -> resources.getColor(R.color.difficulty_medium)
      "EASY" -> resources.getColor(R.color.difficulty_easy)
      else -> resources.getColor(R.color.white)
    }
  }

  private fun checkSelectedTabAndSetTabBG() {
    if (binding.cvLayoutTab.selectedTabPosition == 0) {
      binding.layoutDescription.visibility = View.GONE
      binding.btnToDescription.isClickable = true
      binding.btnToDescription.isEnabled = true
      setTabBG(
        R.drawable.group_detail_second_tab_tabbar_left_selected,
        R.drawable.group_detail_second_tab_tabbar_right_unselected
      )
    } else {
      binding.layoutDescription.visibility = View.GONE
      binding.btnToDescription.isClickable = true
      binding.btnToDescription.isEnabled = true
      setTabBG(
        R.drawable.group_detail_second_tab_tabbar_left_unselected,
        R.drawable.group_detail_second_tab_tabbar_right_selected
      )
    }
  }

  private fun setTabBG(tab1: Int, tab2: Int) {
    val tabStrip = binding.cvLayoutTab
    val tabView1 = tabStrip.getTabAt(0)?.view
    val tabView2 = tabStrip.getTabAt(1)?.view

    tabView1?.setBackgroundResource(tab1)
    tabView2?.setBackgroundResource(tab2)
  }

  override fun onMapReady(naverMap: NaverMap) {
    this.naverMap = naverMap
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