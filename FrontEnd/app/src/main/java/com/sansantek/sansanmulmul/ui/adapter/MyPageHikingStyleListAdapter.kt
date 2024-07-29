package com.sansantek.sansanmulmul.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.databinding.ListMyPageHikingStyleBinding

class MyPageHikingStyleListAdapter():
    ListAdapter<String, MyPageHikingStyleListAdapter.MyPageHikingStyleListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    inner class MyPageHikingStyleListHolder(private val binding: ListMyPageHikingStyleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            binding.tvHikingStyle.text = item.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageHikingStyleListHolder {
        return MyPageHikingStyleListHolder(
            ListMyPageHikingStyleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyPageHikingStyleListHolder, position: Int) {
        holder.bindInfo(position)
    }
}