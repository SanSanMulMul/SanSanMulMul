package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.databinding.ListGroupBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "GroupTabListAdapter_싸피"
class GroupTabListAdapter(private val isAllGroupLayout:Boolean):
    ListAdapter<Crew, GroupTabListAdapter.GroupInfoListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<Crew>() {
        override fun areItemsTheSame(oldItem: Crew, newItem: Crew): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: Crew, newItem: Crew): Boolean {
            return oldItem == newItem
        }
    }

    inner class GroupInfoListHolder(private val binding: ListGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            if(!isAllGroupLayout){
                binding.btnRegisterGroup.visibility = View.GONE
                binding.root.setOnClickListener{
                    itemClickListener.onGroupClick(position)
                }
            }
            val item = getItem(position)
            if(item.userJoined){
                binding.btnRegisterGroup.visibility = View.GONE
            }
            Log.d(TAG, "bindInfo: $item")
            Glide.with(binding.root).load(item.mountainImg).into(binding.groupImage)
            binding.groupTitle.text = item.crewName

            val originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val targetFormatter = DateTimeFormatter.ofPattern("yy.MM.dd HH:mm")
            val startDate = LocalDateTime.parse(item.crewStartDate, originalFormatter)
            val endDate = LocalDateTime.parse(item.crewEndDate, originalFormatter)
            val formattedStartDate = startDate.format(targetFormatter)
            val formattedEndDate = endDate.format(targetFormatter)

            binding.groupPersonInfo.text = "${item.crewCurrentMembers} / ${item.crewMaxMembers}"
            binding.groupSchedule.text = "${formattedStartDate} - ${formattedEndDate}"

            binding.btnRegisterGroup.setOnClickListener {
                itemClickListener.onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupInfoListHolder {
        return GroupInfoListHolder(
            ListGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupInfoListHolder, position: Int) {
        holder.bindInfo(position)
    }


    interface ItemClickListener {
        fun onClick(crew: Crew)
        fun onGroupClick(position: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}