package com.sansantek.sansanmulmul.ui.view.creategroup

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.databinding.FragmentGroupCourseSearchBinding
import com.sansantek.sansanmulmul.ui.adapter.GroupCourceSearchListAdapter
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.register.GroupCreateViewPagerFragment
import com.sansantek.sansanmulmul.ui.viewmodel.CreateGroupViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch

private const val TAG = "GroupCourseSearchFragme 싸피"

class GroupCourseSearchFragment : BaseFragment<FragmentGroupCourseSearchBinding>(
    FragmentGroupCourseSearchBinding::bind,
    R.layout.fragment_group_course_search
) {
    // 예시 데이터 리스트
    private var dataList = listOf<Mountain>()
    private lateinit var adapter: GroupCourceSearchListAdapter
    private var isSearchViewClicked = false
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: CreateGroupViewModel by activityViewModels()
    private val viewPagerFragment by lazy {
        parentFragment as GroupCreateViewPagerFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchView()
        registerObserver()
        adapter = GroupCourceSearchListAdapter()
        binding.rvListSearchMountain.adapter = adapter.apply {
            setItemClickListener(object : GroupCourceSearchListAdapter.ItemClickListener {
                override fun onMountainClick(mountain: Mountain) {
                    viewModel.setGroupMountainId(mountain.mountainId)
                    showUpCourseChoiceDialog(mountain)
                }
                
                // 즐겨 찾기 로직
                override fun onLikeClick(mountain: Mountain, check: Boolean) {
                    activityViewModel.token?.let {
                        if (check) {
                            lifecycleScope.launch {
                                val result = mountainService.addLikeMountain(
                                    makeHeaderByAccessToken(it.accessToken),
                                    mountain.mountainId
                                )
                                if (result.isSuccessful) {
                                    if (result.body().equals("산 즐겨찾기 성공")) {
                                        showToast("즐겨찾기 등록 성공!")
                                    }
                                } else {
                                    showToast("즐겨찾기 실패!")
                                    Log.d(TAG, "onLikeClick: 즐겨찾기 등록 오류")
                                }
                            }
                        } else {
                            lifecycleScope.launch {
                                val result = mountainService.deleteLikeMountain(
                                    makeHeaderByAccessToken(it.accessToken),
                                    mountain.mountainId
                                )
                                if (result.isSuccessful) {
                                    if (result.body().equals("즐겨찾기 제거")) {
                                        showToast("즐겨찾기 제거 성공!")
                                    }
                                } else {
                                    showToast("즐겨찾기 실패!")
                                    Log.d(TAG, "onLikeClick: 즐겨찾기 제거 오류")
                                }
                            }
                        }
                    }
                }
            }
            )
        }
        binding.rvListSearchMountain.layoutManager = LinearLayoutManager(requireContext())

        // 데이터를 어댑터에 전달
        adapter.submitList(dataList)
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun showGroupScheduleFragment() {
        val fragment = GroupScheduleFragment.newInstance()
        parentFragmentManager.beginTransaction()
            .replace(R.id.vp_create_group, fragment)
            .commitAllowingStateLoss()
        Log.d("DownCourseDialog", "showGroupScheduleFragment: 레이아웃 변경 완료")
    }

    private fun registerObserver(){
        viewModel.groupUpCourseId.observe(viewLifecycleOwner){
            checkValid()
        }
        viewModel.groupDownCourseId.observe(viewLifecycleOwner){
            checkValid()
        }
    }

    private fun onDrawableRightClick() {
        // 드로어블 오른쪽 클릭 시 실행할 로직
        binding.svMountainSearchbar.setText("")
    }

    private fun initSearchView() {
        // SearchView 초기화
        val searchView = binding.svMountainSearchbar

        searchView.setOnClickListener {
            isSearchViewClicked = true
        }
        // 드로어블 클릭 이벤트 처리
        // 드로어블 클릭 이벤트 처리
        searchView.setOnTouchListener { v, event ->
            if (event.action == ACTION_UP) {
                val drawableRight = searchView.compoundDrawables[2]
                drawableRight?.let {
                    val drawableWidth = it.bounds.width()
                    if (event.rawX >= (searchView.right - drawableWidth)) {
                        onDrawableRightClick()
                        v.performClick() // 접근성 지원을 위한 호출
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(newText: Editable?) {
                dataList = mutableListOf()
                Log.d(TAG, "onTextChanged: $newText")
                if (newText.isNullOrEmpty()) {
                    // Show all if query is empty
                    adapter.submitList(dataList)
                } else {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val result = mountainService.searchMountainList(newText.toString())
                        if (result.isSuccessful) {
                            result.body()?.let {
                                dataList = it
                                adapter.submitList(dataList)
                            }
                        } else {
                            adapter.submitList(dataList)
                        }
                    }
                }
            }
        }
        )

    }

    private fun showUpCourseChoiceDialog(mountain: Mountain) {
        val dialog = UpCourseChoiceDialog(mountain)
        dialog.show(parentFragmentManager, "UpCourseChoiceDialog")
    }

    private fun checkValid() {
        if(viewModel.groupUpCourseId.value != -1L && viewModel.groupDownCourseId.value != -1L){
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.groupUpCourseId.value?.let {
                    val result = mountainService.getCourseDetail(viewModel.groupMountainId, it)
                    if(result.isSuccessful){
                        result.body()?.let { course ->
                            viewModel.setGroupUpCourseName(course.courseName)
                        }
                    }
                }
                viewModel.groupDownCourseId.value?.let {
                    val result = mountainService.getCourseDetail(viewModel.groupMountainId, it)
                    if(result.isSuccessful){
                        result.body()?.let { course ->
                            viewModel.setGroupDownCourseName(course.courseName)
                        }
                    }
                }
            }
            Log.d(TAG, "checkValid: 유효성 검사 통과")
            viewPagerFragment.enableNextButton(true)

        }
        else{
            Log.d(TAG, "checkValid: 유효성 검사 실패 : ${viewModel.groupUpCourseId.value}   ${viewModel.groupDownCourseId.value}")
            viewPagerFragment.enableNextButton(false)
        }
    }

    private fun init() {
        activity?.let {
            hideBottomNav(
                it.findViewById(R.id.main_layout_bottom_navigation),
                true
            )
        }
        viewModel.setGroupUpCourseId(-1)
        viewModel.setGroupDownCourseId(-1)
        checkValid()
    }

}

//course ->
//            showUpCourseChoiceDialog(course)