package com.sansantek.sansanmulmul.ui.view.maptab

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.sansantek.sansanmulmul.databinding.ItemBottomSheetMountainBinding
import com.sansantek.sansanmulmul.ui.adapter.BottomSheetMountainListAdapter
import com.sansantek.sansanmulmul.ui.util.PermissionChecker
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.mountainService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.hometab.MountainSearchResultFragment
import com.sansantek.sansanmulmul.ui.view.mountaindetail.MountainDetailFragment
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainSearchViewModel
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
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val mountainDetailViewModel: MountainDetailViewModel by activityViewModels()
    private lateinit var searchEditTextView: EditText
    private val searchViewModel: MountainSearchViewModel by activityViewModels()
    private lateinit var itemBottomSheetMountainBinding: ItemBottomSheetMountainBinding

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
        searchMountain()

    }

    override fun onResume() {
        super.onResume()
        if (this::naverMap.isInitialized) {
            fetchMountainListAndUpdateLocation()
        }
    }

    private fun searchMountain() {
        searchEditTextView = binding.layoutSearchMountain.etSearchMountain

        // 검색 완료 시 프래그먼트 이동
        searchEditTextView.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val mountainSearchResultFragment = MountainSearchResultFragment()
                val searchKeyword = searchEditTextView.text.toString()
                searchViewModel.setSearchKeyword(searchKeyword)

                requireActivity().supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_view, mountainSearchResultFragment).commit()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun init() {
        activity?.let { hideBottomNav(it.findViewById(R.id.main_layout_bottom_navigation), false) }
        initBottomSheet()
        val dividerDrawable = activity?.getDrawable(R.drawable.recyclerview_divider_lightgray)
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        if (dividerDrawable != null) dividerItemDecoration.setDrawable(dividerDrawable)
        binding.rvBottomSheetMountain.addItemDecoration(dividerItemDecoration)
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
                    mountainDetailViewModel.setMountainID(mountain.mountainId)

                    requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.fragment_view, MountainDetailFragment()).commit()
                }
            }
        )

        mountainRecyclerView.layoutManager = LinearLayoutManager(context)
        mountainRecyclerView.adapter = mountainListAdapter



    }


    private fun initMountainData(nearbyMountains: List<Mountain>, mountainCourses: Map<Int, MountainCourse>): List<SearchMountainListItem> {
        return nearbyMountains.map { mountainDto ->
            val courseCount = mountainCourses[mountainDto.mountainId]?.courseCount ?: 0
            SearchMountainListItem(
                mountainId = mountainDto.mountainId,
                mountainImg = mountainDto.mountainImg,
                mountainName = mountainDto.mountainName,
                courseCnt = courseCount // 코스 수 추가
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
        if(isAdded){
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
    }

    private fun requestLocationUpdates() {
        locationSource.activate { provider ->
            activity?.let {
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
        Log.d(TAG, "showNearbyMountains: 메서드 호출됨")
        // Fragment의 뷰가 아직 존재하는지 확인
        if (view == null || !isAdded) {
            Log.e(TAG, "Fragment의 뷰가 존재하지 않거나 추가되지 않았습니다.")
            return
        }

        // mountainList가 초기화되었는지 확인
        if (!::mountainList.isInitialized || activity == null) {
            Log.d(TAG, "mountainList가 초기화되지 않았습니다.")
            fetchMountainListAndUpdateLocation()
        } else {
            Log.d(TAG, "mountainList : ${mountainList}")
            // 현재 위치를 가져옴
            Log.d(TAG, "showNearbyMountains: ${locationSource.lastLocation}")
            val currentLocation = locationSource.lastLocation ?: location
            
            val currentLat = currentLocation.latitude
            val currentLon = currentLocation.longitude

            // 50km 이내의 산들을 필터링
            val nearbyMountains = mountainList.filter { mountain ->
                val distance = calculateDistance(currentLat, currentLon, mountain.mountainLat, mountain.mountainLon)
                distance <= 50 // 50km 이내에 있는 산 필터링
            }
            Log.d(TAG, "필터 산 : ${nearbyMountains}")

            if (nearbyMountains.isEmpty()) {
                Log.d(TAG, "50km 이내에 산이 없습니다.")
                return
            }

            nearbyMountains.forEach { mountain ->
                Log.d(TAG, "마커 추가: ${mountain.mountainName} at (${mountain.mountainLat}, ${mountain.mountainLon})")
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
                    // 좋아요 버튼 클릭 시 호출될 리스너
                    itemBottomSheetMountainBinding.ibFavoriteBtn.setOnClickListener {
                        // 산 ID를 가져와서 좋아요 상태를 토글
                        val mountainId = mountain.mountainId
                        val isLiked = checkIfMountainLiked(mountainId) // 좋아요 상태 확인 메서드
                        onLikeClick(mountain, !isLiked)
                    }
                    true

                }
            }

            Log.d(TAG, "showNearbyMountains: $nearbyMountains")

            // 코스 수 받아오기
            val nearbyMountainIds = nearbyMountains.map { it.mountainId }
            viewLifecycleOwner.lifecycleScope.launch {
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
                safeCall{
                    initMountainListRecyclerView(mountains)
                }
            }

        }
    }

    private fun onLikeClick(mountain: Mountain, check: Boolean) {
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

    private fun checkIfMountainLiked(mountainId: Int): Boolean {
        // 로컬 데이터나 API를 통해 해당 산이 즐겨찾기에 등록되어 있는지 확인하는 로직 구현
        // 예를 들어, 로컬 DB나 SharedPreferences에서 확인할 수 있음
        return false // 예시로 false를 반환, 실제 구현 필요
    }

    private fun fetchMountainListAndUpdateLocation() {
        if(isAdded){
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
                // 필요한 경우 권한 요청 코드를 추가할 수 있음
            }
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