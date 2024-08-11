import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sansantek.sansanmulmul.data.model.MessageData
import com.sansantek.sansanmulmul.data.model.MessageHistory
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.chatService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.time.LocalDateTime
import kotlin.math.log

private const val TAG = "GroupChatViewModel_싸피"
class ChatViewModel : ViewModel() {
    private var _crewId: Int = -1
    val crewId: Int
        get() = _crewId
    fun setCrewId(crewId: Int){
        _crewId = crewId
    }

    private var _chatList: MutableLiveData<MutableList<MessageHistory>> = MutableLiveData(mutableListOf())
    val chatList: LiveData<MutableList<MessageHistory>>
        get() = _chatList
    fun setChatList(chatList : MutableList<MessageHistory>){
        _chatList.value = chatList
    }


    val url = "wss://i11d111.p.ssafy.io/websocket"// 소켓에 연결하는 엔드포인트가 /socket일때 다음과 같음
    val stompClient =  Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

    fun runStomp(){
        Log.d(TAG, "runStomp: crewId $crewId")
        stompClient.topic("/topic/public/${crewId}").subscribe { topicMessage ->
            Log.d(TAG, "runStomp: ${gson.fromJson(topicMessage.payload, MessageHistory::class.java)}")
            Log.d(TAG, "message Recieve" + topicMessage.payload)
            val newChatList = chatList.value!!.toMutableList().apply {
                add(gson.fromJson(topicMessage.payload, MessageHistory::class.java))
            }
            viewModelScope.launch(Dispatchers.Main) {
                setChatList(newChatList)
            }
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
    }

    private val gson : Gson = GsonBuilder().create()

    fun sendMessage(messageContent: MessageData){
        Log.d(TAG, "sendMessage: ${gson.toJson(messageContent)}")
        stompClient.send("/app/chat.sendMessage", gson.toJson(messageContent).toString()).subscribe()
    }

    fun stopStomp(){
        stompClient.disconnect()
        _crewId = -1
    }
}
