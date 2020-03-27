package com.icoo.ssgsag_android.data.model.review.club.response

data class PostReviewResponse(
    val status: Int,
    val message: String,
    val data: PostReviewData
)

data class PostReviewData(
    val clubIdx: Int,
    val event: Boolean
)