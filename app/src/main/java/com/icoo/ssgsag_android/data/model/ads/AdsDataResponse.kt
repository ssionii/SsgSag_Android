package com.icoo.ssgsag_android.data.model.ads


data class AdsDataResponse(
    val status: Int,
    val message: String,
    val data: ArrayList<AdItem>
)