package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.content.res.ColorStateList
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.data.model.CrewMountainDetail
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabSecondHikingInfoFragment2Binding
import com.sansantek.sansanmulmul.ui.util.Util
import com.sansantek.sansanmulmul.ui.viewmodel.GroupDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date

private const val TAG = "GroupDetailTabSecondHikingInfoFragment_싸피"

class GroupDetailTabSecondHikingInfoFragment(private val crew: Crew) :
  BaseFragment<FragmentGroupDetailTabSecondHikingInfoFragment2Binding>(
    FragmentGroupDetailTabSecondHikingInfoFragment2Binding::bind,
    R.layout.fragment_group_detail_tab_second_hiking_info_fragment2
  ), OnMapReadyCallback {
  private val groupDetailViewModel: GroupDetailViewModel by viewModels()
  private var naverMap: NaverMap? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initMap()
    checkSelectedTabAndSetTabBG()
    initMountainData(binding.cvLayoutTab.selectedTabPosition)

    binding.cvLayoutTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab?) {
        Log.d(TAG, "onTabSelected: ${binding.cvLayoutTab.selectedTabPosition}")
        checkSelectedTabAndSetTabBG()
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
    binding.map.getMapAsync(this)
  }

  private fun initMountainData(selectedTabPosition: Int) {
    groupDetailViewModel.fetchCrewMountainDetail(crew.crewId)

    groupDetailViewModel.crewMountainDetail.observe(viewLifecycleOwner) {
      val crewMountainDetail = groupDetailViewModel.crewMountainDetail.value
      with(binding) {
        if (crewMountainDetail != null) {
          updateUI(crewMountainDetail)

          if (selectedTabPosition == 0) { // 상행
            tvCourseName.text = crewMountainDetail.upCourseName
            btnDifficulty.also {
              it.text = parseDifficultyName(crewMountainDetail.upCourseLevel)
              it.backgroundTintList =
                ColorStateList.valueOf(getDifficultyColor(crewMountainDetail.upCourseLevel))
            }
          } else { // 하행
            tvCourseName.text = crewMountainDetail.downCourseName
            btnDifficulty.also {
              it.text = parseDifficultyName(crewMountainDetail.downCourseLevel)
              it.backgroundTintList =
                ColorStateList.valueOf(getDifficultyColor(crewMountainDetail.downCourseLevel))
            }
          }
        }
      }
    }
  }

  private fun updateUI(crewMountainDetail: CrewMountainDetail) {
    with(binding) {
      tvMountainName.text = crewMountainDetail.mountainName
      tvDescription.text = crewMountainDetail.mountainDescription

      tvExpectedReachTimeTotal.text =
        Util.formatMinutesToHoursAndMinutes(crewMountainDetail.upCoursetime + crewMountainDetail.downCoursetime)
      tvExpectedReachTimeUphill.text =
        Util.formatMinutesToHoursAndMinutes(crewMountainDetail.upCoursetime)
      tvExpectedReachTimeDownhill.text =
        Util.formatMinutesToHoursAndMinutes(crewMountainDetail.downCoursetime)

      tvDifficultyUphill.let {
        Log.d(TAG, "updateUI: ${crewMountainDetail.upCourseLevel}")
        it.text = parseDifficultyName(crewMountainDetail.upCourseLevel)
        it.backgroundTintList =
          ColorStateList.valueOf(getDifficultyColor(crewMountainDetail.upCourseLevel))
      }
      tvDifficultyDownhill.let {
        it.text = parseDifficultyName(crewMountainDetail.downCourseLevel)
        it.backgroundTintList =
          ColorStateList.valueOf(getDifficultyColor(crewMountainDetail.upCourseLevel))

        Log.d(
          TAG,
          "updateUI: color = ${getDifficultyColor(crewMountainDetail.upCourseLevel)}, ${
            resources.getColor(R.color.difficulty_hard)
          }"
        )
      }

      tvDistanceUphill.text = "${crewMountainDetail.upCourseLength}km"
      tvDistanceDownhill.text = "${crewMountainDetail.downCourseLength}km"
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

  fun formatDate(date: Date): String {
    // SimpleDateFormat을 이용하여 "MM/dd" 형식으로 포맷팅
    val dateFormat = SimpleDateFormat("MM/dd")
    return dateFormat.format(date)
  }

  fun getCurrentDate(addDay: Int): Date {
    // Calendar 인스턴스를 통해 현재 날짜 및 시간 가져오기
    val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, addDay) }
    return calendar.time
  }

  private fun checkSelectedTabAndSetTabBG() {
    if (binding.cvLayoutTab.selectedTabPosition == 0) {
      setTabBG(
        R.drawable.group_detail_second_tab_tabbar_left_selected,
        R.drawable.group_detail_second_tab_tabbar_right_unselected
      )
    } else {
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
}