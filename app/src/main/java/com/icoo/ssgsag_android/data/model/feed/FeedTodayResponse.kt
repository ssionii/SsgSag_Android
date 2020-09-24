package com.icoo.ssgsag_android.data.model.feed

data class FeedTodayResponse(
    val status: Int,
    val message: String,
    val data : FeedMain
)

data class FeedMain(
    val bestFeedList : ArrayList<Feed>?,
    val feedList : ArrayList<Feed>?
)