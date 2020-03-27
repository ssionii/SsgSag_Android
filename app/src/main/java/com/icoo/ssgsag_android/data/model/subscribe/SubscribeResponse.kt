package com.icoo.ssgsag_android.data.model.subscribe

data class SubscribeResponse(
    val status: Int,
    val message: String,
    val data: ArrayList<Subscribe>
)