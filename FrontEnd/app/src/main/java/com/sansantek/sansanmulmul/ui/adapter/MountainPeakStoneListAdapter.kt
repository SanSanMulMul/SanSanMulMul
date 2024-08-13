package com.sansantek.sansanmulmul.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.data.model.MountainPeakStoneItem
import com.sansantek.sansanmulmul.databinding.ItemMountainPeakStoneBinding

class MountainPeakStoneListAdapter :
  ListAdapter<MountainPeakStoneItem, MountainPeakStoneListAdapter.MountainPeakStoneViewHolder>(
    MountainPeakStoneListAdapter
  ) {

  companion object Comparator : DiffUtil.ItemCallback<MountainPeakStoneItem>() {
    override fun areItemsTheSame(
      oldItem: MountainPeakStoneItem,
      newItem: MountainPeakStoneItem
    ): Boolean {
      return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
    }

    override fun areContentsTheSame(
      oldItem: MountainPeakStoneItem,
      newItem: MountainPeakStoneItem
    ): Boolean {
      return oldItem == newItem
    }
  }

  inner class MountainPeakStoneViewHolder(private val binding: ItemMountainPeakStoneBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindInfo(position: Int) {
      val item = getItem(position)
      Glide.with(binding.root).load(item.stoneImg)
        .into(binding.ivStone)
      binding.tvStone.text = item.stoneName
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): MountainPeakStoneViewHolder {
    return MountainPeakStoneViewHolder(
      ItemMountainPeakStoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
  }

  override fun onBindViewHolder(holder: MountainPeakStoneViewHolder, position: Int) {
    holder.bindInfo(position)
  }


}