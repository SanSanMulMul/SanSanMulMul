package com.sansantek.sansanmulmul.ui.view.register

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupCreateViewPagerBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupCreateViewPagerAdapter
import com.sansantek.sansanmulmul.ui.viewmodel.CreateGroupViewModel

private const val TAG = "GroupCreateViewPagerFragment_싸피"

class GroupCreateViewPagerFragment : BaseFragment<FragmentGroupCreateViewPagerBinding>(
    FragmentGroupCreateViewPagerBinding::bind,
    R.layout.fragment_group_create_view_pager
) {
    private val viewModel: CreateGroupViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: View is being created")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: View created")

        val adapter = GroupCreateViewPagerAdapter(this)
        binding.vpCreateGroup.adapter = adapter
        binding.vpCreateGroup.isUserInputEnabled = false
        binding.createGroupSpringDotsIndicator.attachTo(binding.vpCreateGroup)

        Log.d(TAG, "onViewCreated: Adapter set")

        checkViewPageLimit()

        binding.createGroupNextButton.setOnClickListener {
            binding.vpCreateGroup.currentItem += 1
            checkViewPageLimit()
        }
        binding.createGroupPrevButton.setOnClickListener {
            binding.vpCreateGroup.currentItem -= 1
            checkViewPageLimit()
        }

        Log.d(TAG, "onViewCreated: Listeners set")
    }

    private fun checkViewPageLimit() {
        val itemCount = binding.vpCreateGroup.adapter?.itemCount ?: 0
        Log.d(TAG, "checkViewPageLimit: ${binding.vpCreateGroup.currentItem}, itemCount: $itemCount")
        if (binding.vpCreateGroup.currentItem == itemCount - 1) {
            binding.createGroupNextButton.visibility = View.INVISIBLE
            binding.createGroupPrevButton.visibility = View.INVISIBLE
            binding.createGroupProgressButton.visibility = View.INVISIBLE
        } else if (binding.vpCreateGroup.currentItem == 0) {
            binding.createGroupNextButton.visibility = View.VISIBLE
            binding.createGroupPrevButton.visibility = View.INVISIBLE
        } else {
            binding.createGroupNextButton.visibility = View.VISIBLE
            binding.createGroupPrevButton.visibility = View.VISIBLE
        }
    }

    fun enableNextButton(enable: Boolean) {
        if (enable) {
            binding.createGroupNextButton.imageTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.white)
            binding.createGroupNextButton.background.setTintList(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.green
                )
            )
        } else {
            binding.createGroupNextButton.imageTintList = ColorStateList.valueOf(Color.parseColor("#FF373737"))
            binding.createGroupNextButton.background.setTintList(
                null
            )
            binding.createGroupNextButton.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.gray_border_rounded_box)
        }
        binding.createGroupNextButton.isEnabled = enable
    }
}
