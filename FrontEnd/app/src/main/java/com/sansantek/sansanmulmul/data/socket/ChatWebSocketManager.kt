package com.example.yourapp.network  // 패키지 경로는 프로젝트에 맞게 변경

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatWebSocketListener(private val onMessageReceived: (String) -> Unit) : WebSocketListener() {
    override fun onMessage(webSocket: WebSocket, text: String) {
        onMessageReceived(text)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        // 에러 처리 로직
    }
}

class ChatWebSocketManager {
    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun startWebSocket(url: String, onMessageReceived: (String) -> Unit) {
        val request = Request.Builder().url(url).build()
        val listener = ChatWebSocketListener(onMessageReceived)
        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        scope.launch {
            webSocket.send(message)
        }
    }

    fun closeWebSocket() {
        scope.launch {
            webSocket.close(1000, "Client closed")
        }
        job.cancel()
    }
}
