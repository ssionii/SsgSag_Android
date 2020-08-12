package com.icoo.ssgsag_android.data.model.login

data class LoginResponse(
    val status: Int,
    val message: String,
    val data: LoginToken?
)