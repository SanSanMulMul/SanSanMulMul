package com.sansantek.sansanmulmul.ui.view.creategroup

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.addTextChangedListener
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.databinding.FragmentGroupExtraInfoBinding


class GroupExtraInfoFragment : Fragment() {

    private lateinit var binding: FragmentGroupExtraInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupExtraInfoBinding.inflate(layoutInflater)

        val ageRangeSlider = binding.ageRangeSlider
        val minageblank = binding.minAgeBlank
        val maxageblank = binding.maxAgeBlank

        // 입력 못하게 막기
        binding.minAgeBlank.isEnabled = false
        binding.maxAgeBlank.isEnabled = false

        // 10단위로 끊기
        ageRangeSlider.stepSize = 10.0f
        ageRangeSlider.addOnChangeListener { slider, value, fromUser ->
            val values = ageRangeSlider.values
            val minAge = values[0].toInt()
            val maxAge = values[1].toInt()

            minageblank.setText(minAge.toString())
            maxageblank.setText(maxAge.toString())
        }

        return binding.root
    }
}