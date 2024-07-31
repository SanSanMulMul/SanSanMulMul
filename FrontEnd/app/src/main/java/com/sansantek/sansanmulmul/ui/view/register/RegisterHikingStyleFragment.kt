package com.sansantek.sansanmulmul.ui.view.register


import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentRegisterHikingStyleBinding
import com.sansantek.sansanmulmul.ui.util.Util.dpToPx

class RegisterHikingStyleFragment : BaseFragment<FragmentRegisterHikingStyleBinding>(
    FragmentRegisterHikingStyleBinding::bind,
    R.layout.fragment_register_hiking_style
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set listeners for each CheckBox
        binding.cbHikingStyle1.setOnCheckedChangeListener(onCheckedChangeListener(R.color.purple))
        binding.cbHikingStyle2.setOnCheckedChangeListener(onCheckedChangeListener(R.color.lightgreen))
        binding.cbHikingStyle3.setOnCheckedChangeListener(onCheckedChangeListener(R.color.blue))
        binding.cbHikingStyle4.setOnCheckedChangeListener(onCheckedChangeListener(R.color.pink))
        binding.cbHikingStyle5.setOnCheckedChangeListener(onCheckedChangeListener(R.color.yellow))
    }

    private fun onCheckedChangeListener(color: Int): CompoundButton.OnCheckedChangeListener {
        return object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(compoundButton: CompoundButton?, isChecked: Boolean) {
                val checkBox = compoundButton as CheckBox
                if (isChecked) {
                    setGradient(checkBox, color)
                } else {
                    // Reset to the original drawable background
                    checkBox.background = ContextCompat.getDrawable(requireContext(), R.drawable.check_box_background)
                }
            }
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