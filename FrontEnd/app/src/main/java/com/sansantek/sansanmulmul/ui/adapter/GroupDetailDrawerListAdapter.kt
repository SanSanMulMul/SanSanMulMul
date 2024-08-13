package com.sansantek.sansanmulmul.ui.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.R.*
import com.sansantek.sansanmulmul.data.model.Alarm
import com.sansantek.sansanmulmul.databinding.ListGroupDetailAlarmBinding
import com.sansantek.sansanmulmul.databinding.ListGroupDetailDrawerMenuBinding
import com.sansantek.sansanmulmul.ui.util.Util

private const val TAG = "GroupDetailDrawerListAd 싸피"
class GroupDetailDrawerListAdapter():
    ListAdapter<Pair<Int, String>, GroupDetailDrawerListAdapter.GroupDetailAlarmListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<Pair<Int, String>>() {
        override fun areItemsTheSame(oldItem: Pair<Int, String>, newItem: Pair<Int, String>): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: Pair<Int, String>, newItem: Pair<Int, String>): Boolean {
            return oldItem == newItem
        }
    }

    inner class GroupDetailAlarmListHolder(private val binding: ListGroupDetailDrawerMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            Log.d(TAG, "bindInfo: $item")
            binding.ivDrawerIcon.setImageResource(item.first)
            binding.tvDrawerMenu.text = item.second
            binding.tvDrawerMenu.setOnClickListener {
                if(binding.tvDrawerMenu.text == "그룹 삭제"){
                    itemClickListener.onExitGroupClick()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupDetailAlarmListHolder {
        return GroupDetailAlarmListHolder(
            ListGroupDetailDrawerMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupDetailAlarmListHolder, position: Int) {
        holder.bindInfo(position)
    }


    interface ItemClickListener {
        fun onExitGroupClick()
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}