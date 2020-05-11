package com.icoo.ssgsag_android.data.model.review

data class AdsDataResponse(
    val status: Int,
    val message: String,
    val data: ArrayList<Banner>
)