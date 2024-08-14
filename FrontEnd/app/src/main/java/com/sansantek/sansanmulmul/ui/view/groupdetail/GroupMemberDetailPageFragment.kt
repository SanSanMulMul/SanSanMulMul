package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.config.Const.Companion.TITLE
import com.sansantek.sansanmulmul.data.model.User
import com.sansantek.sansanmulmul.databinding.FragmentGroupMemberDetailPageBinding
import com.sansantek.sansanmulmul.ui.adapter.MemberFollowListAdapter
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.view.mypagetab.GroupMemberDetailSecondFragment
import com.sansantek.sansanmulmul.ui.view.mypagetab.MyPageEditTabFragment
import com.sansantek.sansanmulmul.ui.view.mypagetab.MyPageFirstTabFragment
import com.sansantek.sansanmulmul.ui.view.mypagetab.MyPageSecondTabFragment
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch
import kotlin.math.log

private const val TAG = "GroupMemberDetailPageFr 싸피"
class GroupMemberDetailPageFragment(userId: Int) : BaseFragment<FragmentGroupMemberDetailPageBinding>(
    FragmentGroupMemberDetailPageBinding::bind,
    R.layout.fragment_group_member_detail_page) {

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

    private fun initClickListener() {

        binding.tlTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val memberUserId = arguments?.getInt("followUserId") ?: return // 사용자 ID를 가져오는 메서드 (예시)

                when (tab?.position) {
                    0 -> {
                        val firstTabFragment = GroupMemberDetailFirstTabFragment.newInstance(memberUserId)
                        replaceFragment(firstTabFragment)
                    }

                    else -> {
                        val secondTabFragment = GroupMemberDetailSecondFragment()
                        replaceFragment(secondTabFragment)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 다른 탭이 선택되었을 때의 동작
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 현재 탭이 다시 선택되었을 때의 동작
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val memberUserId = arguments?.getInt("followUserId")
        memberUserId?.let {
            Log.d(TAG, "onViewCreated: groupMember")
            replaceFragment(GroupMemberDetailFirstTabFragment.newInstance(it))
        }
        val accessToken = activityViewModel.token?.accessToken
        val currentUserId = activityViewModel.user.userId

        if (memberUserId != null) {
            loadMemberData(memberUserId, currentUserId, accessToken)
        } else {
            Log.e(TAG, "followUserId가 null입니다.")
        }
        initClickListener()

        // Follower 버튼 클릭 리스너
        binding.layoutMyFollowerInfo.setOnClickListener {
            memberUserId?.let { id ->
                val activity = requireActivity() as MainActivity
                val followerFragment = GroupMemberFollowListFragment.newInstance(id, true)
                activity.changeAddToBackstackFragment(followerFragment)
                Log.d(TAG, "팔로우 리스트로 이동완료 ")
            } ?: run {
                Log.e(TAG, "memberUserId가 null입니다.")
            }
        }

        // Following 버튼 클릭 리스너
        binding.layoutMyFollowingInfo.setOnClickListener {
            memberUserId?.let { id ->
                val activity = requireActivity() as MainActivity
                val followingFragment = GroupMemberFollowListFragment.newInstance(id, false)
                activity.changeAddToBackstackFragment(followingFragment)
                Log.d(TAG, "팔로잉 리스트로 이동완료 ")
            } ?: run {
                Log.e(TAG, "memberUserId가 null입니다.")
            }
        }
    }


    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: 뷰 종료")
        super.onDestroyView()
    }

    // 멤버 데이터 불러오기
    private fun loadMemberData(memberUserId: Int, currentUserId: Int, accessToken: String?) {
        lifecycleScope.launch {
            try {
                val response = userService.getMemberInfo(memberUserId)
                if (response.isSuccessful) {
                    val memberData = response.body()
                    memberData?.let { member ->
                        bindMemberData(member)
                        setupFollowButton(member.userNickName, memberUserId, currentUserId, accessToken)

                        // 팔로잉 및 팔로워 수 불러오기
                        loadMemberFollowingFollowerCount(memberUserId)
                    }
                } else {
                    Log.e(TAG, "데이터 로드 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "에러 발생", e)
            }
        }
    }

    // 멤버의 팔로잉 및 팔로워 수 불러오기
    private suspend fun loadMemberFollowingFollowerCount(memberUserId: Int) {
        try {
            // 팔로잉 수 가져오기
            val followingResponse = userService.getMemberFollowing(memberUserId)
            val followingCount = followingResponse.size

            // 팔로워 수 가져오기
            val followerResponse = userService.getMemberFollower(memberUserId)
            val followerCount = followerResponse.size

            // UI 업데이트
            binding.tvFollowingCnt.text = followingCount.toString()
            binding.tvFollowerCnt.text = followerCount.toString()

        } catch (e: Exception) {
            Log.e(TAG, "팔로잉 및 팔로워 수 불러오기 중 에러 발생", e)
        }
    }

    // 멤버 데이터 바인딩
    private fun bindMemberData(member: User) {
        binding.tvUserName.text = member.userNickName
        binding.tvTitleName.text = TITLE[member.userStaticBadge]
        Glide.with(binding.root)
            .load(member.userProfileImg)
            .into(binding.ivMyPageProfile)
    }

    // 팔로우, 팔로잉 버튼 로직
    private fun setupFollowButton(followUserNickName: String, memberUserId: Int, currentUserId: Int, accessToken: String?) {
        // 본인의 상세 페이지이면 팔로우 버튼 숨기기
        if (memberUserId == currentUserId) {
            binding.btnFollow.visibility = View.GONE
            return
        }

        accessToken?.let { token ->
            val headerAccessToken = makeHeaderByAccessToken(token)
            lifecycleScope.launch {
                try {
                    val followings = userService.getUserFollowing(headerAccessToken)
                    var isFollowing = followings.any { it.nickName == followUserNickName }
                    Log.d(TAG, "내 팔로우 리스트: ${followings}")
                    Log.d(TAG, "팔로우 중인가?: ${isFollowing}")
                    updateFollowButton(isFollowing)

                    binding.btnFollow.setOnClickListener {
                        // 버튼 클릭 시 현재 팔로우 상태 확인 후 반대 작업 수행
                        lifecycleScope.launch {
                            try {
                                if (isFollowing) {
                                    // 언팔로우 요청
                                    val unfollowResponse = userService.deleteMemberFollow(headerAccessToken, memberUserId)
                                    if (unfollowResponse.isSuccessful) {
                                        Log.d(TAG, "언팔로우 성공")
                                        showToast("언팔로우 성공")
                                        isFollowing = false
                                        updateFollowButton(isFollowing)
                                        loadMemberFollowingFollowerCount(memberUserId)
                                        updateMyPageData()
                                    } else {
                                        Log.e(TAG, "언팔로우 실패: ${unfollowResponse.code()}")
                                        showToast("언팔로우 실패")
                                    }
                                } else {
                                    // 팔로우 요청
                                    val followResponse = userService.addMemberFollow(headerAccessToken, currentUserId, memberUserId)
                                    if (followResponse.isSuccessful) {
                                        Log.d(TAG, "팔로우 성공")
                                        showToast("팔로우 성공")
                                        isFollowing = true
                                        updateFollowButton(isFollowing)
                                        loadMemberFollowingFollowerCount(memberUserId)
                                        updateMyPageData()
                                    } else {
                                        Log.e(TAG, "팔로우 실패: ${followResponse.code()}")
                                        showToast("팔로우 실패")
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "팔로우/언팔로우 에러", e)
                                showToast("에러가 발생했습니다.")
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "팔로우 상태 확인 중 에러 발생", e)
                    binding.btnFollow.isEnabled = false
                }
            }
        }
    }

    // 팔로우 팔로잉 버튼 초기 세팅
    private fun updateFollowButton(isFollowing: Boolean) {
        if (isFollowing) {
            binding.btnFollow.text = "팔로잉 ✓"
            binding.btnFollow.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.btnFollow.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white                )
            )
        } else {
            binding.btnFollow.text = "팔로우"
            binding.btnFollow.setTextColor(ContextCompat.getColor(requireContext(), R.color.white)) // 텍스트 색상을 흰색으로 설정
            binding.btnFollow.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.group_detail_second_tab_temperature_min_color                )
            )
        }
        binding.btnFollow.isEnabled = true
    }


    fun replaceFragment(view: Fragment) {
        Log.d(TAG, "replaceFragment: 실행")
        childFragmentManager.beginTransaction().replace(binding.myPageFragmentView.id, view)
            .commit()
    }

    private suspend fun updateMyPageData() {
        activityViewModel.token?.let {
            // 마이 페이지 정보 다시 로드
            val myPageInfo = userService.getMyPageInfo(makeHeaderByAccessToken(it.accessToken))
            activityViewModel.setMyPageInfo(myPageInfo)

        }
    }
}
