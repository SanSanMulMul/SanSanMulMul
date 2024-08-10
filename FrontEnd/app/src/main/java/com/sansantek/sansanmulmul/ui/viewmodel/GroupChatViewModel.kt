import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.yourapp.network.ChatWebSocketManager
import com.sansantek.sansanmulmul.ui.view.groupchat.GroupChatAdapter
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val webSocketManager = ChatWebSocketManager()

    val chatMessages = MutableLiveData<List<Pair<String, Int>>>()

    init {
        webSocketManager.startWebSocket("ws://yourserver.com/chat") { message ->
            val updatedMessages = chatMessages.value.orEmpty() + Pair(message, GroupChatAdapter.GREEN_BUBBLE)
            chatMessages.postValue(updatedMessages)
        }
    }

    fun sendMessage(message: String) {
        webSocketManager.sendMessage(message)
    }

    override fun onCleared() {
        super.onCleared()
        webSocketManager.closeWebSocket()
    }
}
