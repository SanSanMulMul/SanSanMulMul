package com.sansantek.sansanmulmul.ui.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.Service.START_REDELIVER_INTENT
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager

import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.app.ServiceCompat.stopForeground
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

import com.google.android.gms.location.LocationServices
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.sansantek.sansanmulmul.data.local.entity.LocationHistory
import com.sansantek.sansanmulmul.data.local.entity.StepCount
import com.sansantek.sansanmulmul.data.model.DateInfo
import com.sansantek.sansanmulmul.data.model.HikingRecordingCoord
import com.sansantek.sansanmulmul.data.repository.LocationHistoryRepository
import com.sansantek.sansanmulmul.data.repository.StepCounterRepository
import com.sansantek.sansanmulmul.ui.service.HikingRecordingService.Companion.isRunning
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.hikingRecordingService
import com.sansantek.sansanmulmul.ui.util.Util.makeHeaderByAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

private const val TAG = "HikingRecordingService_싸피"

class HikingRecordingService : Service(), SensorEventListener {

    private var stepCountSensor: Sensor? = null
    private val gpsSensor: Sensor? = null
    private val stepDetector = Sensor.TYPE_STEP_DETECTOR //보행 계수기
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationManager: LocationManager
    private lateinit var sensorManager: SensorManager
    private var crewId: Int = -1
    private var status = "undefined"
    private val token = sharedPreferencesUtil.getKakaoLoginToken()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                // 위치 업데이트 처리
                handleLocation(location)
            }
        }
    }
    companion object{
        var isRunning: Boolean = false

    }

    private val stepCounterRepository by lazy {
        StepCounterRepository.get()
    }
    private val locationHistoryRepository by lazy {
        LocationHistoryRepository.get()
    }

    private fun handleLocation(location: Location){
        // 위치가 변경될 때마다 호출됩니다.
        Log.d(TAG, "onLocationChanged: GPS 위치 변경 수신")
        val latitude = location.latitude
        val longitude = location.longitude
        val altitude = location.altitude
        // TODO: 위치 정보를 사용하세요.
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                Log.d(TAG, "onLocationChanged: 위치 변경 감지 ${LocationHistory(crewId, LocalDateTime.now().toString(), latitude, longitude, altitude)} ")
                Log.d(TAG, "onLocationChanged: 서버로 위치 송신")
                token?.let {
                    val response = hikingRecordingService.saveMyCoord(makeHeaderByAccessToken(it.accessToken), HikingRecordingCoord(crewId, latitude,longitude))
                    if(response.isSuccessful){
                        Log.d(TAG, "onLocationChanged: 위치 전송 성공")
                    }
                }
            }
            launch {
                Log.d(TAG, "onLocationChanged: 위치 변경 감지 ${LocationHistory(crewId, LocalDateTime.now().toString(), latitude, longitude, altitude)} ")
                locationHistoryRepository.insertLocationHistory(LocationHistory(crewId, LocalDateTime.now().toString(), latitude, longitude, altitude))
                Log.d(TAG, "onLocationChanged: 위치 변경 감지 피니tl ${locationHistoryRepository.getLocationHistory(crewId)}")

            }
            Log.d(TAG, "onLocationChanged: stepCounter레포지토리 호출 직전")
            val step = stepCounterRepository.getStepCount(crewId) ?: StepCount()
            Log.d(TAG, "onLocationChanged: stepCounter레포지토리 호출 직전")
            Log.d(TAG, "onSensorChanged: stepcount 확인 $step")
            if (step.stepCount == 0 && step.elevation == -1.0) {

                step.crewId = crewId
                step.elevation = altitude
                Log.d(TAG, "onSensorChanged: insert")
                CoroutineScope(Dispatchers.IO).launch {
                    stepCounterRepository.insertStepCount(step)
                }
            }else{
                step.elevation = altitude
                Log.d(TAG, "onSensorChanged: update")
                CoroutineScope(Dispatchers.IO).launch {
                    stepCounterRepository.updateStepCount(step)
                }
            }

            val intent = Intent().apply {
                action = "step"
                putExtra("value", step)
            }

            LocalBroadcastManager.getInstance(this@HikingRecordingService).sendBroadcast(intent)
        }

    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            handleLocation(location)
        }
    }

    private fun getCurrentDateInfo(): DateInfo {
        val currentDate = LocalDate.now()
        val year = currentDate.year
        val month = currentDate.monthValue
        val day = currentDate.dayOfMonth
        return DateInfo(year, month, day)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand isRunning: 서비스 시작")
        isRunning = true
        if (intent != null) {
            status = intent.getStringExtra("status") ?: "undefined"
            crewId = intent.getIntExtra("crewId", -1)
            Log.d(TAG, "onStartCommand: status $status crewId $crewId")
        } else {
            isRunning = false
            Log.d(TAG, "serviceStatus isRunning intent 없음: ${isRunning}")
            stopSelf()
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel("hiking_recording", "FOREGROUND", NotificationManager.IMPORTANCE_HIGH)
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        // 앱 위에 알림 아이콘 뜨는것 안보이게 설정
        channel.setShowBadge(false)
        manager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(this, "hiking_recording")
        builder.setSmallIcon(R.drawable.sansanmulmul_logo)
        builder.setContentTitle("${status} 기록 서비스 가동")
        // 알림을 밀어서 지우지 못하도록 설정
        builder.setOngoing(true)
        ServiceCompat.startForeground(this, 111, builder.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION) // 서비스 가동시 알림 뜨고, 서비스 종료시 자동으로 사라짐 (종료 전엔 못 없앰)
        Log.d(TAG, "onStartCommand: startForeground 실행 완료 $status")
        CoroutineScope(Dispatchers.Default).launch {
            launch {
                sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
                stepCountSensor = sensorManager.getDefaultSensor(stepDetector)
                sensorManager.registerListener(this@HikingRecordingService, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL)
                if (stepCountSensor == null) {
                    launch(Dispatchers.Main) {
                        Toast.makeText(this@HikingRecordingService, "No Step Detect Sensor!!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            launch {
                if (!::locationManager.isInitialized) {
                    locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
                }
                if (ActivityCompat.checkSelfPermission(this@HikingRecordingService, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this@HikingRecordingService, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    launch(Dispatchers.Main) {
                        Toast.makeText(this@HikingRecordingService, "기록이 되지 않습니다", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    launch(Dispatchers.Main) {
                        startLocationUpdates()
                    }
                }
            }
            Log.d(TAG, "onStartCommand: 센서 등록 완료")
        }
        Log.d(TAG, "serviceStatus isRunning: $isRunning")
        return START_REDELIVER_INTENT
    }

    fun startLocationUpdates() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            // API 31 이상에서는 FusedLocationProviderClient 사용
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            val locationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 10000L  // 10초마다 업데이트
                fastestInterval = 5000L  // 최소 5초 간격으로 업데이트
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }
        } else {
            // API 30 이하에서는 LocationManager 사용
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000L,  // 10초마다 업데이트
                    10f,  // 10미터 이동 시 업데이트
                    locationListener
                )
            }
        }
    }

    fun stopLocationUpdates() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            fusedLocationClient?.removeLocationUpdates(locationCallback)
        } else {
            locationManager?.removeUpdates(locationListener)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        Log.d(TAG, "onSensorChanged: 센서 이벤트 수신")

        if (event?.sensor?.type == stepDetector) {
            var step: StepCount
            CoroutineScope(Dispatchers.IO).launch {
                step = stepCounterRepository.getStepCount(crewId) ?: StepCount()

                Log.d(TAG, "onSensorChanged: stepcount 확인 $step")
                if (step.stepCount == 0 && step.elevation == -1.0) {

                    step.crewId = crewId

                    step.stepCount = 1
                    Log.d(TAG, "onSensorChanged: insert")
                    CoroutineScope(Dispatchers.IO).launch {
                        stepCounterRepository.insertStepCount(step)
                    }
                }else{
                    step.stepCount += 1
                    Log.d(TAG, "onSensorChanged: update")
                    CoroutineScope(Dispatchers.IO).launch {
                        stepCounterRepository.updateStepCount(step)
                    }
                }

                val intent = Intent().apply {
                    action = "step"
                    putExtra("value", step)
                }

                LocalBroadcastManager.getInstance(this@HikingRecordingService).sendBroadcast(intent)
            }
        }
    }

    override fun onDestroy() {
        isRunning = false
        Log.d(TAG, "serviceStatus: isRunning ${isRunning} 서비스 종료")
        Log.d(TAG, "onDestroy: StepCounter $status 서비스 종료")
        if(::sensorManager.isInitialized){
            sensorManager.unregisterListener(this)
        }
        stopLocationUpdates()
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // 정확도 바뀌었을 때 호출되는 함수인 듯?
        // 정확도 높이는 알고리즘을 짤 게 아니라면 건드릴 건 없을 듯
    }


}