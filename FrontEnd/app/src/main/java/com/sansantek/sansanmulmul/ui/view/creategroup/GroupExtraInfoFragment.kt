package com.sansantek.sansanmulmul.ui.view.creategroup

import android.os.Bundle
import android.view.View
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupExtraInfoBinding


class GroupExtraInfoFragment : BaseFragment<FragmentGroupExtraInfoBinding>(
  FragmentGroupExtraInfoBinding::bind,
  R.layout.fragment_group_extra_info
) {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    init()

    val ageRangeSlider = binding.ageRangeSlider
    val minageblank = binding.minAgeBlank
    val maxageblank = binding.maxAgeBlank

    // 입력 못하게 막기
    binding.minAgeBlank.isEnabled = false
    binding.maxAgeBlank.isEnabled = false

    // 10단위로 끊기
    ageRangeSlider.stepSize = 10.0f
    ageRangeSlider.addOnChangeListener { slider, value, fromUser ->
      val values = ageRangeSlider.values
      val minAge = values[0].toInt()
      val maxAge = values[1].toInt()

      minageblank.setText(minAge.toString())
      maxageblank.setText(maxAge.toString())
    }
  }

  private fun init() {
    activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), true) }
  }
}