package com.sansantek.sansanmulmul.ui.view.creategroup

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupScheduleBinding
import com.sansantek.sansanmulmul.ui.view.register.GroupCreateViewPagerFragment
import com.sansantek.sansanmulmul.ui.viewmodel.CreateGroupViewModel
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "GroupScheduleFragment_싸피"

class GroupScheduleFragment : BaseFragment<FragmentGroupScheduleBinding>(
    FragmentGroupScheduleBinding::bind,
    R.layout.fragment_group_schedule
), DateTimePickerDialog.DateTimeDialogListener {
    private val viewPagerFragment by lazy {
        parentFragment as GroupCreateViewPagerFragment
    }
    private val viewModel: CreateGroupViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerObserver()
    }

    override fun onResume() {
        super.onResume()
        // 전달 받은 상행 코스와 하행 코스 설정
        init()
        val upCourse = viewModel.groupUpCourseName
        val downCourse = viewModel.groupDownCourseName
        Log.d(TAG, "onViewCreated: $upCourse  $downCourse")
        val upCourseText = "상행코스 | $upCourse"
        val downCourseText = "하행코스 | $downCourse"

        val greenColor = resources.getColor(R.color.green)
        val spanUpCourse = SpannableString(upCourseText).apply {
            setSpan(
                ForegroundColorSpan(greenColor),
                6,
                upCourseText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                AbsoluteSizeSpan(14, true),
                6,
                upCourseText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        val spanDownCourse = SpannableString(downCourseText).apply {
            setSpan(
                ForegroundColorSpan(greenColor),
                6,
                downCourseText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                AbsoluteSizeSpan(14, true),
                6,
                downCourseText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }



        binding.btnSelectScheduleStart.setOnClickListener {
            DateTimePickerDialog("start").apply {
                setDateTimeDialogListener(this@GroupScheduleFragment)
            }.show(parentFragmentManager, "date_dialog")
        }
        binding.btnSelectScheduleEnd.setOnClickListener {
            DateTimePickerDialog("end").apply {
                setDateTimeDialogListener(this@GroupScheduleFragment)
            }.show(parentFragmentManager, "date_dialog")
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

    override fun onDateTimeSelected(
        status: String,
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int
    ) {
        if (status == "start") {
            binding.btnSelectScheduleStart.text = "${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분"
            val dateTime = LocalDateTime.of(year, month, day, hour, minute)
            // ZonedDateTime으로 변환 (UTC)
            val zonedDateTime = ZonedDateTime.of(dateTime, ZoneOffset.UTC)
            // ISO 8601 형식으로 포맷
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val formatDate = zonedDateTime.format(formatter)
            viewModel.setStartDate(formatDate)
            Log.d(TAG, "onDateTimeSelected: ${binding.btnSelectScheduleEnd.text}")
        } else {
            binding.btnSelectScheduleEnd.text = "${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분"
            val dateTime = LocalDateTime.of(year, month, day, hour, minute)
            // ZonedDateTime으로 변환 (UTC)
            val zonedDateTime = ZonedDateTime.of(dateTime, ZoneOffset.UTC)
            // ISO 8601 형식으로 포맷
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val formatDate = zonedDateTime.format(formatter)
            viewModel.setEndDate(formatDate)
        }
    }

    private fun init() {
        viewModel.setStartDate("")
        viewModel.setEndDate("")
        if (!viewModel.startDate.value.isNullOrBlank()) {
            binding.btnSelectScheduleStart.text = viewModel.startDate.value
        }
        if (!viewModel.endDate.value.isNullOrBlank()) {
            binding.btnSelectScheduleEnd.text = viewModel.endDate.value
        }
        checkValid()
    }

    private fun registerObserver() {
        viewModel.startDate.observe(viewLifecycleOwner) {
            checkValid()
        }
        viewModel.endDate.observe(viewLifecycleOwner) {
            checkValid()
        }
    }

    private fun checkValid() {
        if (viewModel.startDate.value.isNullOrBlank() || viewModel.endDate.value.isNullOrBlank()) {
            viewPagerFragment.enableNextButton(false)
        } else {
            viewPagerFragment.enableNextButton(true)
        }
    }
}