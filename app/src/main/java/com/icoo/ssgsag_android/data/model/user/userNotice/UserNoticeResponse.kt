package com.icoo.ssgsag_android.data.model.user.userNotice

data class UserNoticeResponse(

    val status : Int,
    val message : String,
    val data : ArrayList<UserNotice>
)

data class UserNotice(
    val notifyIdx : Int,
    val category : String,
    val contentIdx : Int,
    val content : String,
    val regDate : String,
    val userIdx : Int,
    val isShowed : Int
)