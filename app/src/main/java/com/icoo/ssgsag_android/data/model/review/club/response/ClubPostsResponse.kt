package com.icoo.ssgsag_android.data.model.review.club.response

import com.icoo.ssgsag_android.data.model.review.club.ClubPost

data class ClubPostsResponse(
    val status: Int,
    val message: String,
    val data : ArrayList<ClubPost>
)