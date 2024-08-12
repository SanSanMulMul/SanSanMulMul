package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.config.Const.Companion.TITLE
import com.sansantek.sansanmulmul.data.model.User
import com.sansantek.sansanmulmul.databinding.FragmentGroupMemberDetailPageBinding
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch

private const val TAG = "GroupMemberDetailPageFr 싸피"
class GroupMemberDetailPageFragment : BaseFragment<FragmentGroupMemberDetailPageBinding>(
    FragmentGroupMemberDetailPageBinding::bind,
    R.layout.fragment_group_member_detail_page) {

    private val activityViewModel: MainActivityViewModel by activityViewModels()

    companion object {
        fun newInstance(followUserId: Int): GroupMemberDetailPageFragment {
            val fragment = GroupMemberDetailPageFragment()
            val args = Bundle().apply {
                putInt("followUserId", followUserId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val accessToken = activityViewModel.token?.accessToken
        val currentUserId = activityViewModel.user.userId
        val followUserId = arguments?.getInt("followUserId")

        if (followUserId != null) {
            loadMemberData(followUserId, currentUserId, accessToken)
        } else {
            Log.e(TAG, "followUserId가 null입니다.")
        }
    }

    // 멤버 데이터 불러오기
    private fun loadMemberData(followUserId: Int, currentUserId: Int, accessToken: String?) {
        lifecycleScope.launch {
            try {
                val response = userService.getMemberInfo(followUserId)
                if (response.isSuccessful) {
                    val memberData = response.body()
                    memberData?.let { member ->
                        bindMemberData(member)
                        setupFollowButton(member.userNickName, followUserId, currentUserId, accessToken)

                        // 팔로잉 및 팔로워 수 불러오기
                        loadMemberFollowingFollowerCount(followUserId)
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
    private suspend fun loadMemberFollowingFollowerCount(followUserId: Int) {
        try {
            // 팔로잉 수 가져오기
            val followingResponse = userService.getMemberFollowing(followUserId)
            val followingCount = followingResponse.size

            // 팔로워 수 가져오기
            val followerResponse = userService.getMemberFollower(followUserId)
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
    private fun setupFollowButton(followUserNickName: String, followUserId: Int, currentUserId: Int, accessToken: String?) {
        // 본인의 상세 페이지이면 팔로우 버튼 숨기기
        if (followUserId == currentUserId) {
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
                                    val unfollowResponse = userService.deleteMemberFollow(headerAccessToken, followUserId)
                                    if (unfollowResponse.isSuccessful) {
                                        Log.d(TAG, "언팔로우 성공")
                                        showToast("언팔로우 성공")
                                        isFollowing = false
                                        updateFollowButton(isFollowing)
                                    } else {
                                        Log.e(TAG, "언팔로우 실패: ${unfollowResponse.code()}")
                                        showToast("언팔로우 실패")
                                    }
                                } else {
                                    // 팔로우 요청
                                    val followResponse = userService.addMemberFollow(headerAccessToken, currentUserId, followUserId)
                                    if (followResponse.isSuccessful) {
                                        Log.d(TAG, "팔로우 성공")
                                        showToast("팔로우 성공")
                                        isFollowing = true
                                        updateFollowButton(isFollowing)
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
        binding.btnFollow.text = if (isFollowing) "팔로잉" else "팔로우"
        binding.btnFollow.isEnabled = true
    }
}
