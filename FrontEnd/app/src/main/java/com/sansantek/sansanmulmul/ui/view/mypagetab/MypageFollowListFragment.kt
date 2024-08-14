package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupMemberFollowListBinding
import com.sansantek.sansanmulmul.ui.adapter.MemberFollowListAdapter
import com.sansantek.sansanmulmul.ui.adapter.MypageFollowListAdapter
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
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
                        // handleFollowButtonClick(user, isFollowing, headerAccessToken)
                    }, followingNicknames, currentUserNickName)

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
}
