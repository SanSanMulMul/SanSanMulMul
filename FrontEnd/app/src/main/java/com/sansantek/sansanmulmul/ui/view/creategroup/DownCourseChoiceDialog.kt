package com.sansantek.sansanmulmul.ui.view.creategroup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.CourseDetail
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainCourse
import com.sansantek.sansanmulmul.databinding.DialogGroupDowncourseChoiceBinding
import com.sansantek.sansanmulmul.ui.adapter.CreateGroupCourseSelectAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.DividerItemDecorator
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.viewmodel.CreateGroupViewModel
import kotlinx.coroutines.launch

private const val TAG = "DownCourseChoiceDialog_싸피"
class DownCourseChoiceDialog() : DialogFragment() {

    private var _binding: DialogGroupDowncourseChoiceBinding? = null
    private val binding get() = _binding!!
    private var selectedLayout: View? = null
    private var selectedDownCourse: String? = null
    private var selectedUpCourse: String? = null
    private var courseList: MutableList<CourseDetail> = mutableListOf()
    private var adapter: CreateGroupCourseSelectAdapter = CreateGroupCourseSelectAdapter()
    private val viewModel: CreateGroupViewModel by activityViewModels()
    private var selectedCourseId: Long = -1
    private lateinit var mountain: Mountain

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
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        mountain = arguments?.getSerializable("mountain") as Mountain
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        // 다이얼로그 내용을 초기화하고 이벤트를 설정
        setupDialogContent()
    }

    private fun initAdapter(){
        binding.rvDownCourse.adapter = adapter.apply {
            setItemClickListener(object: CreateGroupCourseSelectAdapter.ItemClickListener{
                override fun onClick(courseDetail: CourseDetail) {
                    selectedCourseId = courseDetail.courseId
                }
            })
        }
        binding.rvDownCourse.addItemDecoration(DividerItemDecorator(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!))
        binding.rvDownCourse.layoutManager = LinearLayoutManager(context)
        lifecycleScope.launch {
            val course = loadCourse()
            course?.let {
                loadCourseDetail(it)
                Log.d(TAG, "initAdapter: $courseList")
                adapter.submitList(courseList)
                Log.d(TAG, "initAdapter: submitList 호출")
            }
        }
    }

    private suspend fun loadCourse(): MountainCourse?{

        val result = mountainService.getMountainCourse(mountain.mountainId)
        if(result.isSuccessful){
            result.body()?.let {
                return it
            }
        }
        else{
            Log.d(TAG, "loadCourse: course 가져오기 오류!")
        }
        return null
    }

    private suspend fun loadCourseDetail(course: MountainCourse?) {
        course?.let {
            it.courseIds.forEach {
                val result = mountainService.getCourseDetail(mountain.mountainId, it)
                if (result.isSuccessful) {
                    result.body()?.let {
                        courseList.add(it)
                    }
                } else {
                    Log.d(TAG, "loadCourseDetail: 코스 가져오기 에러!")
                }
            }
        }
    }

    private fun setupDialogContent() {
        // 닫기 버튼 이벤트 설정
        binding.ibCourseCancel.setOnClickListener {
            dismiss()
        }

        // '선택 완료' 버튼 클릭 이벤트 설정
        binding.btnNext.setOnClickListener {
            if(selectedCourseId!=-1L){
                viewModel.setGroupDownCourseId(selectedCourseId)
            }
            // 선택된 코스를 저장하고 다음 동작 수행
            if (viewModel.groupDownCourseId.value != -1L) {
                dismiss()
            } else {
                // 선택된 코스가 없을 경우 처리
                // 예: 메시지 표시
            }
        }
    }

    private fun showGroupScheduleFragment() {
        val fragment = GroupScheduleFragment.newInstance()
        parentFragmentManager.beginTransaction()
            .replace(R.id.layout_group_schedule, fragment)
            .commitAllowingStateLoss()
        Log.d("DownCourseDialog", "showGroupScheduleFragment: 레이아웃 변경 완료")
    }

    private fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    private fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.bottom - insets.top
        } else {
            val displayMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

    override fun onStart() {
        super.onStart()
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = getScreenWidth(this.requireContext())
        val screenHeight = getScreenHeight(this.requireContext())

        val newWidth = (screenWidth * 0.95).toInt()
        val newHeight = (screenHeight * 0.5).toInt()
        val layoutParams = requireView().layoutParams
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        requireView().layoutParams = layoutParams
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(mountain: Mountain): DownCourseChoiceDialog {
            val args = Bundle()
            args.putSerializable("mountain", mountain)
            val fragment = DownCourseChoiceDialog()
            fragment.arguments = args
            return fragment
        }
    }
}
