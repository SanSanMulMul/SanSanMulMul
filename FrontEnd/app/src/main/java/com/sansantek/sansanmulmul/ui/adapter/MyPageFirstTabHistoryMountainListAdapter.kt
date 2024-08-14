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
import java.time.DayOfWeek
import java.time.LocalDateTime.*
import java.time.format.DateTimeFormatter
import java.util.Date

private const val TAG = "MyPageFirstTabHistoryMountainListAdapter_싸피"
class MyPageFirstTabHistoryMountainListAdapter():
    ListAdapter<MountainHistory, MyPageFirstTabHistoryMountainListAdapter.MyPageHistoryMountainListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<MountainHistory>() {
        override fun areItemsTheSame(oldItem: MountainHistory, newItem: MountainHistory): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: MountainHistory, newItem: MountainHistory): Boolean {
            return oldItem == newItem
        }
    }

    inner class MyPageHistoryMountainListHolder(private val binding: ListHistoryMountainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            Glide.with(binding.root).load(item.mountainImg).into(binding.mountainImage)
            binding.mountainImage.apply {
                setColorFilter(Color.parseColor("#66000000"), PorterDuff.Mode.SRC_OVER)
            }
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val dateTime = parse(item.recordStartTime, formatter)
            val outputFormatter = DateTimeFormatter.ofPattern("yy-MM-dd")
            val formattedDate = dateTime.format(outputFormatter)
            binding.tvMountainTitle.text = item.mountainName

            val dayOfWeek: DayOfWeek = dateTime.dayOfWeek
            val koreanDayOfWeek = when (dayOfWeek) {
                DayOfWeek.MONDAY -> "월"
                DayOfWeek.TUESDAY -> "화"
                DayOfWeek.WEDNESDAY -> "수"
                DayOfWeek.THURSDAY -> "목"
                DayOfWeek.FRIDAY -> "금"
                DayOfWeek.SATURDAY -> "토"
                DayOfWeek.SUNDAY -> "일"
            }

            binding.tvHistoryDate.text = "$formattedDate($koreanDayOfWeek)"

            binding.root.setOnClickListener {
                itemClickListener.onHistoryClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageHistoryMountainListHolder {
        return MyPageHistoryMountainListHolder(
            ListHistoryMountainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyPageHistoryMountainListHolder, position: Int) {
        holder.bindInfo(position)
    }

    fun formatDate(date: Date): String {
        // SimpleDateFormat을 이용하여 "MM/dd" 형식으로 포맷팅
        val dateFormat = SimpleDateFormat("YYYY.MM.dd")
        return dateFormat.format(date)
    }

    interface ItemClickListener {
        fun onHistoryClick(mountainHistory: MountainHistory)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}