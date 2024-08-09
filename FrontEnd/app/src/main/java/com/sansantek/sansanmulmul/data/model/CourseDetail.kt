package com.sansantek.sansanmulmul.data.model

data class CourseDetail(
    val courseBestTrackId: Any,
    val courseDowntime: Int,
    val courseId: Long,
    val courseLength: Double,
    val courseLevel: String,
    val courseName: String,
    val courseUptime: Int,
    val tracks: List<Track>
)