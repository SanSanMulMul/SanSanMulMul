package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.databinding.ListSearchMountainBinding

data class CourseItem(
    val name: String,
    var isFavorite: Boolean = false // Default to not favorite
)

private const val TAG = "GroupCourceSearchListAd 싸피"
class GroupCourceSearchListAdapter :
    ListAdapter<String, GroupCourceSearchListAdapter.GroupCourseSearchListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    inner class GroupCourseSearchListHolder(private val binding: ListSearchMountainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(item: CourseItem) {
            binding.mountainImage.setImageResource(R.drawable.gayamountain)
            binding.mountainTitle.text = item.name
            binding.groupCourseNumberText.text = "코스 총 6개"



            // 버튼 클릭 시 즐겨찾기 상태 토글
            binding.btnFavoriteMountain.setOnClickListener {
                item.isFavorite = !item.isFavorite // Toggle the favorite state

                // 새로운 상태에 따라 버튼 이미지 업데이트
                binding.btnFavoriteMountain.setImageResource(
                    if (item.isFavorite) R.drawable.star_filled_favorite
                    else R.drawable.star_empty_favorite
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupCourseSearchListHolder {
        return GroupCourseSearchListHolder(
            ListSearchMountainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupCourseSearchListHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ${getItem(position)}")
        // 현재 위치의 항목을 가져와서 뷰 홀더에 바인딩
        val item = getItem(position)
        holder.bindInfo(CourseItem(item)) // 바인딩 메서드 호출
    }
}

