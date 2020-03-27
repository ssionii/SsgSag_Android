package com.icoo.ssgsag_android.data.model.feed

data class Feed (
    val feedIdx: Int,
    val feedName: String,
    val feedHost: String,
    val feedRegDate: String,
    val feedUrl: String,
    val feedPreviewImgUrl: String?,
    var isSave:Int,
    val showNum: Int
)