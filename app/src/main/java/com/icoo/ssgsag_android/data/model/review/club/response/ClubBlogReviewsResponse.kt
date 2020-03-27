package com.icoo.ssgsag_android.data.model.review.club.response

import com.icoo.ssgsag_android.data.model.review.club.BlogReview

data class ClubBlogReviewsResponse(
    val status: Int,
    val message: String,
    val data : ArrayList<BlogReview>
)