package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupMemberDetailPageBinding
import com.sansantek.sansanmulmul.databinding.FragmentGroupMemberFollowListBinding
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel


class GroupMemberFollowListFragment : BaseFragment<FragmentGroupMemberFollowListBinding>(
    FragmentGroupMemberFollowListBinding::bind,
    R.layout.fragment_group_member_follow_list
) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    companion object {
        fun newInstance(memberUserId: Int): GroupMemberDetailPageFragment {
            val fragment = GroupMemberDetailPageFragment(memberUserId)
            val args = Bundle().apply {
                putInt("followUserId", memberUserId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}