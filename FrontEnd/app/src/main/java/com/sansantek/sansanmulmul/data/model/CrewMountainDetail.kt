package com.sansantek.sansanmulmul.data.model

data class CrewMountainDetail(
    val downCourseId: Long,
    val downCourseLength: Double,
    val downCourseLevel: String,
    val downCourseName: String,
    val downCourseTrackPaths: List<DownCourseTrackPath>,
    val downCoursetime: Int,
    val mountainDescription: String,
    val mountainId: Int,
    val mountainLat: Double,
    val mountainLon: Double,
    val mountainName: String,
    val upCourseId: Long,
    val upCourseLength: Double,
    val upCourseLevel: String,
    val upCourseName: String,
    val upCourseTrackPaths: List<UpCourseTrackPath>,
    val upCoursetime: Int
)