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
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.CrewGallery
import com.sansantek.sansanmulmul.data.model.Picture
import com.sansantek.sansanmulmul.databinding.ListPictureViewpagerBinding

private const val TAG = "GroupGalleryViewPagerAdapter_싸피"
class GroupGalleryViewPagerAdapter(
) :
  ListAdapter<CrewGallery, GroupGalleryViewPagerAdapter.PictureViewHolder>(Comparator) {
  companion object Comparator : DiffUtil.ItemCallback<CrewGallery>() {
    override fun areItemsTheSame(oldItem: CrewGallery, newItem: CrewGallery): Boolean {
      return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
    }

    override fun areContentsTheSame(oldItem: CrewGallery, newItem: CrewGallery): Boolean {
      return oldItem == newItem
    }
  }

  inner class PictureViewHolder(val binding: ListPictureViewpagerBinding) : RecyclerView.ViewHolder(binding.root) {
      fun bindInfo(position: Int){
        val item = getItem(position)

        if(item.imgUrl.isEmpty()){
          Log.d(TAG, "bindInfo: imgUrl이 없음")
          val img = ContextCompat.getDrawable(binding.root.context, R.drawable.bg_group_preview)!!.toBitmap()
          binding.ivPicture.setImageBitmap(img)
        }else{
          Log.d(TAG, "bindInfo: imgUrl 세팅 ${item.imgUrl}")
          Glide.with(binding.root).load(item.imgUrl).into(binding.ivPicture)
        }
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

