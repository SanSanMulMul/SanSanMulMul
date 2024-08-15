package com.sansantek.sansanmulmul.data.model

data class MountainHistoryDetail(val mountainName: String, val mountainImg: String, val upCourseName: String, val downCourseName: String, val courseLength: Double, val upCourseTrackPaths: List<Track>, val downCourseTrackPaths: List<Track>, val crewId: Int, val crewMembers: List<GroupUser>,val recordStartTime: String, val recordEndTime: String, val recordDistance: Int, val recordDuration: Long, val recordSteps: Int, val recordElevation: Double, val recordKcal: Double)
