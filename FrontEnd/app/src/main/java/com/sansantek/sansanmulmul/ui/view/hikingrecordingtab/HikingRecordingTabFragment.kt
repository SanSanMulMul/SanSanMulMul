package com.sansantek.sansanmulmul.ui.view.hikingrecordingtab

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.config.Const.Companion.AFTER_HIKING
import com.sansantek.sansanmulmul.config.Const.Companion.BANNED
import com.sansantek.sansanmulmul.config.Const.Companion.BEFORE_HIKING
import com.sansantek.sansanmulmul.config.Const.Companion.HIKING
import com.sansantek.sansanmulmul.data.local.entity.StepCount
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.databinding.FragmentHikingRecordingTabBinding
import com.sansantek.sansanmulmul.ui.service.HikingRecordingService
import com.sansantek.sansanmulmul.ui.util.PermissionChecker
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.crewService
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.hikingRecordingService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.viewmodel.ChronometerViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.HikingRecordingTabViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private const val TAG = "HikingRecordingTabFragment_싸피"

class HikingRecordingTabFragment : BaseFragment<FragmentHikingRecordingTabBinding>(
  FragmentHikingRecordingTabBinding::bind,
  R.layout.fragment_hiking_recording_tab
), OnMapReadyCallback {
  private lateinit var permissionChecker: PermissionChecker
  private var isHikingInfoViewExpanded = false
  private lateinit var rootActivity: MainActivity
  private val activityViewModel: MainActivityViewModel by activityViewModels()
  private var currentCallback: ActivityResultCallback<Map<String, Boolean>>? = null
  private val hikingRecordingTabViewModel: HikingRecordingTabViewModel by activityViewModels()
  private val chronometerViewModel: ChronometerViewModel by viewModels()
  private val PERMISSION = if (Build.VERSION.SDK_INT >= 33) {
    arrayOf(
        Manifest.permission.CAMERA,
      Manifest.permission.ACTIVITY_RECOGNITION,
      Manifest.permission.POST_NOTIFICATIONS,
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_COARSE_LOCATION
    )
  } else if (Build.VERSION.SDK_INT >= 29) {
    arrayOf(
        Manifest.permission.CAMERA,
      Manifest.permission.ACTIVITY_RECOGNITION,
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_COARSE_LOCATION
    )
  } else {
    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
  }
  private val requestPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) {
    Log.d(TAG, "requestPermissionLauncher: 건수 : ${it.size}")
    currentCallback?.onActivityResult(it)
  }
  private val mMessageReceiver = object : BroadcastReceiver() {
    override fun onReceive(p0: Context, intent: Intent) {
      val message = intent.getSerializableExtra("value") as StepCount
      Log.d(TAG, "Got message: " + message)
      safeCall {

        binding.tvStepCnt.text = message.stepCount.toString()

        val distance = (0.74 * message.stepCount)
        binding.tvDistance.text = if (distance < 1000) {
          "${distance.toInt()} m"
        } else {
          "${String.format("%.2f", (distance / 1000))} km"
        }

        binding.tvHeight.text = if (message.elevation < 1000) {
          if (message.elevation != -1.0) {
            "${message.elevation.toInt()} m"
          } else {
            "0 km"
          }
        } else {
          "${message.elevation.toInt() / 1000} km"
        }

        val kcal = (46.62 * message.stepCount)
        binding.tvCalorie.text = if (kcal < 1000) {
          "${kcal.toInt()} cal"
        } else {
          "${String.format("%.2f", (kcal / 1000))} kcal"
        }
      }
    }
  }


  override fun onAttach(context: Context) {
    super.onAttach(context)
    if (context is MainActivity) {
      rootActivity = context
    }
  }

  override fun onResume() {
    super.onResume()
    // 지금 기록 중이었는지 확인하고 상태에 따라 버튼 초기화
    Log.d(TAG, "onViewCreated: resume 시작")
    syncButtonStatus()
    initClickListener()
    Log.d(TAG, "onViewCreated: resume 종료")
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    init()
    Log.d(TAG, "onViewCreated: init 종료")
  }

    private fun init() {
        setInitialView()
        setPermissionChecker()
        registerObserving()
        registerLocalBroadCastReceiver()
        hideBottomNav(rootActivity.findViewById(R.id.main_layout_bottom_navigation), false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.hiking_recording_tab_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.hiking_recording_tab_map, it).commit()
            }

        mapFragment.getMapAsync(this)
    }

  private fun setInitialView() {
    loadMyScheduledGroupListAndUpdateUI()
  }


  private fun loadMyScheduledGroupListAndUpdateUI() {
    activityViewModel.token?.let {
      lifecycleScope.launch {
        val result = crewService.getMyScheduledCrew(makeHeaderByAccessToken(it.accessToken))
        if (result.isSuccessful) {
          result.body()?.let { list ->
            if (list.isEmpty()) { // 등산이 완료된 그룹 뿐임
              hikingRecordingTabViewModel.setRecordingStatus(BANNED)
              binding.tvBannedDescription.visibility = View.GONE
              binding.tvBannedDescriptionTime.visibility = View.GONE
              binding.fragmentHikingRecordingLayoutBanned.visibility = View.VISIBLE
            } else { // 상행 시작 전 + 등산 중인 그룹이 하나 이상 있음
              if (isExistOnGoingCrew(list)) { // 등산 중인 그룹이 있음
                if (hikingRecordingTabViewModel.recordingStatus.value == BANNED) {
                  hikingRecordingTabViewModel.setRecordingStatus(BEFORE_HIKING)
                }
                binding.fragmentHikingRecordingLayoutBanned.visibility = View.GONE
              } else { // 등산 전인 그룹만 있음 -> 언제 다시 오라는 시간도 표시
                hikingRecordingTabViewModel.setRecordingStatus(BANNED)
                binding.tvBannedDescriptionTime.text =
                  findClosestFutureDate(list.map { it.crewStartDate })
                binding.fragmentHikingRecordingLayoutBanned.visibility =
                  View.VISIBLE
              }
            }
          }
          if (binding.fragmentHikingRecordingLayoutBanned.visibility == View.VISIBLE) {
            setViewAndChildrenEnabled(binding.fragmentHikingRecordingLayout, false)
          } else {
            setViewAndChildrenEnabled(binding.fragmentHikingRecordingLayout, true)
          }

        } else {
          Log.d(TAG, "loadMyScheduledGroupList: 실패")
        }
      }
    }
  }

  private fun setViewAndChildrenEnabled(view: View, enabled: Boolean) {
    view.isEnabled = enabled
    if (view is ViewGroup) {
      for (i in 0 until view.childCount) {
        val child = view.getChildAt(i)
        setViewAndChildrenEnabled(child, enabled)
      }
    }
  }

  private fun isExistOnGoingCrew(crews: List<Crew>): Boolean {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val currentDateTime = LocalDateTime.now()

    for (crew in crews) {
      val startDateTime = LocalDateTime.parse(crew.crewStartDate, formatter)
      val endDateTime = LocalDateTime.parse(crew.crewEndDate, formatter)

      Log.d(
        TAG,
        "isCurrentTimeWithinRange: Crew ID = ${crew.crewId}, start = $startDateTime, end = $endDateTime, now = $currentDateTime"
      )

      if (currentDateTime.isAfter(startDateTime) && currentDateTime.isBefore(endDateTime)) {
        hikingRecordingTabViewModel.setOnGoingCrewId(crew.crewId) // 현재 진행중인 crewID 세팅
        return true
      }
    }
    return false
  }

  private fun findClosestFutureDate(dates: List<String>): String? { // 현재 시간과 가장 가까운 미래 날짜를 반환
    val currentDateTime = LocalDateTime.now()
    val inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분")

    return dates
      .map { LocalDateTime.parse(it, inputFormatter) }
      .filter { it.isAfter(currentDateTime) }
      .minByOrNull { it }
      ?.format(outputFormatter)
  }

  private fun initClickListener() {
    initButtonClickListener()
    initHikingInfoViewClickListener()
  }

    private fun initButtonClickListener() {
        binding.btnHikingRecording.setOnClickListener {
            val currentStatus = hikingRecordingTabViewModel.recordingStatus.value
            Log.d(TAG, "initButtonClickListener: $currentStatus")
            when (currentStatus) {
                // 아직 기록 시작 안했을 때 => 상행으로 바뀜 버튼은 하행 버튼으로
                BEFORE_HIKING -> {
                    hikingRecordingTabViewModel.setRecordingStatus(HIKING)
                }
                // 상행 중이었을 때 => 하행으로 바뀜 버튼은 종료 버튼으로
                HIKING -> {
                    hikingRecordingTabViewModel.setRecordingStatus(AFTER_HIKING)
                }

                AFTER_HIKING -> {
                    hikingRecordingTabViewModel.setRecordingStatus(BANNED)
                }
            }

        }

    binding.btnCamera.setOnClickListener {
      rootActivity.checkPermission()
    }
  }

  private fun deleteSharedPreferences() {
    hikingRecordingTabViewModel.deleteOnGoingCrewId()
    hikingRecordingTabViewModel.deleteIsQRScanned()
    hikingRecordingTabViewModel.deleteIsQRCompleted()
  }

  private fun initHikingInfoViewClickListener() {
    binding.layoutHikingInfo.setOnClickListener {
      showExpandedInfoView()
    }
  }

  private fun showExpandedInfoView() {
    when (isHikingInfoViewExpanded) {
      true -> {
        binding.layoutStepCnt.visibility = View.GONE
        binding.layoutCalorie.visibility = View.GONE
        isHikingInfoViewExpanded = false
      }

      false -> {
        binding.layoutStepCnt.visibility = View.VISIBLE
        binding.layoutCalorie.visibility = View.VISIBLE
        isHikingInfoViewExpanded = true
      }
    }
  }

    private fun registerObserving() {
        hikingRecordingTabViewModel.recordingStatus.observe(viewLifecycleOwner) { status ->
            changeHikingButton(binding.btnHikingRecording, status)
            Log.d(TAG, "registerObserving: $status")
            when (status) {
                BANNED -> {
                    deActivateRecordingService()
                    deleteSharedPreferences()
                    resetChronometerTime()
                    hikingRecordingTabViewModel.deleteIsQRScanned()
                    hikingRecordingTabViewModel.deleteIsQRCompleted()

                }
                BEFORE_HIKING -> {
                    SingletonHandler.getHandler().removeCallbacks(runnable)
                    resetChronometerTime()
                }

                HIKING -> {
                    tryRecordingServiceByStatus("상행")
                    launchChronometer()
                }

                 AFTER_HIKING -> {
                      launchChronometer()
                }
            }
        }

        var isLeaderDialogShown = false // 중복 방지용 flag
        hikingRecordingTabViewModel.amILeader.observe(viewLifecycleOwner) { amILeader ->
            Log.d(TAG, "setInitialView: amILeader = $amILeader")
            val currentStatus = hikingRecordingTabViewModel.recordingStatus.value
            if (currentStatus == BEFORE_HIKING && !isLeaderDialogShown) {
                if (amILeader == true &&
                    (hikingRecordingTabViewModel.isQRCompleted.value == null || hikingRecordingTabViewModel.isQRCompleted.value == false)
                ) {
                    showLeaderQRCodeDialog()
                    setViewAndChildrenEnabled(binding.fragmentHikingRecordingLayout, false)
                    isLeaderDialogShown = true // flag 설정
                } else if (amILeader == false) {
                    if (hikingRecordingTabViewModel.isQRScanned.value == null || hikingRecordingTabViewModel.isQRScanned.value == false) {
                        binding.fragmentHikingRecordingLayoutBanned.visibility = View.VISIBLE
                        binding.tvBannedTitle.text = "방장의 QR코드를 찍어주세요!"
                        binding.tvBannedDescriptionTime.visibility = View.GONE
                        binding.tvBannedDescription.text = "QR을 찍으면 회원님의 위치를 공유하기 시작합니다."
                        setViewAndChildrenEnabled(binding.fragmentHikingRecordingLayout, false)
                    }

                }
            }
        }

        // 멤버
        hikingRecordingTabViewModel.isQRScanned.observe(viewLifecycleOwner) { isQRScanned ->
            val amILeader = hikingRecordingTabViewModel.amILeader.value
            Log.d(TAG, "setInitialView: isQRScanned = $isQRScanned")
            if (amILeader == false) {
                if (isQRScanned == true) {
                    binding.fragmentHikingRecordingLayoutBanned.visibility = View.GONE
                    setViewAndChildrenEnabled(binding.fragmentHikingRecordingLayout, true)
                } else {
                    binding.fragmentHikingRecordingLayoutBanned.visibility = View.VISIBLE
                    setViewAndChildrenEnabled(binding.fragmentHikingRecordingLayout, false)
                }
            }
        }

        // 방장
        hikingRecordingTabViewModel.isQRCompleted.observe(viewLifecycleOwner) { isQRCompleted ->
            val amILeader = hikingRecordingTabViewModel.amILeader.value
            if (amILeader == true) {
                if (isQRCompleted == true) {
                    binding.fragmentHikingRecordingLayoutBanned.visibility = View.GONE
                    setViewAndChildrenEnabled(binding.fragmentHikingRecordingLayout, true)
                } else {
                    binding.fragmentHikingRecordingLayoutBanned.visibility = View.VISIBLE
                    setViewAndChildrenEnabled(binding.fragmentHikingRecordingLayout, false)
                }
            }

        }

        hikingRecordingTabViewModel.onGoingCrewId.observe(viewLifecycleOwner) { crewId ->
            if (crewId > -1) {
                Log.d(TAG, "setInitialView: ongoingCrewID = $crewId")
                activityViewModel.token?.let { token ->
                    hikingRecordingTabViewModel.onGoingCrewId.value?.let { id ->
                        hikingRecordingTabViewModel.amILeader(
                            makeHeaderByAccessToken(token.accessToken),
                            id
                        )
                    }
                }
            }
        }

        chronometerViewModel.baseTime.observe(viewLifecycleOwner) { baseTime ->
            Log.d(TAG, "registerObserving: baseTime Change $baseTime")
            binding.timer.base = baseTime
            sharedPreferencesUtil.saveHikingRecordingBaseTime(baseTime)
        }
      }

    private fun registerLocalBroadCastReceiver() {
        LocalBroadcastManager.getInstance(rootActivity).registerReceiver(
            mMessageReceiver, IntentFilter("step")
        )
    }

  private fun showLeaderQRCodeDialog() {
    val dialog = LeaderQRCodeDialog()
    dialog.isCancelable = false
    dialog.show(rootActivity.supportFragmentManager, "dialog")
  }

  private fun activateRecordingService(status: String) {
    val serviceIntent =
      Intent(rootActivity, HikingRecordingService::class.java).apply {
        putExtra("status", status)
        putExtra("crewId", hikingRecordingTabViewModel.onGoingCrewId.value)
      }
      Log.d(TAG, "activateRecordingService isService : 서비스 시작")
      if(!isServiceRunning())
      {
          startForegroundService(rootActivity, serviceIntent)
      }
    sharedPreferencesUtil.saveRecordingServiceState(status)
    SingletonHandler.getHandler().post(runnable)
      Log.d(TAG, "activateRecordingService: handler 실행")
  }


    private fun deActivateRecordingService() {
        val serviceIntent = Intent(rootActivity, HikingRecordingService::class.java)
        requireActivity().stopService(serviceIntent)
        sharedPreferencesUtil.saveRecordingServiceState("종료")
        SingletonHandler.getHandler().removeCallbacks(runnable)
    }

  private fun checkRecordingService(): String {
    return sharedPreferencesUtil.getRecordingServiceState()
  }


    private fun tryRecordingServiceByStatus(status: String) {
        if (permissionChecker.checkPermission(rootActivity, PERMISSION)) {
            //권한있는 경우
            if (checkRecordingService() == "종료") {
                launchChronometer()
                activateRecordingService(status)
            }
            else if(!isServiceRunning()){
                activateRecordingService(status)
            }
        } else {
            showToast("권한을 설정하셔야 기록 서비스를 이용 가능합니다!")
            //ask for permission
            requestPermission {
                if (!isAllPermissionGranted(it)) {
                    showToast("권한없이는 등산 서비스를 제대로 이용하실 수 없습니다!")
                }else if(!isServiceRunning()){
                    activateRecordingService(status)
                }
                else {
                    if (checkRecordingService() == "종료") {
                        activateRecordingService(status)
                    }
                }
            }
        }
      }



    private fun changeHikingButton(button: AppCompatButton, toState: Int) {
        when (toState) {
            BANNED, BEFORE_HIKING -> {
                button.backgroundTintList =
                    getColorStateList(R.color.hiking_recording_tab_button_pink)
                button.text = "상행 시작"
            }

      HIKING -> {
        button.backgroundTintList = getColorStateList(R.color.sansanmulmul_green)
        button.text = "하행 시작"
      }

      AFTER_HIKING -> {
        button.backgroundTintList =
          getColorStateList(R.color.hiking_recording_tab_button_purple)
        button.text = "끝내기"
      }
    }
  }

  private fun getColorStateList(color: Int): ColorStateList? {
    return ContextCompat.getColorStateList(
      rootActivity,
      color
    )
  }

  private fun syncButtonStatus() {
    Log.d(TAG, "syncButtonStatus: syncButtonStastus")
    hikingRecordingTabViewModel.setRecordingStatus(sharedPreferencesUtil.getHikingRecordingState())
  }

  private fun resetBaseTime() {
    chronometerViewModel.setBaseTime(SystemClock.elapsedRealtime())
  }

  private fun launchChronometer() {
    initChronometerBaseTime()
    startChronometer()
  }

  private fun initChronometerBaseTime() {
    // 측정 방식이 base 시간을 기준으로 얼마나 시간이 흘렀는가임, UI에서는 base 시간과 elapsedTime 차이를 바로 보여주는 것
    when (hikingRecordingTabViewModel.recordingStatus.value) {
      BEFORE_HIKING -> {
        chronometerViewModel.setBaseTime(SystemClock.elapsedRealtime())
      }

      else -> {
        Log.d(
          TAG,
          "initChronometerBaseTime: ${sharedPreferencesUtil.getHikingRecordingBaseTime()}"
        )
        chronometerViewModel.setBaseTime(sharedPreferencesUtil.getHikingRecordingBaseTime())
      }
    }
  }

  private fun startChronometer() {
    binding.timer.start()
  }

  private fun resetChronometerTime() {
    Log.d(TAG, "initChronometerBaseTime: 설마 여기?")
    chronometerViewModel.setBaseTime(SystemClock.elapsedRealtime())
    binding.timer.stop()
  }

  private fun requestPermission(callback: ActivityResultCallback<Map<String, Boolean>>) {
    currentCallback = callback
    requestPermissionLauncher.launch(PERMISSION)
  }

  private fun setPermissionChecker() {
    permissionChecker = PermissionChecker(this)
  }

    private fun isAllPermissionGranted(result: Map<String, Boolean>): Boolean {
        result.values.forEach { check ->
            if (!check) {
                return false
            }
        }
        return true
    }
    private lateinit var naverMap: NaverMap
    override fun onMapReady(p0: NaverMap) {
        naverMap = p0
    }

    var runnable: Runnable = object : Runnable {
        override fun run() {
            // 반복해서 실행할 작업
            Log.d(TAG, "10초마다 반복 실행")
            hikingRecordingTabViewModel.onGoingCrewId.value?.let{
                lifecycleScope.launch {
                    Log.d(TAG, "run: 내가 조회하려는 크루 아이디는 $it")
                    val response = hikingRecordingService.getMemberLocation(it)
                    if(response.isSuccessful){
                        if(::naverMap.isInitialized){
                            // 네이버 지도에 찍힌 마커 초기화
                            hikingRecordingTabViewModel.memberMarkerList.value?.let {
                                it.forEach{ marker ->
                                    marker.map = null
                                }
                                it.clear()
                            }
                            response.body()?.let { list ->
                                Log.d(TAG, "run: 조회한 멤버들! $list")
                                list.forEach {
                                    if(it.userLat != null && it.userLon != null){
                                        hikingRecordingTabViewModel.memberMarkerList.value?.apply {
                                            add(
                                                Marker().apply {
                                                    safeCall {
                                                        position = LatLng(it.userLat, it.userLon)
                                                        Glide.with(binding.root).asBitmap().load(it.userProfileImg).into(
                                                            object : CustomTarget<Bitmap>() {
                                                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                                                    // 이미지 로드 완료 후 마커 설정
                                                                    val resizeBitmap = Bitmap.createScaledBitmap(resource, 60, 60, true)
                                                                    icon = OverlayImage.fromBitmap(resizeBitmap)   // 마커 아이콘 설정
                                                                }
                                                                override fun onLoadCleared(placeholder: Drawable?) {}
                                                            }
                                                        )
                                                        map = naverMap
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        showToast("동료 좌표 불러오기 실패")
                    }
                }
            }

            // 다시 10초 후에 실행
            SingletonHandler.getHandler().postDelayed(this, 10000) // 10000ms = 10초
        }
    }


    private fun isServiceRunning(): Boolean {
        Log.d(TAG, "isServiceRunning: ${HikingRecordingService.isRunning}")
        return HikingRecordingService.isRunning
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: destory!!!!!")
        super.onDestroyView()
    }
}

object SingletonHandler {
    // 메인 스레드에 연결된 Handler를 생성
    private val handler: Handler = Handler(Looper.getMainLooper())

    // Handler를 외부에서 사용할 수 있도록 반환하는 함수
    fun getHandler(): Handler {
        return handler
    }
}