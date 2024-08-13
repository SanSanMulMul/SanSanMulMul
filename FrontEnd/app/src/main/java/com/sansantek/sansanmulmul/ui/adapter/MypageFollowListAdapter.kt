package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.data.model.FollowUser
import com.sansantek.sansanmulmul.databinding.ListFollowMemberBinding

private const val TAG = "MypageFollowListAdapter"

class MypageFollowListAdapter(
    private var followList: List<FollowUser>,
    private val onFollowClick: (FollowUser, Boolean) -> Unit,
    private val followingNicknames: List<String?>, // 현재 사용자가 팔로우하고 있는 닉네임 목록
    private val currentUserNickName: String? // 현재 사용자의 닉네임
) : RecyclerView.Adapter<MypageFollowListAdapter.FollowViewHolder>() {

    inner class FollowViewHolder(private val binding: ListFollowMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: FollowUser) {
            binding.tvMemberNickname.text = user.nickName
            binding.tvMemberBadge.text = user.title

            Log.d(TAG, "마이페이지 팔로우 리스트 닉네임: ${user.nickName}")

            Glide.with(binding.root.context)
                .load(user.imgUrl)
                .circleCrop()
                .into(binding.memberImage)

            Log.d(TAG, "내 팔로잉 목록: ${followingNicknames}")

            // 현재 사용자가 이 유저를 팔로우하고 있는지 확인
            val isFollowing = followingNicknames.contains(user.nickName)

//            // 프로필 클릭 리스너 추가
//            binding.root.setOnClickListener {
//                onProfileClick(user, user.nickName == currentUserNickName)
//            }

            // 팔로우 버튼 텍스트 및 클릭 리스너 설정
            if (user.nickName == currentUserNickName) {
                // 자신의 계정일 경우 버튼 숨김
                binding.btnFollow.visibility = View.GONE
            } else {
                binding.btnFollow.visibility = View.VISIBLE
                if (isFollowing) {
                    binding.btnFollow.text = "팔로잉"
                } else {
                    binding.btnFollow.text = "팔로우"
                }

                binding.btnFollow.setOnClickListener {
                    onFollowClick(user, isFollowing)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val binding = ListFollowMemberBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FollowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.bind(followList[position])
    }

    override fun getItemCount(): Int = followList.size

    // 새로운 리스트로 데이터를 업데이트하는 메서드 추가
    fun updateList(newList: List<FollowUser>) {
        followList = newList
        notifyDataSetChanged()  // 데이터 변경 사항을 어댑터에 알림
    }
}
