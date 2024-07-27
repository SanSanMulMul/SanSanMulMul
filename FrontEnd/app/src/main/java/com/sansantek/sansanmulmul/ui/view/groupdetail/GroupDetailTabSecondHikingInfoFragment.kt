package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.os.Bundle
import android.view.View
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabSecondHikingInfoFragmentBinding

class GroupDetailTabSecondHikingInfoFragment() :
  BaseFragment<FragmentGroupDetailTabSecondHikingInfoFragmentBinding>(
    FragmentGroupDetailTabSecondHikingInfoFragmentBinding::bind,
    R.layout.fragment_group_detail_tab_first_info_fragment
  ) {
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    init()
  }

  private fun init() {
    activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), false) }
  }
}