package com.sansantek.sansanmulmul.ui.view.groupchat

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

class GroupChatFragment : BaseFragment<FragmentGroupChatBinding>(
    FragmentGroupChatBinding::bind,
    R.layout.fragment_group_chat
) {

    private lateinit var adapter: GroupChatAdapter
    private lateinit var activity : MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GroupChatAdapter(mutableListOf())
        binding.rvChatMessages.layoutManager = LinearLayoutManager(context)
        binding.rvChatMessages.adapter = adapter

        binding.btnSend.setOnClickListener {
            val message = binding.etChatInput.text.toString()
            if (message.isNotBlank()) {
                adapter.addMessage(message, GroupChatAdapter.GREEN_BUBBLE)
                binding.etChatInput.text.clear()
                binding.rvChatMessages.scrollToPosition(adapter.itemCount - 1)
            }
        }

        binding.btnGpt1.setOnClickListener {
            val message = binding.btnGpt1.text.toString()
            adapter.addMessage(message, GroupChatAdapter.PINK_BUBBLE)
            binding.rvChatMessages.scrollToPosition(adapter.itemCount - 1)
        }

        binding.btnGpt2.setOnClickListener {
            val message = binding.btnGpt2.text.toString()
            adapter.addMessage(message, GroupChatAdapter.PINK_BUBBLE)
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
