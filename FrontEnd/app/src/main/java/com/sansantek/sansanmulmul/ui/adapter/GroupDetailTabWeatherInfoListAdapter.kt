package com.sansantek.sansanmulmul.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.Weather
import com.sansantek.sansanmulmul.databinding.ListWeatherInfoBinding
import java.text.SimpleDateFormat
import java.util.Date

class GroupDetailTabWeatherInfoListAdapter():
    ListAdapter<Weather, GroupDetailTabWeatherInfoListAdapter.GroupHikingStyleListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<Weather>() {
        override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
            return oldItem == newItem
        }
    }

    inner class GroupHikingStyleListHolder(private val binding: ListWeatherInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            when(position){
                0 -> {
                    binding.date.text = "오늘"
                }
                1 -> {
                    binding.date.text = "내일"
                }
                2 -> {
                    binding.date.text = "모레"
                }
                else -> {
                    binding.date.text = formatDate(item.date)
                }
            }
            when(item.condition){
                "맑음" -> {
                    binding.condition.setImageBitmap(ContextCompat.getDrawable(binding.root.context, R.drawable.sunny)!!.toBitmap())
                }
                "흐림" -> {
                    binding.condition.setImageBitmap(ContextCompat.getDrawable(binding.root.context, R.drawable.cloudy)!!.toBitmap())
                }
                "강수" -> {
                    binding.condition.setImageBitmap(ContextCompat.getDrawable(binding.root.context, R.drawable.rainy)!!.toBitmap())
                }
            }
            binding.maxTemperature.text = "${item.maxTemperature}º"
            binding.minTemperature.text = "${item.minTemperature}º"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHikingStyleListHolder {
        return GroupHikingStyleListHolder(
            ListWeatherInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupHikingStyleListHolder, position: Int) {
        holder.bindInfo(position)
    }

    fun formatDate(date: Date): String {
        // SimpleDateFormat을 이용하여 "MM/dd" 형식으로 포맷팅
        val dateFormat = SimpleDateFormat("MM/dd")
        return dateFormat.format(date)
    }
}