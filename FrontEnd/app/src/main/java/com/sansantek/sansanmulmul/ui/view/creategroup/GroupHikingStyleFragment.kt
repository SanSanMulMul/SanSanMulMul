package com.sansantek.sansanmulmul.ui.view.creategroup

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupHikingStyleBinding
import com.sansantek.sansanmulmul.ui.util.Util.dpToPx
import com.sansantek.sansanmulmul.ui.viewmodel.CreateGroupViewModel

private const val TAG = "GroupHikingStyleFragmen 싸피"
class GroupHikingStyleFragment : BaseFragment<FragmentGroupHikingStyleBinding>(
  FragmentGroupHikingStyleBinding::bind,
  R.layout.fragment_group_hiking_style
) {
  private val viewModel: CreateGroupViewModel by activityViewModels()
  private lateinit var checkBoxEnableColorList : List<Int>
  private lateinit var checkBoxList : List<CheckBox>
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    checkBoxList = listOf(
      binding.cbHikingStyle1,
      binding.cbHikingStyle2,
      binding.cbHikingStyle3,
      binding.cbHikingStyle4,
      binding.cbHikingStyle5,
      binding.cbHikingStyle6,
    )
    checkBoxEnableColorList = listOf(
      R.color.purple,
      R.color.lightgreen,
      R.color.blue,
      R.color.pink,
      R.color.yellow,
      R.color.light_orange
    )
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
    registerObserver()
  }

  private fun onCheckedChangeListener(color: Int): CompoundButton.OnCheckedChangeListener {
    return object : CompoundButton.OnCheckedChangeListener {
      override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
        val checkBox = compoundButton as CheckBox
        if (isChecked) {
          addStyleById(checkBox.id)
//          viewModel.setGroupHikingStyleList()
//          setSolidColorBackground(checkBox, color)
        } else {
          // Reset to the original drawable background
          removeStyleById(checkBox.id)
//          checkBox.background =
//            ContextCompat.getDrawable(requireContext(), R.drawable.check_box_background)
        }
      }
    }
  }

  private fun registerObserver(){
    viewModel.crewStyle.observe(viewLifecycleOwner){
      Log.d(TAG, "registerObserver: ${viewModel.crewStyle.value}")
      for(box in checkBoxList){
        setCheckBoxDisable(box)
      }
      it.forEach { style->
        setCheckBoxEnable(style)
      }
    }
  }

  private fun setCheckBoxDisable(checkBox: CheckBox){
    checkBox.background =
      ContextCompat.getDrawable(requireContext(), R.drawable.check_box_background)
  }

  private fun setCheckBoxEnable(style: Int){
    setSolidColorBackground(checkBoxList[style-1], checkBoxEnableColorList[style-1])
  }

  private fun addStyleById(selectedId: Int){
    when (selectedId) {
      R.id.cb_hiking_style_1 -> {
        addStyle(1)
      }

      R.id.cb_hiking_style_2 -> {
        addStyle(2)
      }

      R.id.cb_hiking_style_3 -> {
        addStyle(3)
      }

      R.id.cb_hiking_style_4 -> {
        addStyle(4)
      }

      R.id.cb_hiking_style_5 -> {
          addStyle(5)
      }

      R.id.cb_hiking_style_6 -> {
        addStyle(6)
      }
    }
  }

  private fun addStyle(id: Int){
    viewModel.crewStyle.value?.let{
      val list = it.toMutableList()
      list.add(id)
      viewModel.setCrewStyle(list)
    }
  }

  private fun removeStyleById(selectedId: Int){
    when (selectedId) {
      R.id.cb_hiking_style_1 -> {
        removeStyle(1)
      }

      R.id.cb_hiking_style_2 -> {
        removeStyle(2)
      }

      R.id.cb_hiking_style_3 -> {
        removeStyle(3)
      }

      R.id.cb_hiking_style_4 -> {
        removeStyle(4)
      }

      R.id.cb_hiking_style_5 -> {
        removeStyle(5)
      }

      R.id.cb_hiking_style_6 -> {
        removeStyle(6)
      }
    }
  }

  private fun removeStyle(id: Int){
    viewModel.crewStyle.value?.let{
      val list = it.toMutableList().filter { target -> target != id }
      viewModel.setCrewStyle(list)
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