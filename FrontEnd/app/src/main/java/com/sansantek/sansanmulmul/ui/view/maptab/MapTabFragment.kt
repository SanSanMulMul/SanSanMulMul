package com.sansantek.sansanmulmul.ui.view.maptab

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.CameraUpdate.REASON_GESTURE
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.UiSettings
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.CameraLocation
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
import com.sansantek.sansanmulmul.ui.viewmodel.MapTapViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainDetailViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MountainSearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val mapTapViewModel: MapTapViewModel by viewModels()
    private val mountainDetailViewModel: MountainDetailViewModel by activityViewModels()
    private lateinit var searchEditTextView: EditText
    private val searchViewModel: MountainSearchViewModel by activityViewModels()
    private lateinit var itemBottomSheetMountainBinding: ItemBottomSheetMountainBinding

    // 권한 코드
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    // 권한 받을 리스트
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        // 일단 서울 화면 나옴

        val mapFragment = childFragmentManager.findFragmentById(R.id.maptab_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.maptab_map, it).commit()
            }

        mapFragment.getMapAsync(this)

        mapTapViewModel.isTracking.observe(viewLifecycleOwner){
            if(!::naverMap.isInitialized){}
            else if(it){
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
                binding.btnTraking.imageTintList = ContextCompat.getColorStateList(myContext, R.color.sansanmulmul_green)
            }else{
                naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
                binding.btnTraking.imageTintList = ContextCompat.getColorStateList(myContext, R.color.grey)
            }
        }

        binding.btnTraking.setOnClickListener {
            if(mapTapViewModel.isTracking.value!!){
                mapTapViewModel.setTraking(false)
            }else{
                mapTapViewModel.setTraking(true)
            }
        }

        searchMountain()
    }

    private fun fetchMountainsWithinRadius(lat: Double, lon: Double) {
        lifecycleScope.launch {
            try {
                // 반경 50km 내의 산 목록 가져오기
                val mountains = mountainService.getMountainWithInRadius(CameraLocation(lat, lon,50.toDouble()))
                // 코스 정보 가져오기
                val mountainCourseMap = mutableMapOf<Int, MountainCourse>()
                mountains.forEach { mountain ->

                        val response = mountainService.getMountainCourse(mountain.mountainId)
                        if (response.isSuccessful) {
                            val mountainCourseInfo = response.body()
                            if (mountainCourseInfo != null) {
                                mountainCourseMap[mountain.mountainId] = mountainCourseInfo
                            }
                        }

                }


                // initMountainData 함수를 사용해 코스 정보를 포함한 산 목록 생성
                lifecycleScope.launch {
                    val searchMountainListItems = initMountainData(mountains, mountainCourseMap)
                    Log.d(TAG, "fetchMountainsWithinRadius: $searchMountainListItems")

                    // 리사이클러뷰에 데이터 설정
                    initMountainListRecyclerView(searchMountainListItems)
                }
                mapTapViewModel.markerList.forEach {
                    it.map = null
                }
                mapTapViewModel.clearMarkerList()
                mountains.forEach {
                    val marker = Marker().apply {
                        position = LatLng(it.mountainLat, it.mountainLon)
                        map = naverMap
                        tag = it.mountainName
                        setOnClickListener { overlay ->
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
                    mapTapViewModel.addMarkerList(marker)
                }

            } catch (e: Exception) {
                Log.e(TAG, "fetchMountainsWithinRadius: API 호출 중 오류 발생", e)
            }
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
        val mountainListAdapter = BottomSheetMountainListAdapter().apply {
            submitList(mountainList)
            setOnItemClickListener(object : BottomSheetMountainListAdapter.ItemClickListener {
                override fun onItemClick(mountain: SearchMountainListItem) {
                    mountainDetailViewModel.setMountainID(mountain.mountainId)
                    requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.fragment_view, MountainDetailFragment()).commit()
                }

                override fun onLikeClick(mountain: SearchMountainListItem, check: Boolean) {
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
            })
        }

        mountainRecyclerView.layoutManager = LinearLayoutManager(context)
        mountainRecyclerView.adapter = mountainListAdapter


    }


    private fun initMountainData(
        nearbyMountains: List<Mountain>,
        mountainCourses: Map<Int, MountainCourse>
    ): List<SearchMountainListItem> {
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
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource
    }


    override fun onMapReady(naverMap: NaverMap) {
        Log.d(TAG, "onMapReady: 실행 중")
        this.naverMap = naverMap.apply {
            uiSettings.logoGravity = (Gravity.TOP)
            uiSettings.isLocationButtonEnabled = false
            // 기본 카메라 위치 설정
            moveCamera(CameraUpdate.zoomTo(10.0))
            addOnCameraChangeListener { reason, animated ->
                if (reason == REASON_GESTURE) {
                    mapTapViewModel.setTraking(false)
                }
            }

            addOnLocationChangeListener { location ->
                if(mapTapViewModel.isTracking.value!!){
                    fetchMountainsWithinRadius(location.latitude, location.longitude)
                }
            }

            addOnCameraIdleListener {
                // 카메라 이동이 완료되면 중앙 좌표를 얻음
                val cameraPosition = naverMap.cameraPosition
                val centerLatLng = cameraPosition.target
                // 중앙 좌표를 사용하여 50km 반경 내의 산을 API로 요청
                if(!mapTapViewModel.isTracking.value!!) {
                    fetchMountainsWithinRadius(centerLatLng.latitude, centerLatLng.longitude)
                }
            }
        }
        if (isAdded) {
            // 카메라 움직임 감지 리스너 설정
            initLocationSource()
            mapTapViewModel.setTraking(true)
            Log.d(TAG, "onMapReady: 마지막 위치 ${locationSource.lastLocation}")

            // 줌 버튼 비활성화
            val uiSettings: UiSettings = naverMap.uiSettings
            uiSettings.isZoomControlEnabled = false
        }
    }

}