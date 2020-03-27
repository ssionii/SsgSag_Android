package com.icoo.ssgsag_android.data.model.feed

data class FeedsResponse (
    val status: Int,
    val message: String,
    val data: ArrayList<Feed>?
)