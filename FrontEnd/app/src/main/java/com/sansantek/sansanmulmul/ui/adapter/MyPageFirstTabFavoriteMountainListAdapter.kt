package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.databinding.ListFavoriteMountainBinding

private const val TAG = "MyPageFirstTabFavoriteMountainListAdapter_싸피"
class MyPageFirstTabFavoriteMountainListAdapter():
    ListAdapter<Mountain, MyPageFirstTabFavoriteMountainListAdapter.MyPageFavoriteMountainListHolder>(Comparator) {

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
            Log.d(TAG, "bindInfo: $itemCount")
            Log.d(TAG, "bindInfo: $item")
            binding.mountainImage.setImageBitmap(ContextCompat.getDrawable(binding.root.context, item.mountainImg)!!.toBitmap())
            binding.mountainTitle.text = item.mountainName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageFavoriteMountainListHolder {
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