package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.databinding.ListGroupHikingStyleBinding

private const val TAG = "GroupDetailTabHikingSty 싸피"
class GroupDetailTabHikingStyleListAdapter():
    ListAdapter<String, GroupDetailTabHikingStyleListAdapter.GroupHikingStyleListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    inner class GroupHikingStyleListHolder(private val binding: ListGroupHikingStyleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            binding.tvHikingStyle.text = item.toString()
            Log.d(TAG, "bindInfo: ${binding.tvHikingStyle.text}")
            when(item){
                "#등산도 식후경" ->{
                    binding.tvHikingStyle.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.pink)
                }
                "#등산에 집중" -> {
                    binding.tvHikingStyle.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.light_green)
                }
                "#어쩌구" -> {
                    binding.tvHikingStyle.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.light_blue)
                }
                "#저쩌구" -> {
                    binding.tvHikingStyle.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, R.color.light_purple)
                }
                else -> {

                }
            }
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