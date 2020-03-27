package com.icoo.ssgsag_android.data.model.signUp
import com.icoo.ssgsag_android.data.model.login.LoginToken

data class SignUpResponse(
    val status : Int,
    val message : String,
    var data: LoginToken?
)