package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailBinding

class MountainDetailFragment : BaseFragment<FragmentMountainDetailBinding>(
  FragmentMountainDetailBinding::bind,
  R.layout.fragment_mountain_detail
) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    requireActivity().supportFragmentManager.beginTransaction()
      .replace(binding.mountainDetailFragmentView.id, MountainDetailTabFirstInfoFragment()).commit()

    binding.layoutTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab?) {
        requireActivity().supportFragmentManager.beginTransaction()
          .replace(R.id.mountain_detail_fragment_view, MountainDetailTabFirstInfoFragment())
          .commit()
      }

      override fun onTabUnselected(tab: TabLayout.Tab?) {
      }

      override fun onTabReselected(tab: TabLayout.Tab?) {
      }
    })
  }
}