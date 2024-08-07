package com.sansantek.sansanmulmul.ui.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sansantek.sansanmulmul.data.model.DateInfo
import com.sansantek.sansanmulmul.data.local.entity.StepCount
import com.sansantek.sansanmulmul.data.repository.StepCounterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val TAG = "HikingRecordingService_싸피"
class HikingRecordingService : Service(), SensorEventListener {

    private var stepCountSensor: Sensor? = null
    private val stepDetector = Sensor.TYPE_STEP_DETECTOR //보행 계수기
    private lateinit var sensorManager: SensorManager
    private var status = "undefined"

    private val repository by lazy {
        StepCounterRepository.get()
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
        Log.d(TAG, "onStartCommand: 서비스 시작")
        if(intent != null){
            status = intent.getStringExtra("status") ?: "undefined"
            Log.d(TAG, "onStartCommand: status $status")
        }
        else{
            stopSelf()
        }
        Log.d(TAG, "onStartCommand: 서비스 진짜 시작 $status")
        CoroutineScope(Dispatchers.IO).launch {
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            stepCountSensor = sensorManager.getDefaultSensor(stepDetector)
            sensorManager.registerListener(this@HikingRecordingService, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL)
            if (stepCountSensor == null) {
                launch(Dispatchers.Main) {
                    Toast.makeText(this@HikingRecordingService, "No Step Detect Sensor!!", Toast.LENGTH_SHORT).show()
                }
            }
            Log.d(TAG, "onStartCommand: 센서 등록 완료")
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
        builder.setSmallIcon(android.R.drawable.ic_menu_search)
        builder.setContentTitle("서비스 가동")
        // 알림을 밀어서 지우지 못하도록 설정
        builder.setOngoing(true)

        startForeground(111, builder.build()) // 서비스 가동시 알림 뜨고, 서비스 종료시 자동으로 사라짐 (종료 전엔 못 없앰)

        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        Log.d(TAG, "onSensorChanged: 센서 이벤트 수신")

        if(event?.sensor?.type == stepDetector){
            var step: StepCount
            CoroutineScope(Dispatchers.IO).launch {
                val today = getCurrentDateInfo()
                step = repository.getStepCount(today.year, today.month, today.day) ?: StepCount()

                Log.d(TAG, "onSensorChanged: stepcount 확인 $step")
                if (step.stepCount == -1) {

                    val date = getCurrentDateInfo()

                    step.year = date.year
                    step.month = date.month
                    step.day = date.day

                    step.stepCount = 1
                    Log.d(TAG, "onSensorChanged: insert")
                    CoroutineScope(Dispatchers.IO).launch {
                        repository.insertStepCount(step)
                    }
                } else {
                    step.stepCount += 1
                    Log.d(TAG, "onSensorChanged: update")
                    CoroutineScope(Dispatchers.IO).launch {
                        repository.updateStepCount(step)
                    }
                }

                val intent = Intent().apply {
                    action = "step"
                    putExtra("value", step.stepCount)
                }

                LocalBroadcastManager.getInstance(this@HikingRecordingService).sendBroadcast(intent)
            }
        }

    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: StepCounter $status 서비스 종료")
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // 정확도 바뀌었을 때 호출되는 함수인 듯?
        // 정확도 높이는 알고리즘을 짤 게 아니라면 건드릴 건 없을 듯
    }


}