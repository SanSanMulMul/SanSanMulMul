package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.CourseDetail
import com.sansantek.sansanmulmul.databinding.ItemMountainDetailCourseBinding

private const val TAG = "싸피_MountainDetailCourseLis"

class MountainDetailCourseListAdapter(
  private val itemClickListener: OnItemClickListener
) :
  ListAdapter<CourseDetail, MountainDetailCourseListAdapter.MountainDetailCourseViewHolder>(
    Comparator
  ) {

  interface OnItemClickListener {
    fun onItemClick(course: CourseDetail)
    fun onCourseInfoBtnClick(course: CourseDetail)
  }

  companion object Comparator : DiffUtil.ItemCallback<CourseDetail>() {
    override fun areItemsTheSame(oldItem: CourseDetail, newItem: CourseDetail): Boolean {
      return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
    }

    override fun areContentsTheSame(oldItem: CourseDetail, newItem: CourseDetail): Boolean {
      return oldItem == newItem
    }
  }

  inner class MountainDetailCourseViewHolder(private val binding: ItemMountainDetailCourseBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindInfo(position: Int) {
      val item = getItem(position)
      Log.d(TAG, "bindInfo: $item")
      binding.tvDifficulty.text = item.courseLevel
      binding.tvCourseName.text = item.courseName
      binding.tvDistance.text = "총 " + item.courseLength + " km"
      binding.tvDifficulty.also {
        if (it.text == "HARD") {
          it.text = "어려움"
          it.setBackgroundResource(R.drawable.rounded_difficulty_hard_circle)
        } else if (it.text == "MEDIUM") {
          it.text = "보통"
          it.setBackgroundResource(R.drawable.rounded_difficulty_medium_circle)
        } else if (it.text == "EASY") {
          it.text = "쉬움"
          it.setBackgroundResource(R.drawable.rounded_difficulty_easy_circle)
        }
      }
      binding.root.setOnClickListener { itemClickListener.onItemClick(item) }
      binding.btnCourseInfo.setOnClickListener {
        Log.d(TAG, "bindInfo: ${item.courseName} 클릭!")
        itemClickListener.onCourseInfoBtnClick(item)
      }
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): MountainDetailCourseViewHolder {
    return MountainDetailCourseViewHolder(
      ItemMountainDetailCourseBinding.inflate(
        LayoutInflater.from(
          parent.context
        ), parent, false
      )
    )
  }

  override fun onBindViewHolder(holder: MountainDetailCourseViewHolder, position: Int) {
    holder.bindInfo(position)
  }

}