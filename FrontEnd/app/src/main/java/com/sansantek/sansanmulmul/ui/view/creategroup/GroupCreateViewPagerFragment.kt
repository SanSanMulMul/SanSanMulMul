package com.sansantek.sansanmulmul.ui.view.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupCreateViewPagerBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupCreateViewPagerAdapter

private const val TAG = "GroupCreateViewPagerFragment"

class GroupCreateViewPagerFragment : BaseFragment<FragmentGroupCreateViewPagerBinding>(
    FragmentGroupCreateViewPagerBinding::bind,
    R.layout.fragment_group_create_view_pager
) {

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

        val adapter = GroupCreateViewPagerAdapter(requireActivity())
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
}
