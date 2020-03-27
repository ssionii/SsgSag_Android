package com.icoo.ssgsag_android.data.model.notice

data class NoticeResponse(
    val status: Int,
    val message: String,
    val data: ArrayList<Notice>
)