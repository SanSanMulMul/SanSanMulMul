package com.sansantek.sansanmulmul.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.databinding.ListFavoriteMountainBinding

class MyPageFirstTabFavoriteMountainListAdapter() :
    ListAdapter<Mountain, MyPageFirstTabFavoriteMountainListAdapter.MyPageFavoriteMountainListHolder>(
        Comparator
    ) {

    companion object Comparator : DiffUtil.ItemCallback<Mountain>() {
        override fun areItemsTheSame(oldItem: Mountain, newItem: Mountain): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: Mountain, newItem: Mountain): Boolean {
            return oldItem == newItem
        }
    }

    inner class MyPageFavoriteMountainListHolder(private val binding: ListFavoriteMountainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            Glide.with(binding.root).load(item.mountainImg).into(binding.mountainImage)
            binding.mountainTitle.text = item.mountainName
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPageFavoriteMountainListHolder {
        return MyPageFavoriteMountainListHolder(
            ListFavoriteMountainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyPageFavoriteMountainListHolder, position: Int) {
        holder.bindInfo(position)
    }
}