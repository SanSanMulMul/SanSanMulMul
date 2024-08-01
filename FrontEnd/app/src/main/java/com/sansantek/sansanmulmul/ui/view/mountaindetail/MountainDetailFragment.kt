package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailTabBinding
import com.sansantek.sansanmulmul.ui.view.MainActivity

class MountainDetailFragment : BaseFragment<FragmentMountainDetailTabBinding>(
  FragmentMountainDetailTabBinding::bind,
  R.layout.fragment_mountain_detail_tab
) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    init()
  }

  // 산 상세화면에서 나올 때 홈내비 다시 생기게
  override fun onPause() {
    val activity = requireActivity() as MainActivity
    activity.changeBottomNavigationVisibility(true)
    super.onPause()
  }

  // 산 상세화면 들어갈 때 홈내비 없앰
  private fun init() {
    val activity = requireActivity() as MainActivity
    activity.changeBottomNavigationVisibility(false)


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