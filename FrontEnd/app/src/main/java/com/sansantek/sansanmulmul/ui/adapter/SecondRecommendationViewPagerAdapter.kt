package com.sansantek.sansanmulmul.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.Recommendation

class SecondRecommendationViewPagerAdapter(
  val items: List<Recommendation>,
  private val listener: OnItemClickListener
) :
  RecyclerView.Adapter<SecondRecommendationViewPagerAdapter.RecommendationViewHolder>() {

  interface OnItemClickListener {
    fun onItemClick(item: Recommendation)
  }

  class RecommendationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvMountainName: TextView = view.findViewById(R.id.tv_mountain_name)
    val tvMountainHeight: TextView = view.findViewById(R.id.tv_mountain_height)
    val ivMountainImg: ImageView = view.findViewById(R.id.iv_mountain_img)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
    val view =
      LayoutInflater.from(parent.context)
        .inflate(R.layout.item_recommendation, parent, false)
    return RecommendationViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
    val actualPosition = position % items.size
    val item = items[actualPosition]
    holder.tvMountainName.text = item.mountainName
    holder.tvMountainHeight.text = item.mountainHeight.toString()
    Glide.with(holder.itemView.context)
      .load(item.mountainImg)
      .into(holder.ivMountainImg)

    holder.itemView.setOnClickListener {
      listener.onItemClick(item)
    }
  }

  override fun getItemCount(): Int = Int.MAX_VALUE
}

