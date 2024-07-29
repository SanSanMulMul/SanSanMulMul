package com.sansantek.sansanmulmul.ui.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainHistory
import com.sansantek.sansanmulmul.databinding.ListFavoriteMountainBinding
import com.sansantek.sansanmulmul.databinding.ListHistoryMountainBinding
import okhttp3.internal.format
import java.text.SimpleDateFormat
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
            binding.mountainImage.apply {
                setImageBitmap(ContextCompat.getDrawable(binding.root.context, item.mountainImg)!!.toBitmap())
                setColorFilter(Color.parseColor("#66000000"), PorterDuff.Mode.SRC_OVER)
            }
            binding.tvMountainTitle.text = item.mountainName
            binding.tvHistoryDate.text = formatDate(item.date)
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
}