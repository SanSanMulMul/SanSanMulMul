package com.sansantek.sansanmulmul.ui.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.data.model.MountainHistory
import com.sansantek.sansanmulmul.databinding.ListHistoryMountainBinding
import java.text.SimpleDateFormat
import java.util.Date

class GroupMemberDetailFirstTabHistoryMountainListAdapter :
    ListAdapter<MountainHistory, GroupMemberDetailFirstTabHistoryMountainListAdapter.GroupMemberHistoryMountainListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<MountainHistory>() {
        override fun areItemsTheSame(oldItem: MountainHistory, newItem: MountainHistory): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: MountainHistory, newItem: MountainHistory): Boolean {
            return oldItem == newItem
        }
    }

    inner class GroupMemberHistoryMountainListHolder(private val binding: ListHistoryMountainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            Glide.with(binding.root).load(item.mountainImg).into(binding.mountainImage)
            binding.mountainImage.apply {
                setColorFilter(Color.parseColor("#66000000"), PorterDuff.Mode.SRC_OVER)
            }
            binding.tvMountainTitle.text = item.mountainName
            binding.tvHistoryDate.text = item.recordStartTime
            binding.root.setOnClickListener {
                itemClickListener.onHistoryClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMemberHistoryMountainListHolder {
        return GroupMemberHistoryMountainListHolder(
            ListHistoryMountainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupMemberHistoryMountainListHolder, position: Int) {
        holder.bindInfo(position)
    }

    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("YYYY.MM.dd")
        return dateFormat.format(date)
    }

    interface ItemClickListener {
        fun onHistoryClick(position: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}
