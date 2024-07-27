package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.os.Bundle
import android.view.View
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailTabFirstInfoBinding


class MountainDetailTabFirstInfoFragment : BaseFragment<FragmentMountainDetailTabFirstInfoBinding>(
  FragmentMountainDetailTabFirstInfoBinding::bind,
  R.layout.fragment_mountain_detail_tab_first_info
) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), true) }
  }
}