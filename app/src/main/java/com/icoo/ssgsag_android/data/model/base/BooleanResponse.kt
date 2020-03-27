package com.icoo.ssgsag_android.data.model.base

data class BooleanResponse(
    val status: Int,
    val message: String,
    val data: Boolean?
)