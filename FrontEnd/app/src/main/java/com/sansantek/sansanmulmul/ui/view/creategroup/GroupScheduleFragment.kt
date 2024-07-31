package com.sansantek.sansanmulmul.ui.view.creategroup

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.databinding.FragmentGroupScheduleBinding

class GroupScheduleFragment : Fragment() {

    private var _binding: FragmentGroupScheduleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달 받은 상행 코스와 하행 코스 설정
        val upCourse = arguments?.getString("upCourse")
        val downCourse = arguments?.getString("downCourse")

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(upCourse: String, downCourse: String): GroupScheduleFragment {
            val fragment = GroupScheduleFragment()
            val args = Bundle()
            args.putString("upCourse", upCourse)
            args.putString("downCourse", downCourse)
            fragment.arguments = args
            return fragment
        }
    }
}