package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailTabBinding
import com.sansantek.sansanmulmul.ui.view.MainActivity
import kotlinx.coroutines.launch

private const val TAG = "마운틴디테일 번들"

class MountainDetailFragment : BaseFragment<FragmentMountainDetailTabBinding>(
  FragmentMountainDetailTabBinding::bind,
  R.layout.fragment_mountain_detail_tab
) {
  private var mountainId: Int? = null
  private var mountainName: String? = null
  private var mountainHeight: Int? = null
  private var mountainDescription: String? = null
  private var mountainImage: String? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    init()
    val bundle = arguments
    Log.d(TAG, "onViewCreated: 마운틴서치레절트에서 받은 번들$bundle")
    if(bundle != null){
      mountainId = bundle.getInt("mountainId")
      mountainName = bundle.getString("mountainName")
      mountainHeight = bundle.getInt("mountainHeight")
      mountainDescription = bundle.getString("mountainDescription")
      mountainImage = bundle.getString("mountainImage")
//      Log.d(TAG, "onViewCreated: ${mountainName}")
    }

    binding.tvMountainName.text = mountainName.toString()

    if(mountainImage == null){
      // 없을 경우 기본 이미지. 글라이드 : 링크이미지 받아올 때
      Glide.with(binding.root)
        .load("https://images-ext-1.discordapp.net/external/9pyEBG4x_J2aG-j5BeoaA8edEpEpfQEOEO9SdmT9hIg/https/k.kakaocdn.net/dn/cwObI9/btsGqPcg5ic/UHYbwvy2M2154EdZSpK8B1/img_110x110.jpg%2C?format=webp")
        .into(binding.ivMountain)
    }
    else{
      Glide.with(binding.root)
        .load(mountainImage)
        .into(binding.ivMountain)
    }
    val mountainDetailTabFirstInfoFragment = MountainDetailTabFirstInfoFragment()
    val detailBundle = Bundle()

    mountainId?.let {
      detailBundle.putInt("mountainId", it)
    }
    mountainHeight?.let {
      detailBundle.putInt("mountainHeight", it)
    }
    mountainDescription?.let {
      detailBundle.putString("mountainDescription", it)
    }

    mountainDetailTabFirstInfoFragment.arguments = detailBundle

    Log.d(TAG, "init: 마운틴 디테일 탭퍼스트로 보내줄 번들${detailBundle}")

    requireActivity().supportFragmentManager.beginTransaction()
      .replace(binding.mountainDetailFragmentView.id, mountainDetailTabFirstInfoFragment).commit()
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

    initTabLayout()
  }

  private fun initTabLayout() {
    binding.layoutTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab != null) {
          when (tab.position) {
            0 -> {
              val mountainDetailTabFirstInfoFragment = MountainDetailTabFirstInfoFragment()
              val bundle = Bundle()

              mountainId?.let {
                bundle.putInt("mountainId", it)
              }
              mountainHeight?.let {
                bundle.putInt("mountainHeight", it)
              }
              mountainDescription?.let {
                bundle.putString("mountainDescription", it)
              }

              mountainDetailTabFirstInfoFragment.arguments = bundle

              requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.mountain_detail_fragment_view, mountainDetailTabFirstInfoFragment)
                .commit()
            }

            1 -> {
              val mountainDetailTabSecondInfoFragment = MountainDetailTabSecondCourseFragment()
              val bundle = Bundle()

              mountainId?.let {
                bundle.putInt("mountainId", it)
              }

              mountainDetailTabSecondInfoFragment.arguments = bundle

              requireActivity().supportFragmentManager.beginTransaction()
              .replace(R.id.mountain_detail_fragment_view, mountainDetailTabSecondInfoFragment)
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