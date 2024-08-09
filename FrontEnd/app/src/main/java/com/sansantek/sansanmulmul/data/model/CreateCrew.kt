package com.sansantek.sansanmulmul.data.model

data class CreateCrew(val crewName: String,
                      val crewDescription: String,
                      val crewMaxMembers: Int,
                      val crewGender: String,
                      val crewMinAge: Int,
                      val crewMaxAge: Int,
                      val crewStartDate: String,
                      val crewEndDate: String,
                      val crewStyles: List<Int>,
                      val mountainId: Int,
                      val upCourseId: Long,
                      val downCourseId: Long)