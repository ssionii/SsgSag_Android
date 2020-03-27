package com.icoo.ssgsag_android.data.model.feed

data class FeedTodayResponse(
    val status: Int,
    val message: String,
    val data : ArrayList<FeedCategory>?
)