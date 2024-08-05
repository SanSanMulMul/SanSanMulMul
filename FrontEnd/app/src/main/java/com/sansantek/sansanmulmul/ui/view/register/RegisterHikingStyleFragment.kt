package com.sansantek.sansanmulmul.ui.view.register


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
import com.sansantek.sansanmulmul.databinding.FragmentRegisterHikingStyleBinding
import com.sansantek.sansanmulmul.ui.util.Util.dpToPx
import com.sansantek.sansanmulmul.ui.viewmodel.LoginActivityViewModel

private const val TAG = "RegisterHikingStyleFrag 싸피"
class RegisterHikingStyleFragment : BaseFragment<FragmentRegisterHikingStyleBinding>(
    FragmentRegisterHikingStyleBinding::bind,
    R.layout.fragment_register_hiking_style
) {
    private val activityViewModel: LoginActivityViewModel by activityViewModels()
    private lateinit var checkBoxList : List<CheckBox>
    private lateinit var checkBoxEnableColorList : List<Int>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkBoxList = listOf(
            binding.cbHikingStyle1,
            binding.cbHikingStyle2,
            binding.cbHikingStyle3,
            binding.cbHikingStyle4,
            binding.cbHikingStyle5,
        )
        checkBoxEnableColorList = listOf(
            R.color.purple,
            R.color.lightgreen,
            R.color.blue,
            R.color.pink,
            R.color.yellow
        )
        // Set listeners for each CheckBox
        binding.cbHikingStyle1.setOnCheckedChangeListener(onCheckedChangeListener(R.color.purple))
        binding.cbHikingStyle2.setOnCheckedChangeListener(onCheckedChangeListener(R.color.lightgreen))
        binding.cbHikingStyle3.setOnCheckedChangeListener(onCheckedChangeListener(R.color.blue))
        binding.cbHikingStyle4.setOnCheckedChangeListener(onCheckedChangeListener(R.color.pink))
        binding.cbHikingStyle5.setOnCheckedChangeListener(onCheckedChangeListener(R.color.yellow))

        registerObserver()
    }

    private fun onCheckedChangeListener(color: Int): CompoundButton.OnCheckedChangeListener {
        return CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            val checkBox = compoundButton as CheckBox
            if (isChecked) {
                addStyleById(checkBox.id)
            } else {
                // Reset to the original drawable background
                removeStyleById(checkBox.id)
            }
        }
    }

    private fun registerObserver(){
        activityViewModel.userStyles.observe(viewLifecycleOwner){
            Log.d(TAG, "registerObserver: ${activityViewModel.userStyles.value}")
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
        setGradient(checkBoxList[style-1], checkBoxEnableColorList[style-1])
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
                activityViewModel.userStyles.value?.let{
                    addStyle(5)
                }
            }
        }
    }

    private fun addStyle(id: Int){
        activityViewModel.userStyles.value?.let{
            val list = it.toMutableList()
            list.add(id)
            activityViewModel.setUserStyles(list)
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
        }
    }

    private fun removeStyle(id: Int){
        activityViewModel.userStyles.value?.let{
            val list = it.toMutableList().filter { target -> target != id }
            activityViewModel.setUserStyles(list)
        }
    }

    private fun setGradient(checkBox: CheckBox, color: Int) {
        // Create a gradient drawable with stroke and corner radius
        val gradientDrawable = GradientDrawable().apply {
            orientation = GradientDrawable.Orientation.TOP_BOTTOM
            colors = intArrayOf(
                Color.WHITE, // Starting color
                ContextCompat.getColor(requireContext(), color) // Dynamic color from resources
            )
            val dp = 10f.dpToPx(this@RegisterHikingStyleFragment.requireContext())
            cornerRadius = dp // Set the corner radius
            setStroke(1, Color.parseColor("#808080")) // Set stroke width and color
        }
        checkBox.background = gradientDrawable // Set the gradient as background
    }
}