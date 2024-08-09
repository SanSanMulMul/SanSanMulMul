package com.sansantek.sansanmulmul.ui.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.CrewGallery
import com.sansantek.sansanmulmul.data.model.Picture
import com.sansantek.sansanmulmul.databinding.ListGalleryInfoBinding
import com.sansantek.sansanmulmul.ui.viewmodel.GroupDetailViewModel

class GroupDetailTabGalleryInfoListAdapter():
    ListAdapter<CrewGallery, GroupDetailTabGalleryInfoListAdapter.GroupGalleryListHolder>(Comparator) {
    companion object Comparator : DiffUtil.ItemCallback<CrewGallery>() {
        override fun areItemsTheSame(oldItem: CrewGallery, newItem: CrewGallery): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: CrewGallery, newItem: CrewGallery): Boolean {
            return oldItem == newItem
        }
    }

    inner class GroupGalleryListHolder(private val binding: ListGalleryInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(position: Int) {
            val item = getItem(position)

            if(item.imgUrl.isEmpty()){
                val img = ContextCompat.getDrawable(binding.root.context, R.drawable.bg_group_preview)!!.toBitmap()
                binding.ivPicture.setImageBitmap(img)
            }else{
                Glide.with(binding.root).load(item.imgUrl).into(binding.ivPicture)
            }
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