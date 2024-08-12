package com.sansantek.sansanmulmul.ui.view.groupdetail

import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.userService
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.config.Const.Companion.TITLE
import com.sansantek.sansanmulmul.data.model.GroupUser
import com.sansantek.sansanmulmul.databinding.FragmentGroupDetailTabFirstInfoFragmentBinding
import com.sansantek.sansanmulmul.databinding.FragmentGroupMemberDetailPageBinding
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch

private const val TAG = "GroupMemberDetailPageFr 싸피"
class GroupMemberDetailPageFragment : BaseFragment<FragmentGroupMemberDetailPageBinding>(
    FragmentGroupMemberDetailPageBinding::bind,
    R.layout.fragment_group_member_detail_page) {

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    companion object {
        // `newInstance` 메서드 정의
        fun newInstance(userId: Int): GroupMemberDetailPageFragment {
            val fragment = GroupMemberDetailPageFragment()
            val args = Bundle().apply {
                putInt("userId", userId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = arguments?.getInt("userId")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 userId를 가져옴
        val userId = arguments?.getInt("userId")
        Log.d(TAG, "onViewCreated: ${userId}")
        // userId로 멤버 데이터를 가져오기 (닉네임, 칭호, 이미지)
        if (userId != null) {
            lifecycleScope.launch {
                try {
                    val response = userService.getMemberInfo(userId)
                    if (response.isSuccessful) {
                        val memberData = response.body()
                        Log.d(TAG, "memberData: $memberData")

                        // 가져온 데이터를 UI에 반영
                        binding.tvUserName.text = memberData?.userNickName
                        memberData?.let { member ->
                            binding.tvTitleName.text = TITLE[member.userStaticBadge]
                        }
                        // Glide를 사용하여 프로필 이미지 로드
                        Glide.with(binding.root)
                            .load(memberData?.userProfileImg)
                            .into(binding.ivMyPageProfile)
                    } else {
                        Log.e(TAG, "onViewCreated: 데이터 로드 실패: ${response.code()}")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "onViewCreated: 에러 발생", e)
                }
            }
            // 팔로우 추가
            lifecycleScope.launch {
                try {
                    val response = userService.addMemberFollow(userId)
                    if (response.isSuccessful) {
                        // 팔로우 성공 시 처리할 로직
                        Log.d(TAG, "팔로우 성공")
                        showToast("팔로우 성공")

                        // 필요 시 UI 업데이트 로직
                        // 예: 팔로우 버튼 비활성화 또는 텍스트 변경
                        binding.btnFollow.isEnabled = false
                        binding.btnFollow.text = "팔로잉"
                    } else {
                        // 팔로우 실패 시 처리할 로직
                        Log.e(TAG, "팔로우 추가 실패: ${response.code()}")
                        showToast("팔로우 추가에 실패했습니다.")
                    }
                } catch (e: Exception) {
                    // 네트워크 오류 등 예외 처리
                    Log.e(TAG, "팔로우 추가 중 오류 발생", e)
                    showToast("오류가 발생했습니다. 다시 시도해주세요.")
                }
            }

            // 팔로우 취소
            val accessToken = activityViewModel.token?.let {
                lifecycleScope.launch {
                    try {
                        val response = userService.deleteMemberFollow(accessToken)
                    }
                }
            }

        }
    }
}