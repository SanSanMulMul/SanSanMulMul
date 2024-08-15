package com.sansantek.sansanmulmul.ui.view.mypagetab

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.overlay.PolylineOverlay
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.local.entity.LocationHistory
import com.sansantek.sansanmulmul.data.model.GroupUser
import com.sansantek.sansanmulmul.data.model.MountainHistory
import com.sansantek.sansanmulmul.data.model.Track
import com.sansantek.sansanmulmul.data.repository.LocationHistoryRepository
import com.sansantek.sansanmulmul.databinding.DialogMyPageHistoryBinding
import com.sansantek.sansanmulmul.ui.adapter.MyPageHistoryMemberListAdapter
import com.sansantek.sansanmulmul.ui.adapter.itemdecoration.SpaceItemDecoration
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.recordService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.view.groupdetail.GroupMemberDetailPageFragment
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter

private const val TAG = "ShowMyPageHistoryDialog_싸피"
class ShowMyPageHistoryDialog(private val mountainHistory: MountainHistory) : DialogFragment(), OnMapReadyCallback {
    // 뷰 바인딩 정의
    private var _binding: DialogMyPageHistoryBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val myPageHistoryMemberListAdapter: MyPageHistoryMemberListAdapter = MyPageHistoryMemberListAdapter()
    private lateinit var naverMap: NaverMap
    private val upCoursePolylines: MutableList<PolylineOverlay> = mutableListOf() // 현재 그려진 polyline들
    private val downCoursePolylines: MutableList<PolylineOverlay> = mutableListOf() // 현재 그려진 polyline들
    private var upCourseList: List<Track> = mutableListOf()
    private var downCourseList: List<Track> = mutableListOf()
    private val locationHistoryRepository by lazy {
        LocationHistoryRepository.get()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMyPageHistoryBinding.inflate(inflater, container, false)
        binding.ivGroupPreview.setColorFilter(
            Color.parseColor("#99000000"),
            PorterDuff.Mode.SRC_OVER
        )
        runBlocking {
            activityViewModel.token?.let {

                val response = recordService.getMountainDetailRecord(makeHeaderByAccessToken(it.accessToken), mountainHistory.recordId)
                if(response.isSuccessful){
                    response.body()?.let { result ->
                        upCourseList = result.upCourseTrackPaths
                        downCourseList = result.downCourseTrackPaths
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                        val recordStartDateTime = parse(result.recordStartTime, formatter)
                        val recordEndDateTime = parse(result.recordEndTime, formatter)
                        val outputFormatter = DateTimeFormatter.ofPattern("yy.MM.dd HH:mm")
                        val startFormattedDate = recordStartDateTime.format(outputFormatter).toString()
                        val endFormattedDate = recordEndDateTime.format(outputFormatter).toString()

                        Glide.with(binding.root).load(result.mountainImg).into(binding.ivGroupPreview)
                        binding.tvStartDate.text = startFormattedDate
                        binding.tvEndDate.text = endFormattedDate

                        binding.tvMountainName.text = mountainHistory.mountainName

                        binding.tvUpCourseName.text = result.upCourseName
                        binding.tvDownCourseName.text = result.downCourseName

                        val hour = result.recordDuration / 60
                        val min = result.recordDuration % 60


                        binding.tvHistoryInfo.text = if (result.recordDistance < 1000) {
                            "${result.recordDistance.toInt()}m 등산 완료!\n완등까지 약 ${hour}시간 ${min}분 걸렸습니다."
                        } else {
                            "${String.format("%.2f", (result.recordDistance / 1000))}km 등산 완료!\n완등까지 약 ${hour}시간 ${min}분 걸렸습니다."
                        }
                        binding.tvTotalWalkData.text = "${result.recordSteps}걸음"
                        binding.tvTotalCalorieData.text = if (result.recordKcal < 1000) {
                            "${result.recordKcal.toInt()} cal"
                        } else {
                            "${String.format("%.2f", (result.recordKcal / 1000))} kcal"
                        }
                        myPageHistoryMemberListAdapter.apply {
                            setItemClickListener(object: MyPageHistoryMemberListAdapter.ItemClickListener{
                                override fun onClick(groupUser: GroupUser) {
                                    val activity = requireActivity() as MainActivity
                                    activity.changeAddToBackstackFragment(GroupMemberDetailPageFragment.newInstance(groupUser.userId))
                                }
                            })
                            submitList(result.crewMembers)
                        }
                    }
                }

            }
        }
        binding.ibCloseBtn.setOnClickListener { dismiss() }

        val mapFragment = childFragmentManager.findFragmentById(R.id.navermap_map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.navermap_map_view, it).commit()
            }

        mapFragment.getMapAsync(this)

        binding.rvMemberList.apply {
            adapter = myPageHistoryMemberListAdapter.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                ).apply { isMeasurementCacheEnabled = false }
                addItemDecoration(SpaceItemDecoration(10))
            }
        }
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        // 닫기 버튼 클릭

        return view
    }

    override fun onMapReady(naverMap: NaverMap) {
        // 줌 버튼 비활성화
        val uiSettings: UiSettings = naverMap.uiSettings
        uiSettings.isZoomControlEnabled = false
        this.naverMap = naverMap
        naverMap.uiSettings.apply {
            isZoomControlEnabled = false
            isScrollGesturesEnabled = false
            isRotateGesturesEnabled = false
            isTiltGesturesEnabled =false
        }
        lifecycleScope.launch {
            val up = locationHistoryRepository.getLocationHistory(mountainHistory.crewId, "상행")
            val down = locationHistoryRepository.getLocationHistory(mountainHistory.crewId, "하행")
            Log.d(TAG, "onMapReady: 상행 리스트 $up")
            Log.d(TAG, "onMapReady: 하행 리스트 $down")
            launch {
                up?.let {
                    drawUpPolyLineOnMap(it, resources.getColor(R.color.chip_course_difficulty_easy))
                    //drawUpcoursePolyLineOnMap(upCourseList, resources.getColor(R.color.chip_course_difficulty_easy))
                }
            }
            launch {
                down?.let {
                    drawDownPolyLineOnMap(it, resources.getColor(R.color.chip_course_difficulty_medium))
                    //drawDowncoursePolyLineOnMap(downCourseList, resources.getColor(R.color.chip_course_difficulty_medium))
                }
            }
        }
        // 추가적으로 다른 설정을 할 수 있습니다.

    }

    private fun drawUpPolyLineOnMap(courses: List<LocationHistory>, id:Int) {
        upCoursePolylines.forEach { it.map = null }
        upCoursePolylines.clear()
        val boundsBuilder = LatLngBounds.Builder()

        if (courses.isEmpty()) return
        val path = courses.map { track ->
            LatLng(track.latitude, track.longitude)

        }
        val polyline = PolylineOverlay().apply {
            coords = path
            color = id
            width = 20
        }
        polyline.map = naverMap
        upCoursePolylines.add(polyline)
        path.forEach { latLng -> boundsBuilder.include(latLng) }
        val latLngBounds = boundsBuilder.build()
        naverMap.moveCamera(CameraUpdate.fitBounds(latLngBounds, 100))
    }

    private fun drawDownPolyLineOnMap(courses: List<LocationHistory>, id:Int) {
        downCoursePolylines.forEach { it.map = null }
        downCoursePolylines.clear()
        val boundsBuilder = LatLngBounds.Builder()

        if (courses.isEmpty()) return
        val path = courses.map { track ->
            LatLng(track.latitude, track.longitude)

        }
        val polyline = PolylineOverlay().apply {
            coords = path
            color = id
            width = 20
        }
        polyline.map = naverMap
        downCoursePolylines.add(polyline)
        path.forEach { latLng -> boundsBuilder.include(latLng) }
        val latLngBounds = boundsBuilder.build()
        naverMap.moveCamera(CameraUpdate.fitBounds(latLngBounds, 100))
    }

    private fun drawUpcoursePolyLineOnMap(courses: List<Track>, id:Int) {
        upCoursePolylines.forEach { it.map = null }
        upCoursePolylines.clear()
        val boundsBuilder = LatLngBounds.Builder()

        if (courses.isEmpty()) return
        courses.forEach { track ->
            val path =
                track.trackPaths.map { LatLng(it.trackPathLat, it.trackPathLon) }
            val polyline = PolylineOverlay().apply {
                coords = path
                color = id
                width = 20
            }
            polyline.map = naverMap
            upCoursePolylines.add(polyline)
            path.forEach { latLng -> boundsBuilder.include(latLng) }
        }
    }

    private fun drawDowncoursePolyLineOnMap(courses: List<Track>, id:Int) {
        downCoursePolylines.forEach { it.map = null }
        downCoursePolylines.clear()
        val boundsBuilder = LatLngBounds.Builder()

        if (courses.isEmpty()) return
        courses.forEach { track ->
            val path =
                track.trackPaths.map { LatLng(it.trackPathLat, it.trackPathLon) }
            val polyline = PolylineOverlay().apply {
                coords = path
                color = id
                width = 20
            }
            polyline.map = naverMap
            downCoursePolylines.add(polyline)
            path.forEach { latLng -> boundsBuilder.include(latLng) }
        }

        val latLngBounds = boundsBuilder.build()
        naverMap.moveCamera(CameraUpdate.fitBounds(latLngBounds, 100))
    }

    override fun onStart() {
        super.onStart()
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = getScreenWidth(this.requireContext())
        val screenHeight = getScreenHeight(this.requireContext())

        val newWidth = (screenWidth * 0.8).toInt()
        val newHeight = (screenHeight * 0.7).toInt()
        val layoutParams = requireView().layoutParams
        layoutParams.width = newWidth
        layoutParams.height = newHeight
        requireView().layoutParams = layoutParams
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}