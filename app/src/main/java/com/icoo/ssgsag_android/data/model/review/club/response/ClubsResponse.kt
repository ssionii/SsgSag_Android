package com.icoo.ssgsag_android.data.model.review.club.response

import com.icoo.ssgsag_android.data.model.review.club.ClubInfo

data class ClubsResponse(
    val status: Int,
    val message: String,
    val data: ArrayList<ClubInfo>
)