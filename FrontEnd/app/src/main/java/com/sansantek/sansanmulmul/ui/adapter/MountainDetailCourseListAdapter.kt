package com.sansantek.sansanmulmul.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.Course
import com.sansantek.sansanmulmul.databinding.ItemMountainDetailCourseBinding

class MountainDetailCourseListAdapter(private val courseList: List<Course>) :
  RecyclerView.Adapter<MountainDetailCourseListAdapter.MountainDetailCourseViewHolder>() {

  inner class MountainDetailCourseViewHolder(private val binding: ItemMountainDetailCourseBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindInfo(position: Int) {
      val item = courseList[position]
      binding.tvDifficulty.text = item.difficulty
      binding.tvCourseName.text = item.courseName
      binding.tvStart.text = item.start
      binding.tvEnd.text = item.end
      binding.tvDistance.text = "총" + item.distance + "km"

      binding.tvDifficulty.also {
        if (it.text == "어려움") {
          it.setBackgroundResource(R.drawable.rounded_difficulty_hard_circle)
        } else if (it.text == "보통") {
          it.setBackgroundResource(R.drawable.rounded_difficulty_medium_circle)
        } else if (it.text == "쉬움") {
          it.setBackgroundResource(R.drawable.rounded_difficulty_easy_circle)
        }
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

  override fun getItemCount() = courseList.size

}