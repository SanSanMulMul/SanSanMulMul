package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PolylineOverlay
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.config.Const.Companion.COURSE_TAB
import com.sansantek.sansanmulmul.config.Const.Companion.INFO_TAB
import com.sansantek.sansanmulmul.data.model.CourseDetail
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailTabSecondCourseBinding
import com.sansantek.sansanmulmul.ui.adapter.MountainDetailCourseListAdapter
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.view.coursedetail.CourseDetailFragment
import com.sansantek.sansanmulmul.ui.viewmodel.CourseDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainDetailViewModel

private const val TAG = "싸피_MountainDetailTabSecond"

class MountainDetailTabSecondCourseFragment :
  BaseFragment<FragmentMountainDetailTabSecondCourseBinding>(
    FragmentMountainDetailTabSecondCourseBinding::bind,
    R.layout.fragment_mountain_detail_tab_second_course
  ), OnMapReadyCallback {
  private lateinit var rootActivity: MainActivity
  private lateinit var courseList: List<CourseDetail>
  private val mountainDetailViewModel: MountainDetailViewModel by activityViewModels()
  private val courseDetailViewModel: CourseDetailViewModel by activityViewModels()
  private lateinit var courseListAdapter: MountainDetailCourseListAdapter
  private lateinit var naverMap: NaverMap
  private val filteringMap = mapOf(
    R.id.chip_easy to "EASY",
    R.id.chip_medium to "MEDIUM",
    R.id.chip_hard to "HARD",
    R.id.chip_all to "ALL"
  )

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    initCourse()
    initCourseDifficultyChipGroup()
    initCourseMap()
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    if (context is MainActivity) {
      rootActivity = context
    }
  }

  private fun initCourse() {
    val mountainId = mountainDetailViewModel.mountainID.value

    if (mountainId != null) {
      mountainDetailViewModel.mountainCourse.value?.let {
        courseDetailViewModel.fetchCourseDetail(mountainId, it.courseIds)
      }
    }

    courseDetailViewModel.courseDetails.observe(viewLifecycleOwner) {
      val courseDetails = courseDetailViewModel.courseDetails.value
      if (courseDetails != null) {
        courseList = courseDetails
        courseListAdapter.submitList(courseList)
      }
    }

    initCourseRecyclerView()
  }

  private fun initCourseRecyclerView() {
    val courseRecyclerView = binding.rvMountainDetailCourses

    courseListAdapter = MountainDetailCourseListAdapter(
      object : MountainDetailCourseListAdapter.OnItemClickListener {
        override fun onItemClick(course: CourseDetail) {
          courseDetailViewModel.setCourseID(course.courseId)
          drawPolyLineOnMap(course)
        }

        override fun onCourseInfoBtnClick(course: CourseDetail) {
          mountainDetailViewModel.setPrevTab(COURSE_TAB)
          courseDetailViewModel.setCourseID(course.courseId)
          Log.d(TAG, "onCourseInfoBtnClick: ${course.courseId}")
          changeFragmentWithSlideRightAnimation(CourseDetailFragment())
        }
      })

    courseRecyclerView.layoutManager = LinearLayoutManager(context)
    courseRecyclerView.adapter = courseListAdapter

    val dividerDrawable = activity?.getDrawable(R.drawable.recyclerview_divider_lightgray)
    val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    if (dividerDrawable != null) dividerItemDecoration.setDrawable(dividerDrawable)
    courseRecyclerView.addItemDecoration(dividerItemDecoration)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    mountainDetailViewModel.setPrevTab(INFO_TAB)
  }

  private fun drawPolyLineOnMap(course: CourseDetail) {
    val trackPaths =
      course.tracks[0].trackPaths.map { LatLng(it.trackPathLat, it.trackPathLon) }

    val polyline = PolylineOverlay().apply {
      map = null
      coords = trackPaths
      color = getPolyLineColor(course.courseLevel)
      width = 20
      zIndex = 1
    }
    polyline.map = naverMap

    if (trackPaths.isNotEmpty()) {
      val boundsBuilder = LatLngBounds.Builder()
      polyline.coords.forEach { latLng -> boundsBuilder.include(latLng) }
      val latLngBounds = boundsBuilder.build()
      naverMap.moveCamera(CameraUpdate.fitBounds(latLngBounds, 100))
    }
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

  private fun initCourseMap() {
    binding.mountainDetailCourseMap.getMapAsync(this)
  }

  private fun initCourseDifficultyChipGroup() {
    binding.chipAll.also {
      it.setOnCheckedChangeListener { compoundButton, b ->
        if (b) {
          unClickOtherChips(binding.cgCourseDifficulty)
        }
      }
      it.isChecked = true
    }

    binding.chipEasy.setOnCheckedChangeListener { compoundButton, b ->
      if (b) {
        binding.chipAll.isChecked = false
      }
    }
    binding.chipMedium.setOnCheckedChangeListener { compoundButton, b ->
      if (b) {
        binding.chipAll.isChecked = false
      }
    }
    binding.chipHard.setOnCheckedChangeListener { compoundButton, b ->
      if (b) {
        binding.chipAll.isChecked = false
      }
    }

    val chipGroup = binding.cgCourseDifficulty
    chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
      if (checkedIds.isNotEmpty()) {
        var selectedChips: List<String> = checkedIds.map { id -> filteringMap[id] ?: id.toString() }
        if (selectedChips.contains("ALL")) {
          selectedChips = listOf("ALL")
        }
        filterCourses(selectedChips)
      } else {
        filterCourses(listOf())
      }
    }
  }

  private fun filterCourses(difficultyList: List<String>) {
    val filteredList: MutableList<CourseDetail> = mutableListOf()
    if (difficultyList.isEmpty() || difficultyList.contains("ALL")) {
      courseListAdapter.submitList(courseList)
      return
    }

    difficultyList.forEach { difficulty ->
      val easyCourses = courseList.filter { it.courseLevel == difficulty }
      filteredList.addAll(easyCourses)
    }
    courseListAdapter.submitList(filteredList)
  }

  private fun unClickOtherChips(chipGroup: ChipGroup) {
    for (chipId in chipGroup.checkedChipIds) {
      val chip = chipGroup.findViewById<Chip>(chipId)
      if (chip.text != "전체") {
        Log.d(TAG, "unClickOtherChips: ${chip.text}")
        chip.isChecked = false
      }
    }
  }

  override fun onMapReady(naverMap: NaverMap) {
    this.naverMap = naverMap
  }

}