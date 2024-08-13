package com.sansantek.sansanmulmul.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.config.Const.Companion.TITLE
import com.sansantek.sansanmulmul.data.model.User
import com.sansantek.sansanmulmul.databinding.ListFollowMemberBinding

class MemberFollowListAdapter(
    private val followList: List<User>,
    private val onFollowClick: (User) -> Unit
) : RecyclerView.Adapter<MemberFollowListAdapter.FollowViewHolder>() {

    inner class FollowViewHolder(private val binding: ListFollowMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.tvMemberNickname.text = user.userNickName
            binding.tvMemberBadge.text = TITLE[user.userStaticBadge]

            Glide.with(binding.root.context)
                .load(user.userProfileImg)
                .circleCrop() // 이미지를 원형으로 표시
                .into(binding.memberImage)

            binding.btnFollow.setOnClickListener {
                onFollowClick(user)
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
}
