package com.sansantek.sansanmulmul.ui.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.config.Const.Companion.TITLE
import com.sansantek.sansanmulmul.data.model.GroupDetailFirstData
import com.sansantek.sansanmulmul.data.model.GroupUser
import com.sansantek.sansanmulmul.data.model.Member
import com.sansantek.sansanmulmul.databinding.ListGroupDetailFirstTabMemberListBinding
import com.sansantek.sansanmulmul.ui.util.Util
import com.sansantek.sansanmulmul.ui.view.groupdetail.GroupDetailTabFirstInfoFragment
import com.sansantek.sansanmulmul.ui.view.groupdetail.GroupMemberDetailPageFragment
import kotlinx.coroutines.runBlocking

private const val TAG = "GroupDetailFirstTabMemberListAdapter_싸피"
class GroupDetailFirstTabMemberListAdapter(private var amILeader: Boolean, private val myId: Int, private val fragmentManager: FragmentManager):
    ListAdapter<GroupUser, GroupDetailFirstTabMemberListAdapter.MemberInfoListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<GroupUser>() {
        override fun areItemsTheSame(oldItem: GroupUser, newItem: GroupUser): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: GroupUser, newItem: GroupUser): Boolean {
            return oldItem == newItem
        }
    }

    inner class MemberInfoListHolder(private val binding: ListGroupDetailFirstTabMemberListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            if (!amILeader || myId == item.userId){
                binding.btnDelegateAdmin.visibility = View.GONE
                binding.btnKickMember.visibility = View.GONE
            }
            if(item.leader){
                binding.ivLeader.visibility = View.VISIBLE
            }else{
                binding.ivLeader.visibility = View.GONE
            }
            //}
            Log.d(TAG, "bindInfo: $item")
            Glide.with(binding.root).load(item.userProfileImg).into(binding.ivMemberImage)
            binding.memberTitle.text = TITLE[item.userStaticBadge]
            binding.memberName.text = item.userNickname

            // 클릭 리스너 설정
            binding.root.setOnClickListener {
                runBlocking {
                    itemClickListener.onMemberClick(item)
                }
            }

            binding.btnDelegateAdmin.setOnClickListener {
                runBlocking {
                    val result = itemClickListener.onLeaderDelegateClick(item)
                    if(result){
                        amILeader = false
                        refreshList()
                    }
                }
            }
            binding.btnKickMember.setOnClickListener {
                runBlocking {
                    itemClickListener.onKickClick(item)
                    refreshList()
                }
            }
        }
    }

    fun refreshList(){
        val newList = currentList.toMutableList()
        submitList(null)
        submitList(newList)
    }

    fun setAmILeader(amILeader: Boolean){
        this.amILeader = amILeader
        Log.d(TAG, "setAmILeader: ${this.amILeader}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberInfoListHolder {
        return MemberInfoListHolder(
            ListGroupDetailFirstTabMemberListBinding.inflate(
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
        suspend fun onLeaderDelegateClick(user: GroupUser): Boolean
        suspend fun onKickClick(user: GroupUser)
        suspend fun onMemberClick(user: GroupUser)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}