package com.sansantek.sansanmulmul.ui.view.hikingrecordingtab

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.config.Const.Companion.AFTER_HIKING
import com.sansantek.sansanmulmul.config.Const.Companion.BEFORE_HIKING
import com.sansantek.sansanmulmul.config.Const.Companion.HIKING
import com.sansantek.sansanmulmul.databinding.FragmentHikingRecordingTabBinding
import com.sansantek.sansanmulmul.ui.service.HikingRecordingService
import com.sansantek.sansanmulmul.ui.util.PermissionChecker
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.viewmodel.ChronometerViewModel
import com.sansantek.sansanmulmul.ui.viewmodel.HikingRecordingTabViewModel


private const val TAG = "HikingRecordingTabFragment_싸피"
class HikingRecordingTabFragment : BaseFragment<FragmentHikingRecordingTabBinding>(
    FragmentHikingRecordingTabBinding::bind,
    R.layout.fragment_hiking_recording_tab
), OnMapReadyCallback {
    private lateinit var permissionChecker: PermissionChecker
    private var isHikingInfoViewExpanded = false
    private lateinit var rootActivity: MainActivity
    private var currentCallback: ActivityResultCallback<Map<String, Boolean>>? = null
    private val hikingRecordingTabViewModel: HikingRecordingTabViewModel by viewModels()
    private val chronometerViewModel: ChronometerViewModel by viewModels()
    private val PERMISSION = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.POST_NOTIFICATIONS)
    } else if (Build.VERSION.SDK_INT >= 29) {
        arrayOf(Manifest.permission.ACTIVITY_RECOGNITION)
    } else {
        arrayOf()
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){
        Log.d(TAG, "requestPermissionLauncher: 건수 : ${it.size}")
        currentCallback?.onActivityResult(it)
    }
    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context, intent: Intent) {
            val message = intent.getIntExtra("value", -1)
            Log.d(TAG, "Got message: " + message)
            binding.tvStepCnt.text = message.toString()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            rootActivity = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: init 시작")
        init()
        showQRCodeDialog()
        Log.d(TAG, "onViewCreated: init 종료")
    }

    override fun onResume() {
        super.onResume()
        // 지금 기록 중이었는지 확인하고 상태에 따라 버튼 초기화
        Log.d(TAG, "onViewCreated: resume 시작")
        syncButtonStatus()
        initClickListener()
        Log.d(TAG, "onViewCreated: resume 종료")
    }

    private fun init() {
        setPermissionChecker()
        registerObserving()
        registerLocalBroadCastReceiver()
        hideBottomNav(
            rootActivity.findViewById(R.id.main_layout_bottom_navigation),
            false
        )
    }

    private fun initClickListener(){
        initButtonClickListener()
        initHikingInfoViewClickListener()
        initBanLayoutClickListener()
    }

    private fun initButtonClickListener() {
        binding.btnHikingRecording.setOnClickListener {
            val currentStatus = hikingRecordingTabViewModel.recordingStatus.value
            Log.d(TAG, "initButtonClickListener: $currentStatus")
            when (currentStatus) {
                // 아직 기록 시작 안했을 때 => 상행으로 바뀜 버튼은 하행 버튼으로
                BEFORE_HIKING -> {
                    launchChronometer()
                    tryRecordingServiceByStatus("상행")
                    hikingRecordingTabViewModel.setRecordingStatus(HIKING)
                }
                // 상행 중이었을 때 => 하행으로 바뀜 버튼은 종료 버튼으로
                HIKING -> {
                    deActivateRecordingService()
                    resetChronometerTime()
                    launchChronometer()
                    tryRecordingServiceByStatus("하행")
                    hikingRecordingTabViewModel.setRecordingStatus(AFTER_HIKING)
                }

                AFTER_HIKING -> {
                    resetChronometerTime()
                    deActivateRecordingService()
                    hikingRecordingTabViewModel.setRecordingStatus(BEFORE_HIKING)
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

    private fun showExpandedInfoView(){
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

    private fun initBanLayoutClickListener() {
        // 일단은 banned 화면 클릭하면 없어지도록..
        binding.fragmentHikingRecordingLayoutBanned.setOnClickListener {
            binding.fragmentHikingRecordingLayoutBanned.visibility = View.GONE
        }
    }

    private fun registerObserving(){
        hikingRecordingTabViewModel.recordingStatus.observe(viewLifecycleOwner) { status ->
            changeHikingButton(binding.btnHikingRecording, status)
            when(status){
                BEFORE_HIKING -> {
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

    private fun registerLocalBroadCastReceiver(){
        LocalBroadcastManager.getInstance(rootActivity).registerReceiver(
            mMessageReceiver, IntentFilter("step")
        )
    }

    private fun showQRCodeDialog() {
        QRCodeDialog().show(rootActivity.supportFragmentManager, "dialog")
    }

    private fun activateRecordingService(status: String){
        val serviceIntent = Intent(rootActivity,HikingRecordingService::class.java).apply { putExtra("status", status) }
        startForegroundService(rootActivity, serviceIntent)
        sharedPreferencesUtil.saveRecordingServiceState(status)
    }

    private fun deActivateRecordingService(){
        val serviceIntent = Intent(rootActivity,HikingRecordingService::class.java)
        requireActivity().stopService(serviceIntent)
        sharedPreferencesUtil.saveRecordingServiceState("종료")
    }

    private fun checkRecordingService(): String{
        return sharedPreferencesUtil.getRecordingServiceState()
    }

    private fun tryRecordingServiceByStatus(status: String){
        if(permissionChecker.checkPermission(rootActivity, PERMISSION)){
            //권한있는 경우
            if(checkRecordingService() == "종료"){
                activateRecordingService(status)
            }
        }else{
            showToast("권한을 설정하셔야 기록 서비스를 이용 가능합니다!")
            //ask for permission
            requestPermission {
                if (!isAllPermissionGranted(it)) {
                    showToast("권한없이는 등산 서비스를 제대로 이용하실 수 없습니다!")
                } else {
                    if (checkRecordingService()=="종료") {
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
            BEFORE_HIKING -> {
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

    private fun syncButtonStatus(){
        Log.d(TAG, "syncButtonStatus: syncButtonStastus")
        hikingRecordingTabViewModel.setRecordingStatus(sharedPreferencesUtil.getHikingRecordingState())
    }

    private fun resetBaseTime(){
        chronometerViewModel.setBaseTime(SystemClock.elapsedRealtime())
    }

    private fun launchChronometer() {
        initChronometerBaseTime()
        startChronometer()
    }

    private fun initChronometerBaseTime() {
        // 측정 방식이 base 시간을 기준으로 얼마나 시간이 흘렀는가임, UI에서는 base 시간과 elapsedTime 차이를 바로 보여주는 것
        when(hikingRecordingTabViewModel.recordingStatus.value){
            BEFORE_HIKING ->{
                chronometerViewModel.setBaseTime(SystemClock.elapsedRealtime())
            }
            else -> {
                Log.d(TAG, "initChronometerBaseTime: ${sharedPreferencesUtil.getHikingRecordingBaseTime()}")
                chronometerViewModel.setBaseTime(sharedPreferencesUtil.getHikingRecordingBaseTime())
            }
        }
    }

    private fun startChronometer(){
        binding.timer.start()
    }

    private fun resetChronometerTime() {
        Log.d(TAG, "initChronometerBaseTime: 설마 여기?")
        chronometerViewModel.setBaseTime(SystemClock.elapsedRealtime())
        binding.timer.stop()
    }

    private fun requestPermission(callback: ActivityResultCallback<Map<String, Boolean>>){
        currentCallback = callback
        requestPermissionLauncher.launch(PERMISSION)
    }

    private fun setPermissionChecker(){
        permissionChecker = PermissionChecker(this)
    }

    private fun isAllPermissionGranted(result: Map<String, Boolean>): Boolean{
        result.values.forEach { check ->
            if (!check) { return false }
        }
        return true
    }

    override fun onMapReady(p0: NaverMap) {

    }
}