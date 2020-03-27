package com.icoo.ssgsag_android.data.model.schedule

data class ScheduleResponse(
    val status: Int,
    val message: String,
    val data: ArrayList<Schedule>
)