package com.sansantek.sansanmulmul.ui.view.maptab

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.sansantek.sansanmulmul.R
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.util.FusedLocationSource
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.databinding.FragmentMapTabBinding
import com.sansantek.sansanmulmul.ui.adapter.BottomSheetMountainListAdapter
import com.sansantek.sansanmulmul.ui.util.PermissionChecker
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.data.model.MountainDto
import com.sansantek.sansanmulmul.ui.view.mountaindetail.MountainDetailFragment
import kotlinx.coroutines.launch


private const val TAG = "맵 테스트 싸피"


class MapTabFragment : BaseFragment<FragmentMapTabBinding>(
    FragmentMapTabBinding::bind,
    R.layout.fragment_map_tab

), OnMapReadyCallback {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mountainList: List<MountainDto>

    // 권한 코드
    private val LOCATION_PERMISSION_REQUEST_CODE = 5000

    // 권한 받을 리스트
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 일단 서울 화면 나옴
        binding.maptabMap.getMapAsync(this)


        lifecycleScope.launch {
            mountainList = mountainService.getMountainList()
            mountainList.forEach { mountain ->
                val id = mountain.mountainId
                val latitude = mountain.mountainLat
                val longitude = mountain.mountainLon
                Log.d(TAG, "산 ID: $id, 위도: $latitude, 경도: $longitude")
            }

        }

        init()

    }

    private fun init() {
        activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), false) }
        initBottomSheet()
        requestPermissions()
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(createBottomSheetCallback())
        initMountainListRecyclerView()
    }

    private fun initMountainListRecyclerView() {
        val mountainList = initMountainData()
        val mountainRecyclerView = binding.rvBottomSheetMountain
        val mountainListAdapter = BottomSheetMountainListAdapter(
            mountainList,
            object : BottomSheetMountainListAdapter.OnItemClickListener {
                override fun onItemClick(mountain: Mountain) {
                    requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.fragment_view, MountainDetailFragment()).commit()
                }
            }
        )

        mountainRecyclerView.layoutManager = LinearLayoutManager(context)
        mountainRecyclerView.adapter = mountainListAdapter

        val dividerDrawable = activity?.getDrawable(R.drawable.recyclerview_divider_lightgray)
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        if (dividerDrawable != null) dividerItemDecoration.setDrawable(dividerDrawable)
        mountainRecyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun initMountainData(): List<Mountain> {
        return listOf(
            Mountain(R.drawable.dummy1, "가야산", 6),
            Mountain(R.drawable.dummy2, "가리산", 3),
            Mountain(R.drawable.dummy3, "가리왕산", 2),
            Mountain(R.drawable.dummy3, "가리왕산", 2),
            Mountain(R.drawable.dummy3, "가리왕산", 2),
            Mountain(R.drawable.dummy3, "가리왕산", 2),
            Mountain(R.drawable.dummy3, "가리왕산", 2)
        )
    }

    private fun createBottomSheetCallback(): BottomSheetCallback {
        return object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {}
                    BottomSheetBehavior.STATE_EXPANDED -> {}
                    BottomSheetBehavior.STATE_COLLAPSED -> {}
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {}
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        }
    }

    // 권한 받는 함수
    private fun requestPermissions() {
        val checker = PermissionChecker(this)
        if (!checker.checkPermission(requireContext(), PERMISSIONS)) {
            checker.setOnGrantedListener {
                if (this::naverMap.isInitialized) {
                    initLocationSource()
                    Log.d(TAG, "권한 승인")
                }
            }
            checker.requestPermissionLauncher.launch(PERMISSIONS)
            Log.d(TAG, "권한 실패, 권한 요청 중")

        } else {
            if (this::naverMap.isInitialized) {
                initLocationSource()
                Log.d(TAG, "권한 화면 나옴")
            }
        }
    }

    // 사용자 위치
    private fun initLocationSource() {
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        initLocationSource()

        // 위치 추적 모드 설정
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        // 위치 버튼 활성화
        naverMap.uiSettings.isLocationButtonEnabled = true

        // 줌 버튼 비활성화
        val uiSettings: UiSettings = naverMap.uiSettings
        uiSettings.isZoomControlEnabled = false

        // 기본 카메라 위치 설정
        naverMap.moveCamera(CameraUpdate.zoomTo(10.0))
    }
}
