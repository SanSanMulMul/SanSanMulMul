package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.Member
import com.sansantek.sansanmulmul.databinding.ListGroupDetailFirstTabMemberListBinding
import com.sansantek.sansanmulmul.ui.util.Util

private const val TAG = "GroupDetailFirstTabMemberListAdapter_싸피"
class GroupDetailFirstTabMemberListAdapter():
    ListAdapter<Member, GroupDetailFirstTabMemberListAdapter.MemberInfoListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<Member>() {
        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem == newItem
        }
    }

    inner class MemberInfoListHolder(private val binding: ListGroupDetailFirstTabMemberListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            if(item.registration){
                binding.btnDelegateAdmin.visibility = View.GONE
                binding.btnKickMember.visibility = View.GONE
            }
            else{
                binding.btnDecline.visibility = View.GONE
                binding.btnApprove.visibility = View.GONE
            }
            Log.d(TAG, "bindInfo: $item")
            val img = if(item.imageByte == null){
                ContextCompat.getDrawable(binding.root.context, R.drawable.leader_picture)!!.toBitmap()
            }else{
                Util.byteArrayToBitmap(item.imageByte!!)
            }

            binding.ivMemberImage.setImageBitmap(img)

//            Glide.with(itemView)
//                .load("${ApplicationClass.MENU_IMGS_URL}${item.menuImg}")
//                .into(binding.groupImage)

            binding.memberTitle.text = item.title
            binding.memberName.text = item.name
        }
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
        fun onClick(position: Int) {
        }
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}