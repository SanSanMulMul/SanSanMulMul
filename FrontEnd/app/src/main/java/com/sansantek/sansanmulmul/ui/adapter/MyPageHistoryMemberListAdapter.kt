package com.sansantek.sansanmulmul.ui.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.GroupUser
import com.sansantek.sansanmulmul.data.model.HistoryMember
import com.sansantek.sansanmulmul.databinding.ListHistoryMemberInfoBinding
import com.sansantek.sansanmulmul.databinding.ListMyPageHikingStyleBinding

class MyPageHistoryMemberListAdapter():
    ListAdapter<GroupUser, MyPageHistoryMemberListAdapter.MyPageHistoryMemberListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<GroupUser>() {
        override fun areItemsTheSame(oldItem: GroupUser, newItem: GroupUser): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: GroupUser, newItem: GroupUser): Boolean {
            return oldItem == newItem
        }
    }

    inner class MyPageHistoryMemberListHolder(private val binding: ListHistoryMemberInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(position: Int) {
            val item = getItem(position)
            Glide.with(binding.root).load(item.userProfileImg).into(binding.ivPicture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageHistoryMemberListHolder {
        return MyPageHistoryMemberListHolder(
            ListHistoryMemberInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyPageHistoryMemberListHolder, position: Int) {
        holder.bindInfo(position)
    }
}