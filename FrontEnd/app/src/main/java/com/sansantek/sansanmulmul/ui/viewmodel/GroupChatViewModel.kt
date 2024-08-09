import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sansantek.sansanmulmul.ui.view.groupchat.GroupChatAdapter
import kotlinx.coroutines.*
import java.net.Socket

class GroupChatViewModel : ViewModel() {

    private val _messages = MutableLiveData<Pair<String, Int>>()
    val messages: LiveData<Pair<String, Int>> get() = _messages

    private val socket = Socket("서버 IP", 12345)

    init {
        receiveMessages()
    }

    private fun receiveMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            val reader = socket.getInputStream().bufferedReader()
            while (true) {
                val message = reader.readLine() ?: break
                _messages.postValue(Pair(message, GroupChatAdapter.GREEN_BUBBLE)) // 서버 메시지는 GREEN_BUBBLE로 설정
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        socket.close()
    }
}
