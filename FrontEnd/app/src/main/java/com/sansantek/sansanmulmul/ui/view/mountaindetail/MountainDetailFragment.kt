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
    init()
  }

  private fun init() {
    activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), true) }

    // TODO : 코스 상세에서 뒤로가기 했을 때 무조건 첫번째 Tab으로 돌아오는 거 수정하기
    requireActivity().supportFragmentManager.beginTransaction()
      .replace(binding.mountainDetailFragmentView.id, MountainDetailTabFirstInfoFragment()).commit()

    initTabLayout()
  }

  private fun initTabLayout() {
    binding.layoutTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab != null) {
          when (tab.position) {
            0 -> requireActivity().supportFragmentManager.beginTransaction()
              .replace(R.id.mountain_detail_fragment_view, MountainDetailTabFirstInfoFragment())
              .commit()

            1 -> requireActivity().supportFragmentManager.beginTransaction()
              .replace(R.id.mountain_detail_fragment_view, MountainDetailTabSecondCourseFragment())
              .commit()
          }
        }

      }

      override fun onTabUnselected(tab: TabLayout.Tab?) {}
      override fun onTabReselected(tab: TabLayout.Tab?) {}
    })
  }
}