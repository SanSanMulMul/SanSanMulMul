package com.sansantek.sansanmulmul.ui.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.Picture
import com.sansantek.sansanmulmul.databinding.ListPictureViewpagerBinding
import com.sansantek.sansanmulmul.ui.util.Util

private const val TAG = "GroupGalleryViewPagerAdapter_싸피"
class GroupGalleryViewPagerAdapter(
) :
  ListAdapter<Picture, GroupGalleryViewPagerAdapter.PictureViewHolder>(Comparator) {
  companion object Comparator : DiffUtil.ItemCallback<Picture>() {
    override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
      return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
    }

    override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
      return oldItem == newItem
    }
  }

  inner class PictureViewHolder(val binding: ListPictureViewpagerBinding) : RecyclerView.ViewHolder(binding.root) {
      fun bindInfo(position: Int){
        val item = getItem(position)

        val img = if(item.uri == null){
          ContextCompat.getDrawable(binding.root.context, R.drawable.bg_group_preview)!!.toBitmap()
        }else{
          binding.root.context.contentResolver.openInputStream(item.uri!!)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
          }
        }
        binding.ivPicture.setImageBitmap(img)
      }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
    Log.d(TAG, "onCreateViewHolder: 사진 생성")
    return PictureViewHolder(ListPictureViewpagerBinding.inflate(
      LayoutInflater.from(parent.context),parent, false)
    )
  }

  override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
    holder.bindInfo(position)
  }

}

