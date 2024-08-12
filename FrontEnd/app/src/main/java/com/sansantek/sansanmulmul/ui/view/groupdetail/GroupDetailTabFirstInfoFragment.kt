package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.data.model.GroupUser
import com.sansantek.sansanmulmul.data.model.RequestMember
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabFirstInfoFragmentBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailFirstTabMemberListAdapter
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailFirstTabRegistrationListAdapter
import com.sansantek.sansanmulmul.ui.adapter.GroupDetailTabHikingStyleListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.SpaceItemDecoration
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.crewService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch

private const val TAG = "GroupDetailTabFirstInfo 싸피"

class GroupDetailTabFirstInfoFragment(private val crew: Crew) :
    BaseFragment<FragmentGroupDetailTabFirstInfoFragmentBinding>(
        FragmentGroupDetailTabFirstInfoFragmentBinding::bind,
        R.layout.fragment_group_detail_tab_first_info_fragment
    ) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val styleList = mutableListOf<Int>()
    private val memberList = mutableListOf<GroupUser>()
    private lateinit var hikingStyleListAdapter: GroupDetailTabHikingStyleListAdapter
    private lateinit var groupDetailFirstTabMemberListAdapter: GroupDetailFirstTabMemberListAdapter
    private lateinit var groupDetailFirstTabMemberRegisterListAdapter: GroupDetailFirstTabRegistrationListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        hikingStyleListAdapter = GroupDetailTabHikingStyleListAdapter()
        binding.rvGroupHikingStyle.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false).apply {
                    isMeasurementCacheEnabled = false
                }
            adapter = hikingStyleListAdapter.apply {
                submitList(styleList)
                addItemDecoration(SpaceItemDecoration(15))
            }
        }
        Log.d(TAG, "onViewCreated: $crew")


        binding.rvGroupMemberList.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun init() {
        activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), false) }
        lifecycleScope.launch {
            val result = crewService.getGroupDetailFirstTabData(crew.crewId)
            if (result.isSuccessful) {
                result.body()?.let {
                    binding.tvGroupDescriptionContent.text = it.crewDescription
                    hikingStyleListAdapter.submitList(it.crewHikingStyles)
                    val amILeader =
                        it.members.find { it.userId == activityViewModel.user.userId }?.leader
                            ?: false

                    if (amILeader) {
                        binding.rvGroupRegistrationList.layoutManager =
                            LinearLayoutManager(requireContext())
                        activityViewModel.token?.let { token ->
                            val result = crewService.getRequestList(
                                makeHeaderByAccessToken(token.accessToken),
                                crew.crewId
                            )
                            if (result.isSuccessful) {
                                groupDetailFirstTabMemberRegisterListAdapter =
                                    GroupDetailFirstTabRegistrationListAdapter()
                                binding.rvGroupRegistrationList.adapter =
                                    groupDetailFirstTabMemberRegisterListAdapter.apply {
                                        setItemClickListener(object :
                                            GroupDetailFirstTabRegistrationListAdapter.ItemClickListener {
                                            override suspend fun onDeclineClick(user: RequestMember): Boolean {
                                                val result = crewService.refuseRegister(
                                                    makeHeaderByAccessToken(token.accessToken),
                                                    user.requestId
                                                )
                                                if (result.isSuccessful) {
                                                    val newList = crewService.getRequestList(
                                                        makeHeaderByAccessToken(token.accessToken),
                                                        crew.crewId
                                                    )
                                                    if (newList.isSuccessful) {
                                                        submitList(null)
                                                        submitList(newList.body()!!)
                                                    }
                                                }
                                                return result.isSuccessful
                                            }

                                            override suspend fun onApproveClick(user: RequestMember): Boolean {
                                                val result = crewService.acceptRegister(
                                                    makeHeaderByAccessToken(token.accessToken),
                                                    user.requestId
                                                )
                                                if (result.isSuccessful) {
                                                    val newList = crewService.getRequestList(
                                                        makeHeaderByAccessToken(token.accessToken),
                                                        crew.crewId
                                                    )
                                                    val memberNewList =
                                                        crewService.getGroupDetailFirstTabData(crew.crewId)
                                                    if (newList.isSuccessful) {
                                                        submitList(null)
                                                        submitList(newList.body()!!)
                                                        Log.d(
                                                            TAG,
                                                            "onApproveClick: 현재 요청 멤버 리스트는 ${newList.body()}"
                                                        )
                                                        if (memberNewList.isSuccessful) {
                                                            groupDetailFirstTabMemberListAdapter.submitList(
                                                                memberNewList.body()!!.members
                                                            )
                                                        }
                                                    }
                                                }
                                                return result.isSuccessful
                                            }
                                        })
                                        submitList(result.body()!!)
                                    }
                                binding.rvGroupRegistrationList.addItemDecoration(
                                    DividerItemDecoration(
                                        requireContext(),
                                        LinearLayoutManager.VERTICAL
                                    )
                                )
                            }
                        }
                    } else {
                        binding.layoutRegistrationList.visibility = View.GONE
                    }
                    groupDetailFirstTabMemberListAdapter = GroupDetailFirstTabMemberListAdapter(
                        amILeader,
                        activityViewModel.user.userId
                    ).apply {
                        setItemClickListener(object :
                            GroupDetailFirstTabMemberListAdapter.ItemClickListener {
                            override suspend fun onLeaderDelegateClick(user: GroupUser): Boolean {
                                activityViewModel.token?.let {
                                    val result = crewService.delegateLeader(
                                        makeHeaderByAccessToken(it.accessToken),
                                        crew.crewId,
                                        user.userId
                                    )
                                    if (result.isSuccessful) {
                                        showToast("위임에 성공했습니다!")
                                        val newList =
                                            crewService.getGroupDetailFirstTabData(crew.crewId)
                                        if (newList.isSuccessful) {
                                            Log.d(
                                                TAG,
                                                "onLeaderDelegateClick: 위임 후 값 설정 ${newList.body()!!.members.find { it.userId == activityViewModel.user.userId }?.leader ?: false}"
                                            )
                                            setAmILeader(
                                                newList.body()!!.members.find { it.userId == activityViewModel.user.userId }?.leader
                                                    ?: false
                                            )
                                            binding.layoutRegistrationList.visibility = View.GONE
                                            Log.d(TAG, "onLeaderDelegateClick: 위임 후 리스트 갱신")
                                            groupDetailFirstTabMemberListAdapter.submitList(null)
                                            groupDetailFirstTabMemberListAdapter.submitList(newList.body()!!.members)
                                            return true
                                        }
                                        return false
                                    } else {
                                        Log.d(TAG, "onLeaderDelegateClick: $result")
                                        showToast("위임에 실패했습니다!")
                                        return false
                                    }
                                }
                                return false
                            }

                            override suspend fun onKickClick(user: GroupUser) {
                                activityViewModel.token?.let {
                                    lifecycleScope.launch {
                                        val result = crewService.kickMember(
                                            makeHeaderByAccessToken(it.accessToken),
                                            crew.crewId,
                                            user.userId,
                                        )
                                        if (result.isSuccessful) {
                                            showToast("멤버를 강퇴했습니다!")
                                            val newList =
                                                crewService.getGroupDetailFirstTabData(crew.crewId)
                                            if (newList.isSuccessful) {
                                                groupDetailFirstTabMemberListAdapter.submitList(null)
                                                groupDetailFirstTabMemberListAdapter.submitList(
                                                    newList.body()!!.members
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                        })
                    }
                    binding.rvGroupMemberList.apply {
                        addItemDecoration(
                            DividerItemDecoration(
                                requireContext(),
                                LinearLayoutManager.VERTICAL
                            )
                        )
                        adapter = groupDetailFirstTabMemberListAdapter
                    }
                    groupDetailFirstTabMemberListAdapter.submitList(it.members)
                }
            }

        }
    }
}