package com.sansantek.sansanmulmul.ui.view.creategroup

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupHikingStyleBinding
import com.sansantek.sansanmulmul.ui.util.Util.dpToPx

class GroupHikingStyleFragment : BaseFragment<FragmentGroupHikingStyleBinding>(
  FragmentGroupHikingStyleBinding::bind,
  R.layout.fragment_group_hiking_style
) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    init()

    binding.cbHikingStyle1.setOnCheckedChangeListener(onCheckedChangeListener(R.color.purple))
    binding.cbHikingStyle2.setOnCheckedChangeListener(onCheckedChangeListener(R.color.lightgreen))
    binding.cbHikingStyle3.setOnCheckedChangeListener(onCheckedChangeListener(R.color.blue))
    binding.cbHikingStyle4.setOnCheckedChangeListener(onCheckedChangeListener(R.color.pink))
    binding.cbHikingStyle5.setOnCheckedChangeListener(onCheckedChangeListener(R.color.yellow))
    binding.cbHikingStyle6.setOnCheckedChangeListener(onCheckedChangeListener(R.color.light_orange))
  }

  private fun init() {
    activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), true) }
  }

  private fun onCheckedChangeListener(color: Int): CompoundButton.OnCheckedChangeListener {
    return object : CompoundButton.OnCheckedChangeListener {
      override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
        val checkBox = compoundButton as CheckBox
        if (isChecked) {
          setSolidColorBackground(checkBox, color)
        } else {
          // Reset to the original drawable background
          checkBox.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.check_box_background)
        }
      }
    }
  }

  private fun setSolidColorBackground(checkBox: CheckBox, color: Int) {
    val drawable = GradientDrawable().apply {
      setColor(ContextCompat.getColor(requireContext(), color)) // 단색 배경 색상 설정
      val dp = 10f.dpToPx(this@GroupHikingStyleFragment.requireContext())
      cornerRadius = dp // 모서리 반경 설정
      setStroke(1, Color.parseColor("#808080")) // 테두리 너비와 색상 설정
    }
    checkBox.background = drawable // 단색 배경을 설정
  }
}