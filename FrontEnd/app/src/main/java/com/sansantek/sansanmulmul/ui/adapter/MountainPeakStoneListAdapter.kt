package com.sansantek.sansanmulmul.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.MountainPeakStone
import com.sansantek.sansanmulmul.databinding.ItemMountainPeakStoneBinding

private const val TAG = "싸피_MountainPeakStoneListAd"

class MountainPeakStoneListAdapter :
    ListAdapter<MountainPeakStone, MountainPeakStoneListAdapter.MountainPeakStoneViewHolder>(
        MountainPeakStoneListAdapter
    ) {

    private var stoneList: MutableList<MountainPeakStone> = mutableListOf()

    // 내가 인증된 비석 정보인 STONElIST를 세팅하는 함수
    // 세팅하면 기존에 SUBMITLIST로 가지고 있던 전체 비석 리스트를 일부러 날리고
    // 다시 SUBMIT을 호출해서 RECYCLERVIEW 재생성
    fun setStoneList(list: MutableList<MountainPeakStone>) {
        this.stoneList = list
        val tempList = currentList.toMutableList()
        Log.d(TAG, "setStoneList: $stoneList")
        submitList(null)
        submitList(tempList)
    }

    companion object Comparator : DiffUtil.ItemCallback<MountainPeakStone>() {
        override fun areItemsTheSame(
            oldItem: MountainPeakStone,
            newItem: MountainPeakStone
        ): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(
            oldItem: MountainPeakStone,
            newItem: MountainPeakStone
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

            if (stoneList.any { it.stoneId == item.stoneId }) {
                binding.ivStone.imageTintList = null
                binding.ivStone.background =
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.rounded_rectangle_stone_green_20
                    )
            } else {
                binding.ivStone.imageTintList = ColorStateList.valueOf(Color.GRAY)
                binding.ivStone.background =
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.rounded_rectangle_stone_20
                    )
            }
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