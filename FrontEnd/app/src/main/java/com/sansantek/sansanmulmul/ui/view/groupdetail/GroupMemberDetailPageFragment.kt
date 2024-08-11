package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.GroupUser
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabFirstInfoFragmentBinding
import com.sansantek.sansanmulmul.databinding.FragmentGroupMemberDetailPageBinding


class GroupMemberDetailPageFragment : BaseFragment<FragmentGroupMemberDetailPageBinding>(
    FragmentGroupMemberDetailPageBinding::bind,
    R.layout.fragment_group_member_detail_page) {

    companion object {
        // `newInstance` 메서드 정의
        fun newInstance(user: GroupUser): GroupMemberDetailPageFragment {
            val fragment = GroupMemberDetailPageFragment()
            val args = Bundle().apply {
                putParcelable("user", user)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_member_detail_page, container, false)
    }
}