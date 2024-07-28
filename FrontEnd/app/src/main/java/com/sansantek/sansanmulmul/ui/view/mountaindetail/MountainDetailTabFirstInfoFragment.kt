package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailTabFirstInfoBinding


class MountainDetailTabFirstInfoFragment : BaseFragment<FragmentMountainDetailTabFirstInfoBinding>(
  FragmentMountainDetailTabFirstInfoBinding::bind,
  R.layout.fragment_mountain_detail_tab_first_info
) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    init()
  }

  @SuppressLint("ClickableViewAccessibility")
  private fun init() {

    // 산 설명 - 스크롤
    binding.tvMountainDetail.movementMethod = ScrollingMovementMethod()
    binding.tvMountainDetail.scrollTo(0, 0)
    binding.tvMountainDetail.setOnTouchListener { v, event ->
      v.parent.requestDisallowInterceptTouchEvent(true)
      false
    }
  }

}