package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.Const
import com.sansantek.sansanmulmul.config.Const.Companion.HIKINGSTYLE
import com.sansantek.sansanmulmul.config.Const.Companion.HikingBackgroundTintList
import com.sansantek.sansanmulmul.data.model.CrewInfo
import com.sansantek.sansanmulmul.databinding.ListGroupHikingStyleBinding

private const val TAG = "GroupDetailTabHikingSty 싸피"
class GroupDetailTabHikingStyleListAdapter():
    ListAdapter<Int, GroupDetailTabHikingStyleListAdapter.GroupHikingStyleListHolder>(Comparator) {

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
            Log.d(TAG, "bindInfo: ${HIKINGSTYLE[item]}")
            binding.tvHikingStyle.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, HikingBackgroundTintList[item])
            binding.tvHikingStyle.text = "#${HIKINGSTYLE[item]}"
            Log.d(TAG, "bindInfo: ${binding.tvHikingStyle.text}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHikingStyleListHolder {
        Log.d(TAG, "onCreateViewHolder: 뷰홀더 생산 중")
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