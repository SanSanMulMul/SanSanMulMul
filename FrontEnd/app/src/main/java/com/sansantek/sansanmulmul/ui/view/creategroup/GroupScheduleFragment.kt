package com.sansantek.sansanmulmul.ui.view.creategroup

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupHikingStyleBinding
import com.sansantek.sansanmulmul.databinding.FragmentGroupScheduleBinding
import com.sansantek.sansanmulmul.ui.viewmodel.CreateGroupViewModel

private const val TAG = "GroupScheduleFragment_싸피"
class GroupScheduleFragment : BaseFragment<FragmentGroupScheduleBinding>(
    FragmentGroupScheduleBinding::bind,
    R.layout.fragment_group_schedule
) {

    private val viewModel : CreateGroupViewModel by activityViewModels()
    override fun onResume() {
        super.onResume()
        // 전달 받은 상행 코스와 하행 코스 설정
        val upCourse = viewModel.groupUpCourseName
        val downCourse = viewModel.groupDownCourseName
        Log.d(TAG, "onViewCreated: $upCourse  $downCourse")
        val upCourseText = "상행코스 | $upCourse"
        val downCourseText = "하행코스 | $downCourse"

        val greenColor = resources.getColor(R.color.green)
        val spanUpCourse = SpannableString(upCourseText).apply {
            setSpan(ForegroundColorSpan(greenColor), 6, upCourseText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(AbsoluteSizeSpan(14, true), 6, upCourseText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        val spanDownCourse = SpannableString(downCourseText).apply {
            setSpan(ForegroundColorSpan(greenColor), 6, downCourseText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(AbsoluteSizeSpan(14, true), 6, downCourseText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        binding.tvLastUpCourse.text = spanUpCourse
        binding.tvLastDownCourse.text = spanDownCourse
    }

    companion object {
        fun newInstance(): GroupScheduleFragment {
            val fragment = GroupScheduleFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}