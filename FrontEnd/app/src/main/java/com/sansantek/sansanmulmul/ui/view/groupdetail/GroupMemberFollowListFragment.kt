package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.FollowUser
import com.sansantek.sansanmulmul.databinding.FragmentGroupMemberFollowListBinding
import com.sansantek.sansanmulmul.ui.adapter.MemberFollowListAdapter
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch

private const val TAG = "GroupMemberFollowListFr"

class GroupMemberFollowListFragment : BaseFragment<FragmentGroupMemberFollowListBinding>(
    FragmentGroupMemberFollowListBinding::bind,
    R.layout.fragment_group_member_follow_list
) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    companion object {
        private const val IS_FOLLOWER_LIST = "isFollowerList"

        fun newInstance(memberUserId: Int, isFollowerList: Boolean): GroupMemberFollowListFragment {
            val fragment = GroupMemberFollowListFragment()
            val args = Bundle().apply {
                putInt("followUserId", memberUserId)
                putBoolean(IS_FOLLOWER_LIST, isFollowerList)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val memberUserId = arguments?.getInt("followUserId") ?: return
        val isFollowerList = arguments?.getBoolean(IS_FOLLOWER_LIST) ?: true

        // TextView 텍스트 변경
        if (isFollowerList) {
            binding.tvFollwerList.text = "팔로워"
        } else {
            binding.tvFollwerList.text = "팔로잉"
        }

        // RecyclerView 설정 및 데이터 로드
        setupRecyclerView(memberUserId, isFollowerList)

        // 닫기 버튼 클릭 리스너 설정
        binding.ibCloseFollowList.setOnClickListener {
            // 이전 화면으로 돌아가기
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


    private fun setupRecyclerView(memberUserId: Int, isFollowerList: Boolean) {
        val accessToken = activityViewModel.token?.accessToken
        val currentUserNickName = activityViewModel.user.userNickName

        accessToken?.let { token ->
            val headerAccessToken = makeHeaderByAccessToken(token)
            lifecycleScope.launch {
                try {
                    // 내 팔로잉 리스트 가져오기
                    val followings = userService.getUserFollowing(headerAccessToken)
                    val followingNicknames = followings.map { it.nickName } // 팔로우 중인 사용자들의 닉네임 목록을 가져옴
                    Log.d(TAG, "내 팔로우 리스트: ${followings}")

                    // 어댑터 설정
                    val adapter = MemberFollowListAdapter(emptyList(), { user, isFollowing ->
                        handleFollowButtonClick(user, isFollowing, headerAccessToken)
                    }, followingNicknames, currentUserNickName)

                    // RecyclerView에 어댑터와 레이아웃 매니저 설정
                    binding.rvFollowList.layoutManager = LinearLayoutManager(context)
                    binding.rvFollowList.adapter = adapter

                    // 데이터 로드
                    loadListData(memberUserId, isFollowerList, adapter)
                } catch (e: Exception) {
                    Log.e(TAG, "팔로우 상태 확인 중 에러 발생", e)
                }
            }
        }
    }

    private fun loadListData(memberUserId: Int, isFollowerList: Boolean, adapter: MemberFollowListAdapter) {
        lifecycleScope.launch {
            try {
                val list = if (isFollowerList) {
                    userService.getMemberFollower(memberUserId)
                } else {
                    userService.getMemberFollowing(memberUserId)
                }

                // 어댑터에 데이터 업데이트
                adapter.updateList(list)

            } catch (e: Exception) {
                Log.e(TAG, "리스트 불러오기 실패", e)
            }
        }
    }

    private fun handleFollowButtonClick(user: FollowUser, isFollowing: Boolean, headerAccessToken: String) {
        lifecycleScope.launch {
            try {
                if (isFollowing) {
                    // 언팔로우 요청
                    val unfollowResponse = userService.deleteMemberFollow(headerAccessToken, user.userId)
                    if (unfollowResponse.isSuccessful) {
                        Log.d(TAG, "언팔로우 성공")
                        showToast("언팔로우 성공")
                        // 리스트를 다시 로드하거나 UI를 업데이트하여 상태 반영
                    } else {
                        Log.e(TAG, "언팔로우 실패: ${unfollowResponse.code()}")
                        showToast("언팔로우 실패")
                    }
                } else {
                    // 팔로우 요청
                    val followResponse = userService.addMemberFollow(headerAccessToken, activityViewModel.user.userId, user.userId)
                    if (followResponse.isSuccessful) {
                        Log.d(TAG, "팔로우 성공")
                        showToast("팔로우 성공")
                        // 리스트를 다시 로드하거나 UI를 업데이트하여 상태 반영
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


}
