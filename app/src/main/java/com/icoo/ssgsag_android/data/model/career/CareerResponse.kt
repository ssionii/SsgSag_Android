package com.icoo.ssgsag_android.data.model.career

data class CareerResponse(
    val status: Int,
    val message: String,
    val data: ArrayList<Career>
)