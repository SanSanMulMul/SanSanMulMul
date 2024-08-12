package com.sansantek.sansanmulmul.ui.view.coursedetail

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PolylineOverlay
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.CourseDetail
import com.sansantek.sansanmulmul.databinding.FragmentCourseDetailBinding
import com.sansantek.sansanmulmul.ui.util.Util
import com.sansantek.sansanmulmul.ui.viewmodel.CourseDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainDetailViewModel

private const val TAG = "싸피_CourseDetailFragment"

class CourseDetailFragment : BaseFragment<FragmentCourseDetailBinding>(
    FragmentCourseDetailBinding::bind,
    R.layout.fragment_course_detail
), OnMapReadyCallback {
    private val mountainDetailViewModel: MountainDetailViewModel by activityViewModels()
    private val courseDetailViewModel: CourseDetailViewModel by activityViewModels()
    private lateinit var naverMap: NaverMap
    private lateinit var courseDetail: CourseDetail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init() {
        activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), true) }
        initCourseDetailData()
        initCourseMap()
    }

    private fun initCourseDetailData() {
        val mountainDetail = mountainDetailViewModel.mountainDetail.value
        if (mountainDetail != null) {
            binding.tvMountainName.text = mountainDetail.mountainName
        }

        courseDetailViewModel.courseID.observe(viewLifecycleOwner) { courseId ->
            if (courseId != null) {
                mountainDetail?.let { mountain ->
                    courseDetailViewModel.fetchCourseDetail(mountain.mountainId, listOf(courseId))
                }

                courseDetailViewModel.courseDetails.observe(viewLifecycleOwner) { courseDetails ->
                    val courseDetail = courseDetails.find { it.courseId == courseId }
                    if (courseDetail != null) {
                        updateView(courseDetail)
                        this.courseDetail = courseDetail
                    }
                }
            }
        }
    }

    private fun updateView(courseDetail: CourseDetail) {
//    Log.d(TAG, "init: $courseDetail")
        // 코스 이름
        binding.tvCourseName.text = courseDetail.courseName

        // 코스 난이도 정보
        binding.ivCourseDifficulty.also {
            it.text = when (courseDetail.courseLevel) {
                "HARD" -> {
                    it.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.difficulty_hard
                        )
                    )
                    "어려움"
                }

                "MEDIUM" -> {
                    it.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.difficulty_medium
                        )
                    )
                    "보통"
                }

                "EASY" -> {
                    it.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.difficulty_easy
                        )
                    )
                    "쉬움"
                }

                else -> "알 수 없음"
            }
        }

        // 총 소요시간
        binding.tvCourseTime.text =
            Util.formatMinutesToHoursAndMinutes(courseDetail.courseUptime + courseDetail.courseDowntime)

        // 코스길이
        binding.tvCourseLength.text = courseDetail.courseLength.toString() + " km"
    }

    private fun initCourseMap() {
        binding.courseDetailMap.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        drawPolyLineOnMap(courseDetail)
    }

    private fun drawPolyLineOnMap(course: CourseDetail) {
        val trackPaths =
            course.tracks[0].trackPaths.map { LatLng(it.trackPathLat, it.trackPathLon) }

        val polyline = PolylineOverlay().apply {
            map = null
            coords = trackPaths
            color = getPolyLineColor(course.courseLevel)
            width = 20
            zIndex = 1
        }
        polyline.map = naverMap

        if (trackPaths.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.Builder()
            polyline.coords.forEach { latLng -> boundsBuilder.include(latLng) }
            val latLngBounds = boundsBuilder.build()
            naverMap.moveCamera(CameraUpdate.fitBounds(latLngBounds, 100))
        }
    }

    private fun getPolyLineColor(difficulty: String): Int {
        return when (difficulty) {
            "EASY" -> resources.getColor(R.color.chip_course_difficulty_easy)
            "MEDIUM" -> resources.getColor(R.color.chip_course_difficulty_medium)
            "HARD" -> resources.getColor(R.color.chip_course_difficulty_hard)
            else -> {
                resources.getColor(R.color.grey)
            }
        }
    }
}