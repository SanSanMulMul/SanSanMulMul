package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.CourseDetail
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailTabSecondCourseBinding
import com.sansantek.sansanmulmul.ui.adapter.MountainDetailCourseListAdapter
import com.sansantek.sansanmulmul.ui.view.coursedetail.CourseDetailFragment
import com.sansantek.sansanmulmul.ui.viewmodel.CourseDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainDetailViewModel

private const val TAG = "싸피_MountainDetailTabSecond"

class MountainDetailTabSecondCourseFragment :
  BaseFragment<FragmentMountainDetailTabSecondCourseBinding>(
    FragmentMountainDetailTabSecondCourseBinding::bind,
    R.layout.fragment_mountain_detail_tab_second_course
  ) {
  private lateinit var courseList: MutableList<CourseDetail>
  private val mountainDetailViewModel: MountainDetailViewModel by activityViewModels()
  private val courseDetailViewModel: CourseDetailViewModel by activityViewModels()
  private lateinit var courseListAdapter: MountainDetailCourseListAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    init()
  }

  private fun init() {
//    initCourseDifficultyChipGroup()

    var mountainId = mountainDetailViewModel.mountainID.value
    courseList = mutableListOf()

    mountainDetailViewModel.mountainID.observe(viewLifecycleOwner) {
      mountainId = it
    }

    mountainDetailViewModel.mountainCourse.observe(viewLifecycleOwner) { mountainCourse ->
      if (mountainCourse != null) {
        mountainCourse.courseIds.forEach { courseId ->
          mountainId?.let { courseDetailViewModel.fetchCourseDetail(it, courseId) }
        }
      } else {
        Log.d(TAG, "initCourseData: mountain course 비었음")
      }
    }

    courseDetailViewModel.courseDetail.observe(viewLifecycleOwner) { courseDetail ->
      if (courseDetail != null) {
        courseList.add(courseDetail)
        courseListAdapter.submitList(courseList.toList())
        Log.d(TAG, "initCourseData: $courseDetail")
      } else {
        Log.d(TAG, "initCourseData: course detail 없음")
      }
    }

    initCourseRecyclerView()
  }


  private fun initCourseDifficultyChipGroup() {
    binding.cgCourseDifficulty.setOnCheckedStateChangeListener { group, checkedIds ->
      if (checkedIds.isNotEmpty()) {
        val selectedChips =
          checkedIds.map { id -> binding.root.findViewById<Chip>(id) }
        val allChipSelected = selectedChips.any { it.text == "전체" }
        if (allChipSelected) {
          checkAllChipsExcept(group, true)
        } else {
          checkAllChipsExcept(group, false)
        }

      } else {
//        Toast.makeText(context, "No chip is selected", Toast.LENGTH_SHORT).show()
      }
    }
  }

  private fun checkAllChipsExcept(chipGroup: ChipGroup, checkedState: Boolean) {
    for (i in 0 until chipGroup.childCount) {
      val chip = chipGroup.getChildAt(i) as Chip
      if (chip.text != "전체") {
        chip.isChecked = checkedState
      }
    }
  }

  private fun initCourseRecyclerView() {
    val courseRecyclerView = binding.rvMountainDetailCourses

    courseListAdapter = MountainDetailCourseListAdapter(
      object : MountainDetailCourseListAdapter.OnItemClickListener {
        override fun onItemClick(course: CourseDetail) {
          requireActivity().supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_view, CourseDetailFragment()).commit()
        }
      })

    courseRecyclerView.layoutManager = LinearLayoutManager(context)
    courseRecyclerView.adapter = courseListAdapter

    val dividerDrawable = activity?.getDrawable(R.drawable.recyclerview_divider_lightgray)
    val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    if (dividerDrawable != null) dividerItemDecoration.setDrawable(dividerDrawable)
    courseRecyclerView.addItemDecoration(dividerItemDecoration)
  }

  override fun onPause() {
    super.onPause()
    courseList.clear()
  }

  override fun onResume() {
    super.onResume()
    courseList.clear()
  }

}