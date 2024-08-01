package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailTabFirstInfoBinding

private const val TAG = "MountainDetailTabFirstI 싸피"
class MountainDetailTabFirstInfoFragment : BaseFragment<FragmentMountainDetailTabFirstInfoBinding>(
  FragmentMountainDetailTabFirstInfoBinding::bind,
  R.layout.fragment_mountain_detail_tab_first_info
) {
  private var mountainId: Int? = null
  private var mountainHeight: Int? = null
  private var mountainDescription: String? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val bundle = arguments
    Log.d(TAG, "onViewCreated: $bundle")
    if(bundle != null){
      mountainId = bundle.getInt("mountainId")
      mountainHeight = bundle.getInt("mountainHeight")
      mountainDescription = bundle.getString("mountainDescription")
    }
    binding.tvHeight.text = "${mountainHeight}m"
    binding.tvMountainDetail.text = mountainDescription
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