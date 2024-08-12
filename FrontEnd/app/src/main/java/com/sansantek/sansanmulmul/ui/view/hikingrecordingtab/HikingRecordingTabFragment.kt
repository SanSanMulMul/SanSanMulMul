package com.sansantek.sansanmulmul.ui.view.hikingrecordingtab

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
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
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
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
  private val hikingRecordingTabViewModel: HikingRecordingTabViewModel by viewModels()
  private val chronometerViewModel: ChronometerViewModel by viewModels()
  private val PERMISSION = if (Build.VERSION.SDK_INT >= 33) {
    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
  } else if (Build.VERSION.SDK_INT >= 29) {
    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
  } else {
    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
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
        binding.tvDistance.text = if(distance < 1000){
          "${distance.toInt()} m"
        }else{
          "${String.format("%.2f", (distance / 1000))} km"
        }

        binding.tvHeight.text = if(message.elevation < 1000){
          if(message.elevation != -1.0){
            "${message.elevation.toInt()} m"
          }else{
            "0 km"
          }
        }else{
          "${message.elevation.toInt() / 1000} km"
        }

        val kcal = (46.62 * message.stepCount)
        binding.tvCalorie.text = if(kcal < 1000){
          "${kcal.toInt()} cal"
        }else{
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
  }

  private fun setInitialView() {
    loadMyScheduledGroupListAndUpdateUI()

    hikingRecordingTabViewModel.isQRScanned.observe(viewLifecycleOwner) { isQRScanned ->
      Log.d(TAG, "setInitialView: isQRScanned = $isQRScanned")
      if (isQRScanned == true) {
        binding.fragmentHikingRecordingLayoutBanned.visibility = View.GONE
      }
    }

    hikingRecordingTabViewModel.onGoingCrewId.observe(viewLifecycleOwner) { crewId ->
      if (crewId > -1) {
        Log.d(TAG, "setInitialView: ongoingCrewID = $crewId")
        activityViewModel.token?.let { token ->
          hikingRecordingTabViewModel.onGoingCrewId.value?.let { id ->
            hikingRecordingTabViewModel.amILeader(makeHeaderByAccessToken(token.accessToken), id)
          }
        }
      }
    }

    hikingRecordingTabViewModel.amILeader.observe(viewLifecycleOwner) { amILeader ->
      Log.d(TAG, "setInitialView: amILeader = $amILeader")
      val currentStatus = hikingRecordingTabViewModel.recordingStatus.value
      if (currentStatus == BEFORE_HIKING) {
        if (amILeader == true) {
          showLeaderQRCodeDialog()
        } else {
          if (hikingRecordingTabViewModel.isQRScanned.value == false) {
            binding.fragmentHikingRecordingLayoutBanned.visibility = View.VISIBLE
            binding.tvBannedTitle.text = "방장의 QR코드를 찍어주세요!"
            binding.tvBannedDescriptionTime.visibility = View.GONE
            binding.tvBannedDescription.text = "QR을 찍으면 회원님의 위치를 공유하기 시작합니다."
            setViewAndChildrenEnabled(binding.fragmentHikingRecordingLayout, false)
          }
        }
      }
    }
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
                binding.fragmentHikingRecordingLayoutBanned.visibility = View.VISIBLE
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
          tryRecordingServiceByStatus("상행")
          hikingRecordingTabViewModel.setRecordingStatus(HIKING)
        }
        // 상행 중이었을 때 => 하행으로 바뀜 버튼은 종료 버튼으로
        HIKING -> {
          deActivateRecordingService()
          resetChronometerTime()
          tryRecordingServiceByStatus("하행")
          hikingRecordingTabViewModel.setRecordingStatus(AFTER_HIKING)
        }

        AFTER_HIKING -> {
          resetChronometerTime()
          deActivateRecordingService()
          hikingRecordingTabViewModel.setRecordingStatus(BANNED)
          hikingRecordingTabViewModel.deleteState()
        }
      }
    }
    binding.btnCamera.setOnClickListener {
      rootActivity.checkPermission()
    }
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
      when (status) {
        BANNED, BEFORE_HIKING -> {
          resetChronometerTime()
        }

        HIKING -> {
          launchChronometer()
        }

        AFTER_HIKING -> {
          launchChronometer()
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
    LeaderQRCodeDialog().show(rootActivity.supportFragmentManager, "dialog")
  }

  private fun activateRecordingService(status: String) {
    val serviceIntent =
      Intent(rootActivity, HikingRecordingService::class.java).apply {
        putExtra("status", status)
        putExtra("crewId", hikingRecordingTabViewModel.onGoingCrewId.value)
      }
    startForegroundService(rootActivity, serviceIntent)
    sharedPreferencesUtil.saveRecordingServiceState(status)
  }

  private fun deActivateRecordingService() {
    val serviceIntent = Intent(rootActivity, HikingRecordingService::class.java)
    requireActivity().stopService(serviceIntent)
    sharedPreferencesUtil.saveRecordingServiceState("종료")
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
    } else {
      showToast("권한을 설정하셔야 기록 서비스를 이용 가능합니다!")
      //ask for permission
      requestPermission {
        if (!isAllPermissionGranted(it)) {
          showToast("권한없이는 등산 서비스를 제대로 이용하실 수 없습니다!")
        } else {
          if (checkRecordingService() == "종료") {
            activateRecordingService(status)
          }
        }
      }
    }
  }

//    fun setImageBitmap(imageBitmap: Bitmap?) {
//        binding.hikingRecordingTabMap.setImageBitmap(imageBitmap)
//    }

  private fun changeHikingButton(button: AppCompatButton, toState: Int) {
    when (toState) {
      BANNED, BEFORE_HIKING -> {
        button.backgroundTintList = getColorStateList(R.color.hiking_recording_tab_button_pink)
        button.text = "상행 시작"
      }

      HIKING -> {
        button.backgroundTintList = getColorStateList(R.color.sansanmulmul_green)
        button.text = "하행 시작"
      }

      AFTER_HIKING -> {
        button.backgroundTintList = getColorStateList(R.color.hiking_recording_tab_button_purple)
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
        Log.d(TAG, "initChronometerBaseTime: ${sharedPreferencesUtil.getHikingRecordingBaseTime()}")
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

  override fun onMapReady(p0: NaverMap) {

  }
}