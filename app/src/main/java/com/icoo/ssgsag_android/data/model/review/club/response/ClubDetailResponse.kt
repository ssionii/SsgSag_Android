package com.icoo.ssgsag_android.data.model.review.club.response

import com.icoo.ssgsag_android.data.model.review.club.ClubInfo

data class ClubDetailResponse(
    val status: Int,
    val message: String,
    val data: ClubInfo
)