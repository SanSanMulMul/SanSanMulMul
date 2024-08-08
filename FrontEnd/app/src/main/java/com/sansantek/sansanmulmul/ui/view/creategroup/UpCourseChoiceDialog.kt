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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.CourseDetail
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainCourse
import com.sansantek.sansanmulmul.databinding.DialogGroupUpcourseChoiceBinding
import com.sansantek.sansanmulmul.ui.adapter.CreateGroupCourseSelectAdapter
import com.sansantek.sansanmulmul.ui.adapter.GroupCourceSearchListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.DividerItemDecorator
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.viewmodel.CreateGroupViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch

private const val TAG = "UpCourseChoiceDialog 싸피"
class UpCourseChoiceDialog(private val mountain: Mountain) : DialogFragment() {

    private var _binding: DialogGroupUpcourseChoiceBinding? = null
    private val binding get() = _binding!!
    private var selectedLayout: View? = null
    private var selectedUpCourse: String? = null
    private var courseList: MutableList<CourseDetail> = mutableListOf()
    private var adapter: CreateGroupCourseSelectAdapter = CreateGroupCourseSelectAdapter()
    private val activityViewModel: MainActivityViewModel by viewModels()
    private val viewModel: CreateGroupViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogGroupUpcourseChoiceBinding.inflate(inflater, container, false)

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
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
        binding.rvUpCourse.adapter = adapter.apply {
            setItemClickListener(object: CreateGroupCourseSelectAdapter.ItemClickListener{
                override fun onClick(courseDetail: CourseDetail) {
                    viewModel.setGroupUpCourseId(courseDetail.courseId)
                }
            })
        }
        binding.rvUpCourse.addItemDecoration(DividerItemDecorator(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!))
        binding.rvUpCourse.layoutManager = LinearLayoutManager(context)
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

        // '다음' 버튼 클릭 이벤트 설정
        binding.btnNext.setOnClickListener {
            // 선택된 코스를 저장하고 다음 동작 수행
            if (viewModel.groupUpCourseId != -1L) {
                dismiss()
                showDownCourseChoiceDialog()
            } else {
                // 선택된 코스가 없을 경우 처리
                // 예: 메시지 표시
            }
        }
    }

    private fun showDownCourseChoiceDialog() {
        val downCourseChoiceDialog = DownCourseChoiceDialog.newInstance()
        downCourseChoiceDialog.show(parentFragmentManager, "DownCourseChoiceDialog")
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
}
