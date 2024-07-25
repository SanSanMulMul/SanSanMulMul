package com.sansantek.sansanmulmul.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.Recommendation

class SecondRecommendationViewPagerAdapter(val items: List<Recommendation>) :
    RecyclerView.Adapter<SecondRecommendationViewPagerAdapter.RecommendationViewHolder>() {

    class RecommendationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMountainName: TextView = view.findViewById(R.id.tv_mountain_name)
        val tvMountainDifficulty: TextView = view.findViewById(R.id.tv_mountain_difficulty)
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
        holder.tvMountainDifficulty.text = item.mountainDifficulty
        holder.ivMountainImg.setImageResource(item.mountainImg)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}

