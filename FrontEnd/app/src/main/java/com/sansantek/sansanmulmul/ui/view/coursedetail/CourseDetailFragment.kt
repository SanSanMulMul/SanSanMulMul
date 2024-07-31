package com.sansantek.sansanmulmul.ui.view.coursedetail

import android.os.Bundle
import android.view.View
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentCourseDetailBinding


class CourseDetailFragment : BaseFragment<FragmentCourseDetailBinding>(
  FragmentCourseDetailBinding::bind,
  R.layout.fragment_course_detail
) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    init()
  }

  private fun init() {
    activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), true) }
  }
}