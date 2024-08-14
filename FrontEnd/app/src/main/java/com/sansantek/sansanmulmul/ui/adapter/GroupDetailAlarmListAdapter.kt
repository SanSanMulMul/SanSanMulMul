package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.data.model.Alarm
import com.sansantek.sansanmulmul.databinding.ListGroupDetailAlarmBinding
import com.sansantek.sansanmulmul.ui.util.Util.extractHikingCourse
import com.sansantek.sansanmulmul.ui.util.Util.extractHikingSchedule
import com.sansantek.sansanmulmul.ui.util.Util.extractJoinRequests

private const val TAG = "GroupDetailAlarmListAda 싸피"
class GroupDetailAlarmListAdapter():
    ListAdapter<Alarm, GroupDetailAlarmListAdapter.GroupDetailAlarmListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem == newItem
        }
    }

    inner class GroupDetailAlarmListHolder(private val binding: ListGroupDetailAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            Log.d(TAG, "bindInfo: $item")
            binding.tvAlarmTitle.text = item.alarmTitle
            binding.tvAlarmContent.text = item.alarmBody
            binding.tvAlarmContent.text = if(extractJoinRequests(binding.root.context, binding.tvAlarmContent.text.toString()) != null){
                Log.d(TAG, "bindInfo: 멤버 변경 실행")
                extractJoinRequests(binding.root.context, binding.tvAlarmContent.text.toString())!!
            }
            else if(extractHikingSchedule(binding.root.context, binding.tvAlarmContent.text.toString()) != null){
                Log.d(TAG, "bindInfo: 하이킹 계획 변경 실행")
                extractHikingSchedule(binding.root.context, binding.tvAlarmContent.text.toString())
            }
            else {
                Log.d(TAG, "bindInfo: 하이킹 코스 변경 실행")
                extractHikingCourse(binding.root.context, binding.tvAlarmContent.text.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupDetailAlarmListHolder {
        return GroupDetailAlarmListHolder(
            ListGroupDetailAlarmBinding.inflate(
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
        fun onClick(position: Int) {
        }
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}