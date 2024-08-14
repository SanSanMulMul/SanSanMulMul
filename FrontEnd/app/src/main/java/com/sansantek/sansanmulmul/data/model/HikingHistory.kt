package com.sansantek.sansanmulmul.data.model

data class HikingHistory(
  val crewId: Int,
  val recordDistance: Double,
  val recordElevation: Double,
  val recordEndTime: String,
  val recordKcal: Double,
  val recordStartTime: String,
  val recordSteps: Int
)