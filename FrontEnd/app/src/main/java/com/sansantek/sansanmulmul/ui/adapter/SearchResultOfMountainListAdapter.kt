package com.sansantek.sansanmulmul.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.databinding.ItemSearchResultOfMountainBinding

class SearchResultOfMountainListAdapter(
    private val mountainList: List<Mountain>,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<SearchResultOfMountainListAdapter.MountainViewHolder>() {

    private var lastPosition = -1

    interface OnItemClickListener {
        fun onItemClick(mountain: Mountain)
    }

    inner class MountainViewHolder(private val binding: ItemSearchResultOfMountainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(position: Int) {
            val item = mountainList[position]
            binding.ivMountainImg.setImageResource(item.mountainImg)
            binding.tvMountainName.text = item.mountainName
            binding.tvCourseCnt.text = "코스 총 " + item.courseCnt + "개"

            binding.root.setOnClickListener { itemClickListener.onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MountainViewHolder {
        return MountainViewHolder(
            ItemSearchResultOfMountainBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MountainViewHolder, position: Int) {
        holder.bindInfo(position)

        // 애니메이션 설정
        setAnimation(holder.itemView, position)
    }

    override fun getItemCount() = mountainList.size

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // 애니메이션 딜레이 설정
        if (position > lastPosition) {
            val animation =
                AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_from_right)
            animation.startOffset = (position * 100).toLong()
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}