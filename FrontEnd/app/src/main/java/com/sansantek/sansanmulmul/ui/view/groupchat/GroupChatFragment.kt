package com.sansantek.sansanmulmul.ui.view.groupchat

import ChatViewModel
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.data.model.MessageData
import com.sansantek.sansanmulmul.databinding.FragmentGroupChatBinding
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil.Companion.chatService
import com.sansantek.sansanmulmul.ui.view.MainActivity
import com.sansantek.sansanmulmul.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "GroupChatFragment_싸피"
class GroupChatFragment(private val crew: Crew) : BaseFragment<FragmentGroupChatBinding>(
    FragmentGroupChatBinding::bind,
    R.layout.fragment_group_chat
) {
    private lateinit var adapter: GroupChatAdapter
    private lateinit var activity : MainActivity
    private val chatViewModel: ChatViewModel by viewModels()  // ChatViewModel 초기화
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel을 통해 실시간 메시지 관찰
        chatViewModel.chatList.observe(viewLifecycleOwner) {
            adapter.submitList(it){
                binding.rvChatMessages.scrollToPosition(adapter.currentList.size - 1)
            }
        }

        chatViewModel.setCrewId(crew.crewId)
        lifecycleScope.launch(Dispatchers.Main) {
            val result = chatService.loadChatHistory(crew.crewId)
            if(result.isSuccessful){
                chatViewModel.setChatList(result.body()!!.toMutableList())
            }
        }
        chatViewModel.runStomp()

        adapter = GroupChatAdapter(activityViewModel.user.userProviderId)
        binding.rvChatMessages.layoutManager = LinearLayoutManager(context)
        binding.rvChatMessages.adapter = adapter



        binding.btnSend.setOnClickListener {
            val message = binding.etChatInput.text.toString()
            if (message.isNotBlank()) {
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val today = LocalDateTime.now()
                val message = MessageData(binding.etChatInput.text.toString(),today.toString(), crew.crewId, activityViewModel.user.userId)
                chatViewModel.sendMessage(message)
                Log.d(TAG, "onViewCreated: 메세지 송신 ${message}")
                binding.etChatInput.text.clear()
            }
        }

        binding.ibChatBackBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        activity.changeBottomNavigationVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        activity.changeBottomNavigationVisibility(true)
    }

    override fun onDestroy() {
        chatViewModel.stopStomp()
        super.onDestroy()
    }
}
