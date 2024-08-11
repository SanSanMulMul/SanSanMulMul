package com.sansantek.sansanmulmul.ui.view.groupchat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.data.model.MessageHistory
import com.sansantek.sansanmulmul.databinding.ItemMessageBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GroupChatAdapter(private val userProviderId: String) : ListAdapter<MessageHistory, GroupChatAdapter.MessageViewHolder>(
    Comparator
) {

    companion object Comparator : DiffUtil.ItemCallback<MessageHistory>() {
        override fun areItemsTheSame(oldItem: MessageHistory, newItem: MessageHistory): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: MessageHistory, newItem: MessageHistory): Boolean {
            return oldItem == newItem
        }
    }

    inner class MessageViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindInfo(position: Int){
            val item = getItem(position)
            if(item.user.userProviderId == userProviderId){
                binding.layoutOther.visibility = View.GONE
                binding.layoutMine.visibility = View.VISIBLE
                binding.tvMessage.text = item.messageContent

                val dateTime = LocalDateTime.parse(item.timestamp)
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val formatterDateTime = dateTime.format(formatter)
                binding.tvTime.text = formatterDateTime
            }else{
                binding.layoutOther.visibility = View.VISIBLE
                binding.layoutMine.visibility = View.GONE
                binding.tvMessageOther.text = item.messageContent
                binding.tvUserNickname.text = item.user.userNickname
                val dateTime = LocalDateTime.parse(item.timestamp)
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val formatterDateTime = dateTime.format(formatter)
                binding.tvTimeOther.text = formatterDateTime
                Glide.with(binding.root).load(item.user.userProfileImg).into(binding.ivProfile)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bindInfo(position)
    }
}
