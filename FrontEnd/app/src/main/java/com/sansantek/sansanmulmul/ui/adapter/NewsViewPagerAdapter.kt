package com.sansantek.sansanmulmul.ui.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.data.model.News
import com.sansantek.sansanmulmul.databinding.ItemNewsBinding

private const val TAG = "NewsViewPagerAdapter 싸피"

class NewsViewPagerAdapter() :
    ListAdapter<News, NewsViewPagerAdapter.RecommendationViewHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }
    inner class RecommendationViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            val item = getItem(position)
            Log.d(TAG, "bind: $item")
            binding.tvMountainName.text = item.mountainName
            binding.tvNewsTitle.text = item.title
            Glide.with(binding.root).load(item.mountainImg).into(binding.ivNewsImg)
            binding.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(position)
    }
}

