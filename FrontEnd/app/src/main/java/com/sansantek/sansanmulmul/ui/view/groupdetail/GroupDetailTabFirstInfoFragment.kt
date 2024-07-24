package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabFirstInfoFragmentBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailTabHikingStyleListAdapter
import com.sansantek.sansanmulmul.ui.adapter.layoutmanager.CustomLayoutmanager

private const val TAG = "GroupDetailTabFirstInfo 싸피"
class GroupDetailTabFirstInfoFragment : BaseFragment<FragmentGroupDetailTabFirstInfoFragmentBinding>(
    FragmentGroupDetailTabFirstInfoFragmentBinding::bind,
    R.layout.fragment_group_detail_tab_first_info_fragment
) {
    private val styleList = mutableListOf("#등산도 식후경", "#등산에 집중","#어쩌구", "#저쩌구")
    private lateinit var hikingStyleListAdapter:GroupDetailTabHikingStyleListAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hikingStyleListAdapter = GroupDetailTabHikingStyleListAdapter()
        binding.rvGroupHikingStyle.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//            layoutManager = CustomLayoutmanager(requireContext(), 2)
            adapter = hikingStyleListAdapter.apply {
//                submitList(styleList){binding.rvGroupHikingStyle.post{
//                binding.rvGroupHikingStyle.requestLayout()
//                binding.root.invalidate()
//                Log.d(TAG, "onViewCreated: submit 이후 바로 실행 높이는 ${binding.rvGroupHikingStyle.height}")
//            }}
            }
        }
        hikingStyleListAdapter.submitList(styleList)
        Log.d(TAG, "onViewCreated: ${binding.rvGroupHikingStyle.height}")
    }
}