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
    /**
     * 버전에 따른 Permission 종류들
     */
    private val PERMISSION = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    } else if (Build.VERSION.SDK_INT >= 29) {
        arrayOf(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    } else {
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { currentCallback?.onActivityResult(it) }

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context, intent: Intent) {
            val message = intent.getSerializableExtra("value") as StepCount
            Log.d(TAG, "Got message: " + message)
            safeCall {
                binding.tvStepCnt.text = message.stepCount.toString()

                val distance = (0.74 * message.stepCount)
                binding.tvDistance.text = if (distance < 1000) { "${distance.toInt()} m" } else { "${String.format("%.2f", (distance / 1000))} km" }

                binding.tvHeight.text = if (message.elevation < 1000) { if (message.elevation != -1.0) { "${message.elevation.toInt()} m" } else { "0 km" }
                } else { "${message.elevation.toInt() / 1000} km" }

                val kcal = (46.62 * message.stepCount)
                binding.tvCalorie.text = if (kcal < 1000) { "${kcal.toInt()} cal" } else { "${String.format("%.2f", (kcal / 1000))} kcal" }
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
        registerObserving()
        syncRecordingStatus()
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
        registerLocalBroadCastReceiver()
        hideBottomNav(rootActivity.findViewById(R.id.main_layout_bottom_navigation), false)
        initNaverMap()
    }

    /**
     * fragment로 만들어 놓은 navermap을 형변환한 후 비동기로 NaverMap 객체를 얻어옴
      */
    private fun initNaverMap(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.hiking_recording_tab_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.hiking_recording_tab_map, it).commit()
            }

    mapFragment.getMapAsync(this)
  }

    /**
     * 나의 Schedule이 완료되지 않은 Crew들의 목록을 불러와서 현재 기록이 계획되어 있는 Crew가 있는지를 확인한 후
     *
     * 만약 계획이 없다면 Status를 Ban으로, 있다면,
     *
     * Ban일 경우는 Before_Hiking이라는 초기 상태로 설정하고,
     *
     * Ban이 아니라면 이미 기록 중인 상태이므로, 그대로 두고서, 기능을 막는 창을 없앰
     *
     * 만약 계획되어 있는 Crew들은 있지만 현재 시간이랑 맞는 것은 없다면,
     *
     * 다음 기록 이벤트 안내와 함께 Status를 Ban으로 설정하는 함수를 호출하는 함수
     */
    private fun setInitialView() {
        loadMyScheduledGroupListAndUpdateUI()
    }

    /**
     * 나의 Schedule이 완료되지 않은 Crew들의 목록을 불러와서 현재 기록이 계획되어 있는 Crew가 있는지를 확인한 후
     *
     * 만약 계획이 없다면 Status를 Ban으로, 있다면,
     *
     * Ban일 경우는 Before_Hiking이라는 초기 상태로 설정하고,
     *
     * Ban이 아니라면 이미 기록 중인 상태이므로, 그대로 두고서, 기능을 막는 창을 없앰
     *
     * 만약 계획되어 있는 Crew들은 있지만 현재 시간이랑 맞는 것은 없다면,
     *
     * 다음 기록 이벤트 안내와 함께 Status를 Ban으로 설정
     */
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
          if (binding.fragmentHikingRecordingLayoutBanned.visibility == View.VISIBLE) {
            setViewAndChildrenEnabled(binding.fragmentHikingRecordingLayout, false)
          } else {
            setViewAndChildrenEnabled(binding.fragmentHikingRecordingLayout, true)
          }
      }

    /**
     * 밴 레이아웃 뒤에 원래 있던 지도나 버튼들을 전부 비활성화, 활성화 시키는 함수
     *
     * 밴 상태에 따라 호출
     */
    private fun setViewAndChildrenEnabled(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                setViewAndChildrenEnabled(child, enabled)
            }
        }
    }

    /**
     * 지금 현재 기록을 진행해야 할 Crew가 있는지 조회해서 True, False로 알려주는 함수
     */
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

    /**
     * 문자열로 된 시간 값이 들어있는 리스트들을 담아서, 현재 시간보다 미래면서 가장 가까운 날짜를 반환하는 함수
     *
     * 인자로 계획되어 있는 Crew들의 시작 날짜들을 리스트로 담아서 던진다
     */
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
      val dialog = CameraGuideDialog()
      dialog.show(childFragmentManager, "CameraGuideDialog")
    }
  }

    /**
     * SharedPreference로 저장하고 있던 기록을 진행 중이던 Crew Id 값, QR 스캔 했는지 여부, QR이 완료됐는지 여부를 초기화 하는 함수
     */
    private fun deleteSharedPreferences() {
        hikingRecordingTabViewModel.deleteOnGoingCrewId()
        hikingRecordingTabViewModel.deleteIsQRScanned()
        hikingRecordingTabViewModel.deleteIsQRCompleted()
    }
    /**
     * 기록 정보를 보여주는 상단 탭의 확장 기능을 제공하는 함수
     */
    private fun initHikingInfoViewClickListener() {
        binding.layoutHikingInfo.setOnClickListener {
            showExpandedInfoView()
        }
    }
    /**
     * 기록 관련 정보를 보여주는 상단 탭의 확장 기능을 제공하는 함수
     *
     * 현재 상태에 따라 이미 확장된 상태였다면, 시간, 거리, 고도만을 보여주는 간단한 보기 상태로
     *
     * 간단한 보기 상태였다면 추가적으로 걸음 수와 칼로리도 보여주는 확장된 보기 상태로 설정하는 함수
     */
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

    /**
     * LiveData의 값 변화에 따른 Callback 함수를 등록하는 함수
     */
    private fun registerObserving() {
        observeHikingStatus()

        observeAmILeader()

        observeIsQRScanned()

        observeIsQRCompleted()

        observeOnGoingCrewId()

        observeBaseTime()
    }

    /**
     * crewId를 관찰하는 함수
     *
     * 만약 -1이 아니라면 정상적인 크루의 값이므로,
     *
     * 내가 해당 크루의 방장인지 유무를 구해서 세팅한다
     */
    private fun observeOnGoingCrewId(){
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
    }

    // 방장
    /**
     * QR Complete의 속성이 바뀌면 내가 방장인지 여부를 확인한 뒤,
     *
     * 방장이면, QR이 complete 됐을 때 밴 레이아웃이 내려가면서 하위 영역 상호작용이 가능하도록
     *
     * 아니면 밴 레이아웃 작동 및 하위 영역 상호작용이 안되도록 설정
     */
    private fun observeIsQRCompleted(){
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
    }

    // 멤버
    /**
     * 일반 멤버가 QR 스캔을 했는지 등 상태 변화에 따른 함수
     *
     * 만약 했다고 상태가 변하면, Ban 화면을 가리고, Ban 아래의 영역에 대한 상호작용을 다시 허용함
     *
     * 만약 안했다고 설정되면, Ban이 활성화되고, Ban 아래의 화면들에 대한 상호작용을 전부 막음
     */
    private fun observeIsQRScanned(){
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
    }

    /**
     * Chronometer(시간 측정)이 바뀌었을 때 해당 값을 basetime으로 세팅 및 SharedPreference에 저장하는 Observe 함수
     */
    private fun observeBaseTime(){
        chronometerViewModel.baseTime.observe(viewLifecycleOwner) { baseTime ->
            Log.d(TAG, "registerObserving: baseTime Change $baseTime")
            if(baseTime == 0L){
                binding.timer.base = SystemClock.elapsedRealtime()
            }else{
                binding.timer.base = baseTime
            }
            sharedPreferencesUtil.saveHikingRecordingBaseTime(baseTime)
        }
    }

    /**
     * 등산 Status가 바뀌었을 때 로직을 실행하는 함수
     *
     * Ban이면 등산 기록 서비스를 종료 시키고, SharedPreferences에 저장되어 있는 CrewId, QR 스캔 여부, QR을 완료 했는지 여부를 삭제 후, chronometer를 초기화 및 동료들의 위치를 조회하는 Handler를 삭제시킴
     *
     * BeforeHiking이면, 동료들의 위치를 조회하는 Handler를 삭제시키고, chronometer를 초기화 함
     *
     * Hiking이면, 등산 기록 서비스를 활성화 시키는 함수를 실행시키고, Chronometer 작동함
     *
     * After Hiking이면 하행 시작으로, 기록하던 상행 서비스를 종료시키고,
     * 다시 하행 서비스를 기록하기 시작함
     */
    private fun observeHikingStatus() {
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
                    SingletonHandler.getHandler().removeCallbacks(runnable)
                }

                BEFORE_HIKING -> {
                    SingletonHandler.getHandler().removeCallbacks(runnable)
                    resetChronometerTime()
                }

                HIKING -> {
                    tryRecordingServiceByStatus("상행")
                }

                AFTER_HIKING -> {
                    deActivateRecordingService()
                    tryRecordingServiceByStatus("하행")
                }
            }
        }
    }

    /**
     * 방장인지, Dialog를 보여준 적이 있는지, 방장이 아니라면 Qr을 보여준 적이 있는지에 따라 로직을 수행하는 함수
     *
     * 현재 등산 status를 불러와서, 만약 BeforeHiking이면서, Dialog를 보여준 적도 없는 상황이라면,
     *
     * 내가 Leader이면서, QR 확인 완료를 한 적이 없는지를 확인하고
     *
     * Leader이지만 QR 완료를 한 적이 없다면, QR을 띄워주고, QR외의 버튼들의 상호작용을 막는 함수를 실행시킨 뒤, Dialog를 보여준 적 있는 것으로 flag 설정
     *
     * 만약 Leader가 아니면서, QR 스캔을 아직 한 적이 없다면, 화면의 상호작용을 막는 함수를 실행시킨 뒤, Ban을 활성화 시키고, QR을 찍어달라는 안내 문구 삽입
     */
    private fun observeAmILeader() {
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

    /**
     * 기록 서비스를 활성화 시키는 함수
     *
     * 서비스가 활성화 되어있는지 확인해서 이미 돌아가고 있다면, status만 현재 status를 저장 및 동료들의 위치를 조회하는 Handler를 실행
     *
     * 없다면 상행인지 하행인지를 구분할 수 있는 status 유무와, crewId를 전해주고, 기록 서비스를 실행
     */
    private fun activateRecordingService(status: String) {
        val serviceIntent =
            Intent(rootActivity, HikingRecordingService::class.java).apply {
                putExtra("status", status)
                putExtra("crewId", hikingRecordingTabViewModel.onGoingCrewId.value)
            }
        Log.d(TAG, "activateRecordingService isService : 서비스 시작")
        if (!isServiceRunning()) {
            startForegroundService(rootActivity, serviceIntent)
        }
        sharedPreferencesUtil.saveRecordingServiceState(status)
        SingletonHandler.getHandler().post(runnable)
        Log.d(TAG, "activateRecordingService: handler 실행")
    }

    /**
     * 기록 서비스를 비활성화 시키면서 status를 종료로 저장하고 동료의 위치를 불러오는 Handler를 종료하는 함수
     */
    private fun deActivateRecordingService() {
        val serviceIntent = Intent(rootActivity, HikingRecordingService::class.java)
        requireActivity().stopService(serviceIntent)
        sharedPreferencesUtil.saveRecordingServiceState("종료")
        SingletonHandler.getHandler().removeCallbacks(runnable)
    }

    /**
     * SharedPreference에 저장되어 있는 기록 status를 반환하는 함수
      */

    private fun checkRecordingService(): String {
        return sharedPreferencesUtil.getRecordingServiceState()
    }

    /**
     * 기록을 시도하는 함수
     *
     * 우선 권한체크를 통해 기록을 위해 필요한 권한이 있는지 혹인 후, 없으면 요청
     *
     * 권한이 확보되면, 기록 상태를 확인해서, 종료된 상태거나, Service가 돌아가고 있지 않다면, 서비스를 활성화 시킴
     *
     */
    private fun tryRecordingServiceByStatus(status: String) {
        if (permissionChecker.checkPermission(rootActivity, PERMISSION)) {
            //권한있는 경우
            if (checkRecordingService() == "종료" || !isServiceRunning()) {
                activateRecordingService(status)
            }
            launchChronometer()
        } else {
            showToast("권한을 설정하셔야 기록 서비스를 이용 가능합니다!")
            //ask for permission
            requestPermission {
                if (!isAllPermissionGranted(it)) {
                    showToast("권한없이는 등산 서비스를 제대로 이용하실 수 없습니다!")
                } else if (!isServiceRunning()) {
                    activateRecordingService(status)
                    launchChronometer()
                } else {
                    if (checkRecordingService() == "종료") {
                        activateRecordingService(status)
                        launchChronometer()
                    }
                }
            }
        }
      }

    /**
     * 하단의 등산 버튼을 변경하는 함수
     *
     * 등산 Status에 따라 각각에 맞는 버튼을 제공함
     */
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


    private fun launchChronometer() {
    initChronometerBaseTime()
    startChronometer()
  }

  private fun requestPermission(callback: ActivityResultCallback<Map<String, Boolean>>) {
    currentCallback = callback
    requestPermissionLauncher.launch(PERMISSION)
  }

  private fun setPermissionChecker() {
    permissionChecker = PermissionChecker(this)
  }
    /**
     *현재 등산 기록 상태를 조회해서 그에 맞는 ViewModel의 Status를 업데이트할 수 있도록 하는 함수
      */
    private fun syncRecordingStatus() {
        Log.d(TAG, "syncButtonStatus: syncButtonStastus")
        hikingRecordingTabViewModel.setRecordingStatus(sharedPreferencesUtil.getHikingRecordingState())
    }

    /**
     * sharedPreferencesUtil에서 baseTime을 가져오는데 reset이 되는 등 설정이 0으로 되어 있으면
     *
     * baseTime이 없는 것이므로 현재 시간으로 맞춤, 만약 있으면 저장된 baseTime을 사용 및 세팅
     *
     * 참고로 baseTime은 ban, before hiking때마다 0으로 초기화 됨
     * 또한, baseTime이 set될 때마다 sharedPreference에 저장하도록 Observe 함수를 만들어 놓았으므로 set 함수에 로직 작성 필요 X
     */
    private fun initChronometerBaseTime() {
        // 측정 방식이 base 시간을 기준으로 얼마나 시간이 흘렀는가임, UI에서는 base 시간과 elapsedTime 차이를 바로 보여주는 것
        val savedBaseTime = sharedPreferencesUtil.getHikingRecordingBaseTime()
        if(savedBaseTime == 0L){
            chronometerViewModel.setBaseTime(SystemClock.elapsedRealtime())
        }
        else{
            chronometerViewModel.setBaseTime(savedBaseTime)
        }
    }

    private fun startChronometer() {
        binding.timer.start()
    }

    private fun resetChronometerTime() {
        chronometerViewModel.setBaseTime(0)
        binding.timer.stop()
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
            hikingRecordingTabViewModel.onGoingCrewId.value?.let {
                lifecycleScope.launch {
                    Log.d(TAG, "run: 내가 조회하려는 크루 아이디는 $it")
                    val response = hikingRecordingService.getMemberLocation(it)
                    if (response.isSuccessful) {
                        if (::naverMap.isInitialized) {
                            // 네이버 지도에 찍힌 마커 초기화
                            hikingRecordingTabViewModel.memberMarkerList.value?.let {
                                it.forEach { marker ->
                                    marker.map = null
                                }
                                it.clear()
                            }
                            response.body()?.let { list ->
                                Log.d(TAG, "run: 조회한 멤버들! $list")
                                list.forEach {
                                    if (it.userLat != null && it.userLon != null) {
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
                    } else {
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