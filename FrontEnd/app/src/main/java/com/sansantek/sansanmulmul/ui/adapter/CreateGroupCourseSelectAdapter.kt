package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.CourseDetail
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.databinding.ListGroupBinding
import com.sansantek.sansanmulmul.databinding.ListGroupCreateCourseBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "CreateGroupCourseSelect 싸피"
class CreateGroupCourseSelectAdapter():
    ListAdapter<CourseDetail, CreateGroupCourseSelectAdapter.CourseInfoHolder>(Comparator) {
    private lateinit var selectedView: View
    companion object Comparator : DiffUtil.ItemCallback<CourseDetail>() {
        override fun areItemsTheSame(oldItem: CourseDetail, newItem: CourseDetail): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: CourseDetail, newItem: CourseDetail): Boolean {
            return oldItem == newItem
        }
    }

    inner class CourseInfoHolder(private val binding: ListGroupCreateCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            binding.tvCourseInfoTitle.text = item.courseName
            binding.tvEasyCircle.text = item.courseLevel
            binding.tvCourseInfoDistance.text = "${item.courseLength}km"
            Log.d(TAG, "bindInfo: binding")
            when(item.courseLevel){
                "EASY" -> {
                    binding.tvEasyCircle.apply {
                        text = "쉬움"
                        background = ContextCompat.getDrawable(binding.root.context, R.drawable.circle_easy_green)
                    }
                }
                "MEDIUM" -> {
                    binding.tvEasyCircle.apply {
                        text = "보통"
                        background = ContextCompat.getDrawable(binding.root.context, R.drawable.circle_middle_yellow)
                    }
                }
                "HARD" -> {
                    binding.tvEasyCircle.apply {
                        text = "어려움"
                        background = ContextCompat.getDrawable(binding.root.context, R.drawable.circle_hard_red)
                    }
                }
            }
            binding.root.setOnClickListener {
                itemClickListener.onClick(item)
                if(::selectedView.isInitialized){
                    selectedView.isSelected = false
                }
                selectedView = binding.root
                binding.root.isSelected = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseInfoHolder {
        return CourseInfoHolder(
            ListGroupCreateCourseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CourseInfoHolder, position: Int) {
        holder.bindInfo(position)
    }


    interface ItemClickListener {
        fun onClick(courseDetail: CourseDetail)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}