package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailTabBinding
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.viewmodel.MountainDetailViewModel

// TODO : 코스 상세에서 뒤로가기 했을 때 무조건 첫번째 Tab으로 돌아오는 거 수정하기

private const val TAG = "마운틴디테일 번들"

class MountainDetailFragment : BaseFragment<FragmentMountainDetailTabBinding>(
  FragmentMountainDetailTabBinding::bind,
  R.layout.fragment_mountain_detail_tab
) {
  private val mountainDetailViewModel: MountainDetailViewModel by activityViewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    init()
    requireActivity().supportFragmentManager.beginTransaction()
      .replace(binding.mountainDetailFragmentView.id, MountainDetailTabFirstInfoFragment())
      .commit()
  }

  // 산 상세화면에서 나올 때 홈내비 다시 생기게
  override fun onPause() {
    val activity = requireActivity() as MainActivity
    activity.changeBottomNavigationVisibility(true)
    super.onPause()
  }

  override fun onResume() {
    super.onResume()
    if (mountainDetailViewModel.prevTab.value != null) {
      binding.layoutTab.getTabAt(mountainDetailViewModel.prevTab.value!!)?.select()
    }
  }

  // 산 상세화면 들어갈 때 홈내비 없앰
  private fun init() {
    (requireActivity() as MainActivity).changeBottomNavigationVisibility(false)
    initTabLayout()
    initMountainData()
  }

  private fun initMountainData() {
    val mountainID = mountainDetailViewModel.mountainID.value
    if (mountainID != null) {
      mountainDetailViewModel.fetchMountainDetail(mountainID)
    } else {
      Toast.makeText(context, "산 ID가 유효하지 않습니다!", Toast.LENGTH_SHORT).show()
    }

    mountainDetailViewModel.mountainDetail.observe(viewLifecycleOwner) { mountain ->
      setMountainInfo(mountain)
    }
  }

  private fun setMountainInfo(mountain: Mountain?) {
    if (mountain != null) {
      binding.tvMountainName.text = mountain.mountainName
      if (mountain.mountainImg == null) {
        Glide.with(binding.root)
          .load("https://images-ext-1.discordapp.net/external/9pyEBG4x_J2aG-j5BeoaA8edEpEpfQEOEO9SdmT9hIg/https/k.kakaocdn.net/dn/cwObI9/btsGqPcg5ic/UHYbwvy2M2154EdZSpK8B1/img_110x110.jpg%2C?format=webp")
          .into(binding.ivMountain)
      } else {
        Glide.with(binding.root)
          .load(mountain.mountainImg)
          .into(binding.ivMountain)
      }
    } else {
      Toast.makeText(context, "산 정보가 없습니다!", Toast.LENGTH_SHORT).show()
    }
  }

  private fun initTabLayout() {
    binding.layoutTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab != null) {
          when (tab.position) {
            0 -> {
              requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                  R.id.mountain_detail_fragment_view,
                  MountainDetailTabFirstInfoFragment()
                )
                .commit()
            }

            1 -> {
              requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                  R.id.mountain_detail_fragment_view,
                  MountainDetailTabSecondCourseFragment()
                )
                .commit()
            }
          }
        }

      }

      override fun onTabUnselected(tab: TabLayout.Tab?) {}
      override fun onTabReselected(tab: TabLayout.Tab?) {}
    })
  }
}