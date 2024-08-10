package com.sansantek.sansanmulmul.ui.view.groupchat

import ChatViewModel
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.BaseFragment
import com.sansantek.sansanmulmul.databinding.FragmentGroupChatBinding
import com.sansantek.sansanmulmul.ui.view.MainActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

class GroupChatFragment : BaseFragment<FragmentGroupChatBinding>(
    FragmentGroupChatBinding::bind,
    R.layout.fragment_group_chat
) {

    private lateinit var adapter: GroupChatAdapter
    private lateinit var activity : MainActivity
    private val chatViewModel: ChatViewModel by viewModels()  // ChatViewModel 초기화

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GroupChatAdapter(mutableListOf())
        binding.rvChatMessages.layoutManager = LinearLayoutManager(context)
        binding.rvChatMessages.adapter = adapter

        // ViewModel을 통해 실시간 메시지 관찰
        chatViewModel.chatMessages.observe(viewLifecycleOwner, Observer { messages ->
            adapter.updateMessages(messages)
            binding.rvChatMessages.scrollToPosition(adapter.itemCount - 1)
        })

        binding.btnSend.setOnClickListener {
            val message = binding.etChatInput.text.toString()
            if (message.isNotBlank()) {
                chatViewModel.sendMessage(message)  // WebSocket을 통해 메시지 전송
                binding.etChatInput.text.clear()
                binding.rvChatMessages.scrollToPosition(adapter.itemCount - 1)
            }
        }

        // 기존의 버튼 클릭 리스너도 ViewModel을 통해 메시지 전송
        binding.btnGpt1.setOnClickListener {
            val message = binding.btnGpt1.text.toString()
            chatViewModel.sendMessage(message)
            binding.rvChatMessages.scrollToPosition(adapter.itemCount - 1)
        }

        binding.btnGpt2.setOnClickListener {
            val message = binding.btnGpt2.text.toString()
            chatViewModel.sendMessage(message)
            binding.rvChatMessages.scrollToPosition(adapter.itemCount - 1)
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
}
