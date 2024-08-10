package com.sansantek.sansanmulmul.ui.view.groupchat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import java.text.SimpleDateFormat
import java.util.*

class GroupChatAdapter(private val messages: MutableList<Pair<String, Int>>) : RecyclerView.Adapter<GroupChatAdapter.MessageViewHolder>() {

    companion object {
        const val GREEN_BUBBLE = 0
        const val PINK_BUBBLE = 1
    }

    fun updateMessages(newMessages: List<Pair<String, Int>>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.tvMessage.text = message.first

        when (message.second) {
            GREEN_BUBBLE -> holder.tvMessage.setBackgroundResource(R.drawable.message_background_green)
            PINK_BUBBLE -> holder.tvMessage.setBackgroundResource(R.drawable.message_background_pink)
        }

        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        holder.tvTime.text = currentTime
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: String, type: Int) {
        messages.add(Pair(message, type))
        notifyItemInserted(messages.size - 1)
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tv_message)
        val tvTime: TextView = view.findViewById(R.id.tv_time)
    }
}
