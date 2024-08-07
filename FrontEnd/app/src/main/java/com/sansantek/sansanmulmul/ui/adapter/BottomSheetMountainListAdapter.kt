package com.sansantek.sansanmulmul.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.SearchMountainListItem
import com.sansantek.sansanmulmul.databinding.ItemBottomSheetMountainBinding

class BottomSheetMountainListAdapter(
    private val mountainList: List<SearchMountainListItem>,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<BottomSheetMountainListAdapter.MountainViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(mountain: SearchMountainListItem)
    }

    inner class MountainViewHolder(private val binding: ItemBottomSheetMountainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(item: SearchMountainListItem) {
            Glide.with(binding.root.context)
                .load(item.mountainImg)
                .into(binding.ivMountain)
            binding.tvMountainName.text = item.mountainName
            binding.tvMountainCourseCnt.text = "코스 총 " + item.courseCnt + "개"

            binding.root.setOnClickListener { itemClickListener.onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MountainViewHolder {
        return MountainViewHolder(
            ItemBottomSheetMountainBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MountainViewHolder, position: Int) {
        holder.bindInfo(mountainList[position])
    }

    override fun getItemCount() = mountainList.size

}