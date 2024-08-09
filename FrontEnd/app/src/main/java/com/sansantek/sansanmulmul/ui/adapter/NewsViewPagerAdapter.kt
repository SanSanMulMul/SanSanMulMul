package com.sansantek.sansanmulmul.ui.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sansantek.sansanmulmul.R
import com.sansantek.sansanmulmul.data.model.News

private const val TAG = "NewsViewPagerAdapter 싸피"

class NewsViewPagerAdapter(private val items: List<News>) :
    RecyclerView.Adapter<NewsViewPagerAdapter.RecommendationViewHolder>() {

    class RecommendationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMountainNAme: TextView = view.findViewById(R.id.tv_mountain_name)
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
        holder.tvNewsTitle.text = item.title
        holder.tvMountainNAme.text = item.mountainName
        Log.d(TAG, "onBindViewHolder: ${item.title}")
        // 이미지 로드
        Glide.with(holder.itemView.context)
            .load(item.mountainImg)
            .into(holder.ivNewsImg)

        // 뉴스 항목 클릭 이벤트
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}

