package com.sansantek.sansanmulmul.data.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

data class MessageData(val messageContent: String, val timestamp: String,  val crewId: Int, val userId: Int )
