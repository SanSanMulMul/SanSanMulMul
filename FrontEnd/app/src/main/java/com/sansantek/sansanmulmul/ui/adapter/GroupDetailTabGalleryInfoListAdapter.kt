package com.sansantek.sansanmulmul.ui.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.Picture
import com.sansantek.sansanmulmul.databinding.ListGalleryInfoBinding
import com.sansantek.sansanmulmul.ui.util.Util

class GroupDetailTabGalleryInfoListAdapter():
    ListAdapter<Picture, GroupDetailTabGalleryInfoListAdapter.GroupGalleryListHolder>(Comparator) {

    companion object Comparator : DiffUtil.ItemCallback<Picture>() {
        override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
            return oldItem == newItem
        }
    }

    inner class GroupGalleryListHolder(private val binding: ListGalleryInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)

            val img = if(item.uri == null){
                ContextCompat.getDrawable(binding.root.context, R.drawable.bg_group_preview)!!.toBitmap()
            }else{
                binding.root.context.contentResolver.openInputStream(item.uri!!)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            }
            binding.ivPicture.setImageBitmap(img)
            binding.ivPicture.setOnClickListener {
                itemClickListener.onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupGalleryListHolder {
        return GroupGalleryListHolder(
            ListGalleryInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GroupGalleryListHolder, position: Int) {
        holder.bindInfo(position)
    }

    interface ItemClickListener {
        fun onClick(position: Int)
    }

    private lateinit var itemClickListener: ItemClickListener


    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}