package com.sansantek.sansanmulmul.ui.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.ui.view.LoginActivity
import com.sansantek.sansanmulmul.ui.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MyFirebaseMessageServic 싸피"
class MyFirebaseMessageService : FirebaseMessagingService() {

    // 새로운 토큰이 생성될 때 마다 해당 콜백이 호출된다.
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
        // 새로운 토큰 수신 시 서버로 전송
        //LoginActivity.uploadToken(token)
//        val userid = sharedPreferencesUtil.getUser().id
//        LoginActivity.uploadToken(userid, token)
    }
    // Foreground, Background 모두 처리하기 위해서는 data에 값을 담아서 넘긴다.
    //https://firebase.google.com/docs/cloud-messaging/android/receive
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived: 메세지 도착")
        var messageTitle = ""
        var messageContent = ""
        var orderId = -1

        if(remoteMessage.notification != null){ // notification이 있는 경우 foreground처리
            //foreground
            Log.d(TAG, "onMessageReceived: 여기로 오면 안되는데")

            val data = remoteMessage.data
            Log.d(TAG, "data.message: ${data}")
            Log.d(TAG, "data.message: ${data["title"]}")
            Log.d(TAG, "data.message: ${data["body"]}")
            messageTitle = data["title"].toString()
            messageContent = data["body"].toString()
            CoroutineScope(Dispatchers.Main).launch{
               /* val newList = notiList.value?.toList() ?: listOf()
                val newArrayList = arrayListOf<Notification>()
                newArrayList.addAll(newList)
                newArrayList.add(Notification(orderId, messageContent))
                Log.d(TAG, "onMessageReceived: FCM 메세지 수신 : $newArrayList" )
                notiList.value = newArrayList
                sharedPreferencesUtil.saveNotification()*/

            }

        }else{  // background 에 있을경우 혹은 foreground에 있을경우 두 경우 모두

            Log.d(TAG, "onMessageReceived: 여기려 와야 해")
            val data = remoteMessage.data
            Log.d(TAG, "data.message: ${data}")
            Log.d(TAG, "data.message: ${data["title"]}")
            Log.d(TAG, "data.message: ${data["body"]}")
            messageTitle = data["title"].toString()
            messageContent = data["body"].toString()
            CoroutineScope(Dispatchers.Main).launch{
//                val newList = notiList.value?.toList() ?: listOf()
//                val newArrayList = arrayListOf<Notification>()
//                newArrayList.addAll(newList)
//                newArrayList.add(Notification(orderId, messageContent))
//                Log.d(TAG, "onMessageReceived: FCM 메세지 수신 : $newArrayList" )
//                notiList.value = newArrayList
//                sharedPreferencesUtil.saveNotification()

            }
        }

        val mainIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val mainPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder1 = NotificationCompat.Builder(this, "noti")
            .setSmallIcon(R.drawable.sansanmulmul_logo)
            .setContentTitle(messageTitle)
            .setContentText(messageContent)
            .setAutoCancel(true)
            .setContentIntent(mainPendingIntent)

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(101, builder1.build())
    }
}