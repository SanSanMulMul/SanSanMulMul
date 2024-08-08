package com.sansantek.sansanmulmul.ui.view.mountaindetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PolylineOverlay
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.CourseDetail
import com.sansantek.sansanmulmul.databinding.FragmentMountainDetailTabSecondCourseBinding
import com.sansantek.sansanmulmul.ui.adapter.MountainDetailCourseListAdapter
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.viewmodel.CourseDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainDetailViewModel

private const val TAG = "싸피_MountainDetailTabSecond"

class MountainDetailTabSecondCourseFragment :
    BaseFragment<FragmentMountainDetailTabSecondCourseBinding>(
        FragmentMountainDetailTabSecondCourseBinding::bind,
        R.layout.fragment_mountain_detail_tab_second_course
    ), OnMapReadyCallback {
    private lateinit var rootActivity: MainActivity
    private lateinit var courseList: MutableList<CourseDetail>
    private val mountainDetailViewModel: MountainDetailViewModel by activityViewModels()
    private val courseDetailViewModel: CourseDetailViewModel by activityViewModels()
    private lateinit var courseListAdapter: MountainDetailCourseListAdapter
    private lateinit var naverMap: NaverMap
    private val filteringMap = mapOf(
        R.id.chip_easy to "EASY",
        R.id.chip_medium to "MEDIUM",
        R.id.chip_hard to "HARD",
        R.id.chip_all to "ALL"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initCourse()
        initCourseDifficultyChipGroup()
        initCourseMap()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            rootActivity = context
        }
    }

    private fun initCourse() {
        var mountainId = mountainDetailViewModel.mountainID.value
        courseList = mutableListOf()

        mountainDetailViewModel.mountainID.observe(viewLifecycleOwner) {
            mountainId = it
        }

        mountainDetailViewModel.mountainCourse.observe(viewLifecycleOwner) { mountainCourse ->
            if (mountainCourse != null) {
                mountainCourse.courseIds.forEach { courseId ->
                    mountainId?.let { courseDetailViewModel.fetchCourseDetail(it, courseId) }
                }
            } else {
                Log.d(TAG, "initCourseData: mountain course 비었음")
            }
        }

        courseDetailViewModel.courseDetail.observe(viewLifecycleOwner) { courseDetail ->
            if (courseDetail != null) {
                courseList.add(courseDetail)
                courseListAdapter.submitList(courseList.toList())
                Log.d(TAG, "initCourseData: $courseDetail")
            } else {
                Log.d(TAG, "initCourseData: course detail 없음")
            }
        }

        initCourseRecyclerView()
    }

    private fun initCourseRecyclerView() {
        val courseRecyclerView = binding.rvMountainDetailCourses

        courseListAdapter = MountainDetailCourseListAdapter(
            object : MountainDetailCourseListAdapter.OnItemClickListener {
                override fun onItemClick(course: CourseDetail) {
//                    requireActivity().supportFragmentManager.beginTransaction()
//                        .addToBackStack(null)
//                        .replace(R.id.fragment_view, CourseDetailFragment()).commit()
                    courseDetailViewModel.setCourseID(course.courseId)
                    updateMapWithCourse(course)
                }
            })

        courseRecyclerView.layoutManager = LinearLayoutManager(context)
        courseRecyclerView.adapter = courseListAdapter

        val dividerDrawable = activity?.getDrawable(R.drawable.recyclerview_divider_lightgray)
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        if (dividerDrawable != null) dividerItemDecoration.setDrawable(dividerDrawable)
        courseRecyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun updateMapWithCourse(course: CourseDetail) {
        val trackPaths =
            course.tracks[0].trackPaths.map { LatLng(it.trackPathLat, it.trackPathLon) }


        val polyline = PolylineOverlay().apply {
            map = null
            coords = trackPaths
            color = resources.getColor(R.color.group_detail_second_tab_temperature_min_color, null)
            width = 20
            zIndex = 1
        }
        polyline.map = naverMap

        if (trackPaths.isNotEmpty()) {
            val cameraUpdate = CameraUpdate.scrollTo(trackPaths[trackPaths.size / 2])
            naverMap.moveCamera(cameraUpdate)
        }
    }

    override fun onPause() {
        super.onPause()
        courseList.clear()
    }

    override fun onResume() {
        super.onResume()
        courseList.clear()
    }

    private fun initCourseMap() {
        binding.mountainDetailCourseMap.getMapAsync(this)
    }

    private fun initCourseDifficultyChipGroup() {
        binding.chipAll.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                unClickOtherChips(binding.cgCourseDifficulty)
            }
        }

        binding.chipEasy.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.chipAll.isChecked = false
            }
        }
        binding.chipMedium.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.chipAll.isChecked = false
            }
        }
        binding.chipHard.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.chipAll.isChecked = false
            }
        }
        val chipGroup = binding.cgCourseDifficulty
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                var selectedChips: List<String> =
                    checkedIds.map { id -> filteringMap[id] ?: id.toString() }
                if (selectedChips.contains("ALL")) {
                    selectedChips = listOf("ALL")
                }
//                Toast.makeText(context, "$selectedChips", Toast.LENGTH_SHORT).show()
                filterCourses(selectedChips)
            } else {
                filterCourses(listOf())
            }
        }
    }

    private fun filterCourses(difficultyList: List<String>) {
        val filteredList: MutableList<CourseDetail> = mutableListOf()
        if (difficultyList.isEmpty() || difficultyList.contains("ALL")) {
            courseListAdapter.submitList(courseList)
            return
        }

        difficultyList.forEach { difficulty ->
            val easyCourses = courseList.filter { it.courseLevel == difficulty }
            filteredList.addAll(easyCourses)
        }
        courseListAdapter.submitList(filteredList)
    }

    private fun unClickOtherChips(chipGroup: ChipGroup) {
        for (chipId in chipGroup.checkedChipIds) {
            val chip = chipGroup.findViewById<Chip>(chipId)
            if (chip.text != "전체") {
                Log.d(TAG, "unClickOtherChips: ${chip.text}")
                chip.isChecked = false
            }
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
    }

}