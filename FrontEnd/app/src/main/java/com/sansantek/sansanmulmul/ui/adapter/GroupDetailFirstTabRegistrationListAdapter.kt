package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.config.Const
import com.sansantek.sansanmulmul.data.model.GroupUser
import com.sansantek.sansanmulmul.data.model.RequestMember
import com.sansantek.sansanmulmul.databinding.ListGroupDetailFirstTabMemberListBinding
import com.sansantek.sansanmulmul.databinding.ListGroupDetailFirstTabRegistrationListBinding
import kotlinx.coroutines.runBlocking

private const val TAG = "GroupDetailFirstTabRegi 싸피"
class GroupDetailFirstTabRegistrationListAdapter():
    ListAdapter<RequestMember, GroupDetailFirstTabRegistrationListAdapter.MemberInfoListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<RequestMember>() {
        override fun areItemsTheSame(oldItem: RequestMember, newItem: RequestMember): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: RequestMember, newItem: RequestMember): Boolean {
            return oldItem == newItem
        }
    }

    inner class MemberInfoListHolder(private val binding: ListGroupDetailFirstTabRegistrationListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            Log.d(TAG, "bindInfo: $item")
            Glide.with(binding.root).load(item.userProfileImg).into(binding.ivMemberImage)
            binding.memberTitle.text = Const.TITLE[item.userStaticBadge]
            binding.memberName.text = item.userNickname

            binding.btnApprove.setOnClickListener {
                runBlocking {
                    itemClickListener.onApproveClick(item)
                    refreshList()
                }
            }
            binding.btnDecline.setOnClickListener {
                runBlocking {
                    itemClickListener.onDeclineClick(item)
                    refreshList()
                }
            }
        }
    }

    fun refreshList(){
        submitList(currentList.toList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberInfoListHolder {
        return MemberInfoListHolder(
            ListGroupDetailFirstTabRegistrationListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MemberInfoListHolder, position: Int) {
        holder.bindInfo(position)
    }


    interface ItemClickListener {
        suspend fun onDeclineClick(user: RequestMember): Boolean
        suspend fun onApproveClick(user: RequestMember) : Boolean
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}