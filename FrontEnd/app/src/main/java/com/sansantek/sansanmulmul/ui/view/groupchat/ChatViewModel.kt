package com.sansantek.sansanmulmul.ui.view.groupchat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.sansantek.sansanmulmul.data.model.MessageData
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompCommand.CONNECT
import ua.naiksoftware.stomp.dto.StompHeader
import java.time.LocalDateTime
import java.util.Date

private const val TAG = "ChatViewModel_싸피"
class ChatViewModel : ViewModel() {
    // val url = "http://example.com:8080/"

    val url = "wss://i11d111.p.ssafy.io/websocket"// 소켓에 연결하는 엔드포인트가 /socket일때 다음과 같음
    val stompClient =  Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

    fun runStomp(){
        
        stompClient.topic("/topic/public").subscribe { topicMessage ->
            Log.d(TAG, "message Recieve" + topicMessage.payload)
        }
        Log.d(TAG, "runStomp: 연결 시도")
        stompClient.connect()

        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.d(TAG, "\"OPEND\"!!")
                }
                LifecycleEvent.Type.CLOSED -> {
                    Log.d(TAG, "CLOSED!!")

                }
                LifecycleEvent.Type.ERROR -> {
                    Log.d(TAG, "ERROR!!")
                    Log.d(TAG, "CONNECT ERROR" + lifecycleEvent.exception.toString())
                }
                else ->{
                    Log.d(TAG, "ELSE " + lifecycleEvent.message)
                }
            }
        }

//        val data = JSONObject()
//        data.put("userKey", text.value)
//        data.put("positionType", "1")
//        data.put("content", "test")
//        data.put("messageType", "CHAT")
//        data.put("destRoomCode", "test0912")

//        stompClient.send("/stream/chat/send", data.toString()).subscribe()
    }
}