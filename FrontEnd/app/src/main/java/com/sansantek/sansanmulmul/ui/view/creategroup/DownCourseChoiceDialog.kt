package com.sansantek.sansanmulmul.ui.view.creategroup

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.databinding.DialogGroupDowncourseChoiceBinding

class DownCourseChoiceDialog : DialogFragment() {

    private var _binding: DialogGroupDowncourseChoiceBinding? = null
    private val binding get() = _binding!!
    private var selectedLayout: View? = null
    private var selectedDownCourse: String? = null
    private var selectedUpCourse: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogGroupDowncourseChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 상행 코스 전달 받기
        selectedUpCourse = arguments?.getString("selectedUpCourse")

        // 다이얼로그 내용을 초기화하고 이벤트를 설정
        setupDialogContent()
    }

    private fun setupDialogContent() {
        // 닫기 버튼 이벤트 설정
        binding.ibCourseCancel.setOnClickListener {
            dismiss()
        }

        // 각 코스 클릭 이벤트 설정
        binding.layoutCourseDialogMid.setOnClickListener { selectCourse(it, binding.tvCourseInfoTitle) }
        binding.layoutCourseDialogMid1.setOnClickListener { selectCourse(it, binding.tvCourseInfoTitle1) }
        binding.layoutCourseDialogMid2.setOnClickListener { selectCourse(it, binding.tvCourseInfoTitle2) }
        binding.layoutCourseDialogMid3.setOnClickListener { selectCourse(it, binding.tvCourseInfoTitle3) }

        // '선택 완료' 버튼 클릭 이벤트 설정
        binding.btnNext.setOnClickListener {
            // 선택된 코스를 저장하고 다음 동작 수행
            if (selectedDownCourse != null) {
                dismiss()
                showGroupScheduleFragment(selectedUpCourse!!, selectedDownCourse!!)
            } else {
                // 선택된 코스가 없을 경우 처리
                // 예: 메시지 표시
            }
        }
    }

    private fun selectCourse(selectedView: View, courseTextView: TextView) {
        selectedDownCourse = courseTextView.text.toString()

        // 로그 출력
        Log.d("DownCourseChoiceDialog", "선택된 코스: ${selectedDownCourse}")

        // 이전 선택된 레이아웃의 선택 상태를 해제
        selectedLayout?.isSelected = false

        // 현재 선택된 레이아웃의 선택 상태를 설정
        selectedView.isSelected = true

        // 선택된 레이아웃 저장
        selectedLayout = selectedView
    }

    private fun resetCourseBackgrounds() {
        binding.layoutCourseDialogMid.setBackgroundResource(0)
        binding.layoutCourseDialogMid1.setBackgroundResource(0)
        binding.layoutCourseDialogMid2.setBackgroundResource(0)
        binding.layoutCourseDialogMid3.setBackgroundResource(0)
    }

    private fun showGroupScheduleFragment(upCourse: String, downCourse: String) {
        val fragment = GroupScheduleFragment.newInstance(upCourse, downCourse)
        parentFragmentManager.beginTransaction()
            .replace(R.id.layout_group_schedule, fragment)
            .commitAllowingStateLoss()
        Log.d("DownCourseDialog", "showGroupScheduleFragment: 레이아웃 변경 완료")
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): DownCourseChoiceDialog {
            val args = Bundle()
            val fragment = DownCourseChoiceDialog()
            fragment.arguments = args
            return fragment
        }
    }
}
