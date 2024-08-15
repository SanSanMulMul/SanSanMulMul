package com.sansantek.sansanmulmul.data.model

data class HikingHistory(
  val crewId: Int,
  val recordDistance: Double,
  val recordElevation: Double,
  val recordEndTime: String,
  val recordKcal: Int,
  val recordStartTime: String,
  val recordSteps: Int
)