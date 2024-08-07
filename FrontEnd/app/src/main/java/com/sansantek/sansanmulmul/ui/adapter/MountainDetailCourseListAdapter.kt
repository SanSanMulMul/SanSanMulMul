package com.sansantek.sansanmulmul.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.CourseDetail
import com.sansantek.sansanmulmul.databinding.ItemMountainDetailCourseBinding

private const val TAG = "산산물물_MountainDetailCourseLis"

class MountainDetailCourseListAdapter(
    private val courseList: List<CourseDetail>,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<MountainDetailCourseListAdapter.MountainDetailCourseViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(course: CourseDetail)
    }

    inner class MountainDetailCourseViewHolder(private val binding: ItemMountainDetailCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(position: Int) {
            val item = courseList[position]
            binding.tvDifficulty.text = item.courseLevel
            binding.tvCourseName.text = item.courseName
            binding.tvDistance.text = "총" + item.courseLength + "km"
            binding.tvDifficulty.also {
                if (it.text == "어려움") {
                    it.setBackgroundResource(R.drawable.rounded_difficulty_hard_circle)
                } else if (it.text == "보통") {
                    it.setBackgroundResource(R.drawable.rounded_difficulty_medium_circle)
                } else if (it.text == "쉬움") {
                    it.setBackgroundResource(R.drawable.rounded_difficulty_easy_circle)
                }
            }
            binding.root.setOnClickListener { itemClickListener.onItemClick(item) }
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