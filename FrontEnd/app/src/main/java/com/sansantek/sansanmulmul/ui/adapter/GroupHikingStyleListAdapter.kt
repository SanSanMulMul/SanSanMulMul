package com.sansantek.sansanmulmul.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.Const.Companion.HIKINGSTYLE
import com.sansantek.sansanmulmul.config.Const.Companion.HikingBackgroundTintList
import com.sansantek.sansanmulmul.data.model.HikingStyle
import com.sansantek.sansanmulmul.databinding.ListGroupHikingStyleBinding

class GroupHikingStyleListAdapter():
    ListAdapter<Int, GroupHikingStyleListAdapter.GroupHikingStyleListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    inner class GroupHikingStyleListHolder(private val binding: ListGroupHikingStyleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            binding.tvHikingStyle.text = HIKINGSTYLE[item]
            binding.tvHikingStyle.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, HikingBackgroundTintList[item])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHikingStyleListHolder {
        return GroupHikingStyleListHolder(
            ListGroupHikingStyleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupHikingStyleListHolder, position: Int) {
        holder.bindInfo(position)
    }
}