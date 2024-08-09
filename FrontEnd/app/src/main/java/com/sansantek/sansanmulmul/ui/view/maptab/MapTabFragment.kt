package com.sansantek.sansanmulmul.ui.view.maptab

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainCourse
import com.sansantek.sansanmulmul.data.model.SearchMountainListItem
import com.sansantek.sansanmulmul.databinding.FragmentMapTabBinding
import com.sansantek.sansanmulmul.ui.adapter.BottomSheetMountainListAdapter
import com.sansantek.sansanmulmul.ui.util.PermissionChecker
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.view.mountaindetail.MountainDetailFragment
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt


private const val TAG = "맵 테스트 싸피"


class MapTabFragment : BaseFragment<FragmentMapTabBinding>(
    FragmentMapTabBinding::bind,
    R.layout.fragment_map_tab

), OnMapReadyCallback {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var mountainList: List<Mountain>
    private lateinit var mountainCourseInfo: List<MountainCourse>

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
            // 산 ID, 위도 경도 추출
            mountainList.forEach { mountain ->
                val id = mountain.mountainId
                val latitude = mountain.mountainLat
                val longitude = mountain.mountainLon
                Log.d(TAG, "산 ID: $id, 위도: $latitude, 경도: $longitude")
            }

        }
        init()
//        onMapReady(naverMap)

    }

    override fun onResume() {
        super.onResume()
        if (this::naverMap.isInitialized) {
            fetchMountainListAndUpdateLocation()
        }
    }

    private fun init() {
        activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), false) }
        initBottomSheet()
        requestPermissions()
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(createBottomSheetCallback())
    }

    private fun initMountainListRecyclerView(mountainList: List<SearchMountainListItem>) {
        val mountainRecyclerView = binding.rvBottomSheetMountain
        val mountainListAdapter = BottomSheetMountainListAdapter(
            mountainList,
            object : BottomSheetMountainListAdapter.OnItemClickListener {
                override fun onItemClick(mountain: SearchMountainListItem) {
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


    private fun initMountainData(nearbyMountains: List<Mountain>, mountainCourses: Map<Int, MountainCourse>): List<SearchMountainListItem> {
        return nearbyMountains.map { mountainDto ->
            val courseCount = mountainCourses[mountainDto.mountainId]?.courseCount ?: 0
            SearchMountainListItem(
                mountainDto.mountainImg,
                mountainDto.mountainName,
                courseCount // 코스 수 추가
            )
        }
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
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource

        // 위치 정보 업데이트 요청
        requestLocationUpdates()
    }

    override fun onMapReady(naverMap: NaverMap) {
        Log.d(TAG, "onMapReady: 실행 중")
        this.naverMap = naverMap
        initLocationSource()
        Log.d(TAG, "onMapReady: 마지막 위치 ${locationSource.lastLocation}")

        // 위치 추적 모드 설정
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        // 위치 버튼 활성화
        naverMap.uiSettings.isLocationButtonEnabled = true

        // 줌 버튼 비활성화
        val uiSettings: UiSettings = naverMap.uiSettings
        uiSettings.isZoomControlEnabled = false

        // 기본 카메라 위치 설정
        naverMap.moveCamera(CameraUpdate.zoomTo(10.0))

        requestLocationUpdates()
    }

    private fun requestLocationUpdates() {
        locationSource.activate { provider ->
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return@activate
            }
            locationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    updateLocation(location)
                } else {
                    Log.d(TAG, "requestLocationUpdates: 위치 정보를 가져올 수 없습니다.")
                }
            }
        }
    }

    private fun updateLocation(location: Location) {
        Log.d(TAG, "updateLocation: 위치 업데이트 ${location.latitude}, ${location.longitude}")
        moveCameraToLocation(location)
        showNearbyMountains(location)
    }

    private fun moveCameraToLocation(location: Location) {
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(location.latitude, location.longitude)).animate(
            CameraAnimation.Fly, 1000)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun showNearbyMountains(location: Location) {
        // mountainList가 초기화되었는지 확인
        if (!::mountainList.isInitialized) {
            Log.e(TAG, "mountainList가 초기화되지 않았습니다.")
        } else {
            fetchMountainListAndUpdateLocation()
        }

        // 현재 위치를 가져옴
        val currentLocation = locationSource.lastLocation ?: return

        val currentLat = currentLocation.latitude
        val currentLon = currentLocation.longitude

        // 50km 이내의 산들을 필터링
        val nearbyMountains = mountainList.filter { mountain ->
            val distance = calculateDistance(currentLat, currentLon, mountain.mountainLat, mountain.mountainLon)
            distance <= 50 // 50km 이내에 있는 산 필터링
        }

        // 마운틴 리스트가 초기화되었는지 확인하고 초기화되었으면 아래 로직을 실행
        if (::mountainList.isInitialized) {
            nearbyMountains.forEach { mountain ->
                val marker = Marker().apply {
                    position = LatLng(mountain.mountainLat, mountain.mountainLon)
                    map = naverMap
                    tag = mountain.mountainName
                }

                // 마커 클릭 리스너 설정
                marker.setOnClickListener { overlay ->
                    // Fragment가 Context에 연결되었는지 확인
                    if (!isAdded || context == null) {
                        Log.e(TAG, "Fragment가 Context에 연결되어 있지 않습니다.")
                        return@setOnClickListener true
                    }

                    val marker = overlay as Marker
                    val infoWindow = InfoWindow().apply {
                        adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
                            override fun getText(infoWindow: InfoWindow): CharSequence {
                                return marker.tag as CharSequence
                            }
                        }
                    }

                    // 이미 열려있는 InfoWindow가 있는 경우 닫음
                    if (marker.infoWindow != null) {
                        marker.infoWindow?.close()
                    } else {
                        infoWindow.open(marker)
                    }
                    true
                }
            }
            Log.d(TAG, "showNearbyMountains: $nearbyMountains")

            // 코스 수 받아오기
            val nearbyMountainIds = nearbyMountains.map { it.mountainId }
            lifecycleScope.launch {
                val mountainCourse = mutableMapOf<Int, MountainCourse>()
                nearbyMountainIds.forEach { mountainId ->
                    try {
                        val response = mountainService.getMountainCourse(mountainId)
                        if (response.isSuccessful) {
                            val mountainCourseInfo = response.body()
                            if (mountainCourseInfo != null) {
                                mountainCourse[mountainId] = mountainCourseInfo
                                Log.d(TAG, " $mountainId: $mountainCourseInfo")
                            } else {
                                Log.e(TAG, " $mountainId")
                            }
                        } else {
                            Log.e(TAG, " $mountainId: ${response.errorBody()}")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, " $mountainId: ${e.message}")
                        e.printStackTrace()
                    }
                }
                val mountains = initMountainData(nearbyMountains, mountainCourse)
                initMountainListRecyclerView(mountains)
            }
        } else {
            Log.e(TAG, "Mountain list is not initialized")
        }
    }

    private fun fetchMountainListAndUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            lifecycleScope.launch {
                try {
                    // 산 리스트를 가져와서 초기화
                    mountainList = mountainService.getMountainList()

                    // 위치 정보를 가져와서 업데이트
                    locationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            updateLocation(location)
                        } else {
                            Log.d(TAG, "fetchMountainListAndUpdateLocation: 위치 정보를 가져올 수 없습니다.")
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "fetchMountainListAndUpdateLocation: 산 리스트를 가져오는 중 오류 발생", e)
                }
            }
        } else {
            Log.e(TAG, "fetchMountainListAndUpdateLocation: 위치 권한이 없습니다.")
            // 필요한 경우 권한 요청 코드를 추가할 수 있습니다.
        }
    }


    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val radius = 6371 // 지구 반지름 (단위: km)
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2).pow(2.0) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2).pow(2.0)
        val c = 2 * Math.atan2(sqrt(a), sqrt(1 - a))
        return radius * c
    }
}
