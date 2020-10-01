package com.icoo.ssgsag_android.data.model.user.myBoard

data class MyCommentResponse(
    val status : Int,
    val message : String,
    val data : ArrayList<MyComment>
)