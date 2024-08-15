package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.FollowUser
import com.sansantek.sansanmulmul.data.model.GroupUser
import com.sansantek.sansanmulmul.databinding.FragmentGroupMemberFollowListBinding
import com.sansantek.sansanmulmul.ui.adapter.MemberFollowListAdapter
import com.sansantek.sansanmulmul.ui.adapter.MypageFollowListAdapter
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.view.groupdetail.GroupMemberDetailPageFragment
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch

private const val TAG = "MypageFollowListFragment"



class MypageFollowListFragment : BaseFragment<FragmentGroupMemberFollowListBinding>(
    FragmentGroupMemberFollowListBinding::bind,
    R.layout.fragment_group_member_follow_list
) {
    private val activityViewModel: MainActivityViewModel by activityViewModels()


    companion object {
        private const val IS_FOLLOWER_LIST = "isFollowerList"

        fun newInstance(isFollowerList: Boolean): MypageFollowListFragment {
            val fragment = MypageFollowListFragment()
            val args = Bundle().apply {
                putBoolean(IS_FOLLOWER_LIST, isFollowerList)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isFollowerList = arguments?.getBoolean(IS_FOLLOWER_LIST) ?: true

        // TextView 텍스트 변경
        if (isFollowerList) {
            binding.tvFollwerList.text = "팔로워"
        } else {
            binding.tvFollwerList.text = "팔로잉"
        }

        // RecyclerView 설정 및 데이터 로드
        setupRecyclerView(isFollowerList)

        // 닫기 버튼 클릭 리스너 설정
        binding.ibCloseFollowList.setOnClickListener {
            // 이전 화면으로 돌아가기
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView(isFollowerList: Boolean) {
        val accessToken = activityViewModel.token?.accessToken
        val currentUserNickName = activityViewModel.user.userNickName

        accessToken?.let { token ->
            val headerAccessToken = makeHeaderByAccessToken(token)
            lifecycleScope.launch {
                try {
                    // 내 팔로잉 리스트 가져오기
                    val followings = userService.getUserFollowing(headerAccessToken)
                    val followingNicknames = followings.map { it.nickName }
                    Log.d(TAG, "내 팔로우 리스트: ${followings}")

                    // 어댑터 설정
                    val adapter = MypageFollowListAdapter(emptyList(), { user, isFollowing ->
                         handleFollowButtonClick(user, isFollowing, headerAccessToken)
                    }, {user ->
                        run {
                            val activity = requireActivity() as MainActivity
                            activity.changeAddToBackstackFragment(GroupMemberDetailPageFragment.newInstance(user.userId))
                        }
                    },followingNicknames, currentUserNickName)

                    // RecyclerView에 어댑터와 레이아웃 매니저 설정
                    binding.rvFollowList.layoutManager = LinearLayoutManager(context)
                    binding.rvFollowList.adapter = adapter

                    // 데이터 로드
                    loadListData(isFollowerList, adapter)
                } catch (e: Exception) {
                    Log.e(TAG, "팔로우 상태 확인 중 에러 발생", e)
                }
            }
        }
    }

    private fun loadListData(isFollowerList: Boolean, adapter: MypageFollowListAdapter) {
        val accessToken = activityViewModel.token?.accessToken
        lifecycleScope.launch {
            try {
                accessToken?.let { token ->
                    val headerAccessToken = makeHeaderByAccessToken(token)
                    val list = if (isFollowerList) {
                        userService.getUserFollower(headerAccessToken)
                    } else {
                        userService.getUserFollowing(headerAccessToken)
                    }
                    // 어댑터에 데이터 업데이트
                    adapter.updateList(list)
                }

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
                        updateMyPageData()
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
    private suspend fun updateMyPageData() {
        activityViewModel.token?.let {
            // 마이 페이지 정보 다시 로드
            val myPageInfo = userService.getMyPageInfo(makeHeaderByAccessToken(it.accessToken))
            activityViewModel.setMyPageInfo(myPageInfo)

        }
    }
}
