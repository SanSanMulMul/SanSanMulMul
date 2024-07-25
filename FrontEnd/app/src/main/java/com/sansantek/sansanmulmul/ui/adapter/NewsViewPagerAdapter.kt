package com.sansantek.sansanmulmul.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.News

class NewsViewPagerAdapter(val items: List<News>) :
    RecyclerView.Adapter<NewsViewPagerAdapter.RecommendationViewHolder>() {

    class RecommendationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNewsTitle: TextView = view.findViewById(R.id.tv_news_title)
        val ivNewsImg: ImageView = view.findViewById(R.id.iv_news_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_news, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val actualPosition = position % items.size
//        holder.bind(items[actualPosition])
        val item = items[actualPosition]
        holder.tvNewsTitle.text = item.newsTitle
        holder.ivNewsImg.setImageResource(item.newsImg)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}

