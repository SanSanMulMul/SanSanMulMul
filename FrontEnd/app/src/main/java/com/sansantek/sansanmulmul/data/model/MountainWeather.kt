package com.sansantek.sansanmulmul.data.model

data class MountainWeather(
  val dayOfMonth: Int,
  val dayOfWeek: String,
  val description: String,
  val feelsLike: Double,
  val humidity: Int,
  val max: Double,
  val min: Double,
  val pop: Double
)