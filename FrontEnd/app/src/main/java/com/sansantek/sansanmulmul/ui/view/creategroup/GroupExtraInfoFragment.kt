package com.sansantek.sansanmulmul.ui.view.creategroup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        ageRangeSlider.addOnChangeListener { slider, value, fromUser ->
            val values = ageRangeSlider.values
            val minAge = values[0].toInt()
            val maxAge = values[1].toInt()

            // 나이 5단위로 끊기
            ageRangeSlider.stepSize = 1f
            minageblank.setText(minAge.toString())
            maxageblank.setText(maxAge.toString())
        }

        return binding.root
    }
}