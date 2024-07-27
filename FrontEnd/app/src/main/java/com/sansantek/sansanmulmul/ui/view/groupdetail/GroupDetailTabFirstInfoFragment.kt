package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Member
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabFirstInfoFragmentBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailFirstTabMemberListAdapter
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailTabHikingStyleListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.SpaceItemDecoration

private const val TAG = "GroupDetailTabFirstInfo 싸피"

class GroupDetailTabFirstInfoFragment :
  BaseFragment<FragmentGroupDetailTabFirstInfoFragmentBinding>(
    FragmentGroupDetailTabFirstInfoFragmentBinding::bind,
    R.layout.fragment_group_detail_tab_first_info_fragment
  ) {
  private val styleList = mutableListOf("#등산도 식후경", "#등산에 집중", "#어쩌구", "#저쩌구")
  private val memberList = mutableListOf<Member>()
  private lateinit var hikingStyleListAdapter: GroupDetailTabHikingStyleListAdapter
  private lateinit var groupDetailFirstTabMemberListAdapter: GroupDetailFirstTabMemberListAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    init()

    hikingStyleListAdapter = GroupDetailTabHikingStyleListAdapter()
    binding.rvGroupHikingStyle.apply {
      layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      adapter = hikingStyleListAdapter.apply {
        submitList(styleList)
        addItemDecoration(SpaceItemDecoration(15))
      }
    }

    for (i in 1..3) {
      val member = Member("엄홍길👑", "박태asssssssss우스", false)
      memberList.add(member)
    }
    for (i in 1..3) {
      val member = Member("엄홍길👑", "박태우스", true)
      memberList.add(member)
    }

    groupDetailFirstTabMemberListAdapter = GroupDetailFirstTabMemberListAdapter()
    binding.rvGroupMemberList.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = groupDetailFirstTabMemberListAdapter.apply {
        submitList(memberList)
        addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
      }
    }
  }

  private fun init() {
    activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), false) }
  }
}