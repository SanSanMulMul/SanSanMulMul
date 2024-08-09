package com.sansantek.sansanmulmul.data.model

data class Crew(val crewId: Int,
                val crewName: String,
                val mountainName: String,
                var crewStartDate: String,
                val crewEndDate: String,
                val crewMaxMembers: Int,
                val crewCurrentMembers: Int,
                val mountainImg: String,
                val crewMinAge: Int,
                val crewMaxAge: Int,
                val crewGender: String,
                val crewStyles: List<Int>,
                val userStaticBadge: Int,
                val userName: String,
                val userNickname: String,
                val userProfileImg: String,
                val userJoined: Boolean)
