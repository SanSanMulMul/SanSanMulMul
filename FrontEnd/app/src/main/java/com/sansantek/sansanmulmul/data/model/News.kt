package com.sansantek.sansanmulmul.data.model

data class NewsResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<News>
)

data class News(
    val title: String,
    val originallink: String,
    val link: String,
    val description: String,
    val pubDate: String
)