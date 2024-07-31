package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Course
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailTabSecondCourseBinding
import com.sansantek.sansanmulmul.ui.adapter.MountainDetailCourseListAdapter
import com.sansantek.sansanmulmul.ui.view.coursedetail.CourseDetailFragment

class MountainDetailTabSecondCourseFragment :
  BaseFragment<FragmentMountainDetailTabSecondCourseBinding>(
    FragmentMountainDetailTabSecondCourseBinding::bind,
    R.layout.fragment_mountain_detail_tab_second_course
  ) {
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    init()
  }

  private fun init() {
    val courseList = initCourseData()
    val courseRecyclerView = binding.rvMountainDetailCourses
    val courseListAdapter = MountainDetailCourseListAdapter(
      courseList,
      object : MountainDetailCourseListAdapter.OnItemClickListener {
        override fun onItemClick(course: Course) {
          requireActivity().supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_view, CourseDetailFragment()).commit()
        }
      })

    courseRecyclerView.layoutManager = LinearLayoutManager(context)
    courseRecyclerView.adapter = courseListAdapter

    courseRecyclerView.setOnTouchListener { v, event ->
      v.parent.requestDisallowInterceptTouchEvent(true)
      false
    }

    val dividerDrawable = activity?.getDrawable(R.drawable.recyclerview_divider_lightgray)
    val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    if (dividerDrawable != null) dividerItemDecoration.setDrawable(dividerDrawable)
    courseRecyclerView.addItemDecoration(dividerItemDecoration)
  }

  private fun initCourseData(): List<Course> {
    return listOf(
      Course("가야산코스1", "어려움", "출발지1", "목적지1", 4.0),
      Course("가야산코스2", "보통", "출발지2", "목적지2", 2.5),
      Course("가야산코스3", "쉬움", "출발지3", "목적지3", 3.0),
      Course("가야산코스4", "어려움", "출발지4", "목적지4", 6.0),
    )
  }

}