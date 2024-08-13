package com.sansantek.sansanmulmul.ui.view.creategroup

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupExtraInfoBinding
import com.sansantek.sansanmulmul.ui.view.register.GroupCreateViewPagerFragment
import com.sansantek.sansanmulmul.ui.viewmodel.CreateGroupViewModel


class GroupExtraInfoFragment : BaseFragment<FragmentGroupExtraInfoBinding>(
    FragmentGroupExtraInfoBinding::bind,
    R.layout.fragment_group_extra_info
) {
    private val viewPagerFragment by lazy {
        parentFragment as GroupCreateViewPagerFragment
    }

    private val viewModel: CreateGroupViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ageRangeSlider = binding.ageRangeSlider
        val minageblank = binding.minAgeBlank
        val maxageblank = binding.maxAgeBlank

        // 입력 못하게 막기
        binding.minAgeBlank.isEnabled = false
        binding.maxAgeBlank.isEnabled = false

        binding.groupPeopleNumberBlank.doOnTextChanged { text, start, before, cnt ->
            checkValid()
        }

        // 10단위로 끊기
        ageRangeSlider.stepSize = 10.0f
        ageRangeSlider.addOnChangeListener { slider, value, fromUser ->
            val values = ageRangeSlider.values
            val minAge = values[0].toInt()
            val maxAge = values[1].toInt()
            viewModel.setGroupMinAge(minAge)
            viewModel.setGroupMaxAge(maxAge)
            checkValid()
            minageblank.setText(minAge.toString())
            maxageblank.setText(maxAge.toString())
        }

        binding.GroupGenderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.anybodyButton.id -> {
                    viewModel.setGroupGender("A")
                }

                binding.maleButton.id -> {
                    viewModel.setGroupGender("M")
                }

                binding.femaleButton.id -> {
                    viewModel.setGroupGender("F")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun checkValid() {
        if (binding.groupPeopleNumberBlank.text.isNullOrBlank() || (binding.groupPeopleNumberBlank.text.toString()
                .toIntOrNull() == null) || viewModel.groupMinAge == -1 || viewModel.groupMaxAge == -1
        ) {
            viewPagerFragment.enableNextButton(false)
        } else {
            viewModel.setMaxMember(binding.groupPeopleNumberBlank.text.toString().toInt())
            viewPagerFragment.enableNextButton(true)

        }
    }

    private fun init() {
        activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), true) }
        binding.ageRangeSlider.setValues(0F, 100F)
        checkValid()
    }
}