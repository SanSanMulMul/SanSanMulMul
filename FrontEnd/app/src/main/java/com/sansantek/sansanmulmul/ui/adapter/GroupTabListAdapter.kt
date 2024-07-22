package com.sansantek.sansanmulmul.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.GroupListInfo
import com.sansantek.sansanmulmul.databinding.ListGroupBinding
import com.sansantek.sansanmulmul.ui.util.Util.byteArrayToBitmap

private const val TAG = "GroupTabListAdapter_싸피"
class GroupTabListAdapter():
    ListAdapter<GroupListInfo, GroupTabListAdapter.GroupInfoListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<GroupListInfo>() {
        override fun areItemsTheSame(oldItem: GroupListInfo, newItem: GroupListInfo): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: GroupListInfo, newItem: GroupListInfo): Boolean {
            return oldItem == newItem
        }
    }

    inner class GroupInfoListHolder(private val binding: ListGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)
            Log.d(TAG, "bindInfo: $item")
            val img = if(item.imageByte == null){
                ContextCompat.getDrawable(binding.root.context, R.drawable.signup_finish_tiger)!!.toBitmap()
            }else{
                byteArrayToBitmap(item.imageByte!!)
            }

            binding.groupImage.setImageBitmap(img)

//            Glide.with(itemView)
//                .load("${ApplicationClass.MENU_IMGS_URL}${item.menuImg}")
//                .into(binding.groupImage)

            binding.groupTitle.text = item.title
            binding.groupSchedule.text = "24.07.15 13:00 - 24.07.15 14:00"
            binding.groupPersonInfo.text = "3 / 10명"

//            binding.btnDelete.setOnClickListener {
//                itemClickListener.onDelete(position)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupInfoListHolder {
        return GroupInfoListHolder(
            ListGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupInfoListHolder, position: Int) {
        holder.bindInfo(position)
    }


    interface ItemClickListener {
        fun onDelete(position: Int) {
        }
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}