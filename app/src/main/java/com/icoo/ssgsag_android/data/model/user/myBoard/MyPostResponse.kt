package com.icoo.ssgsag_android.data.model.user.myBoard

data class MyPostResponse (
    val status : Int,
    val message : String,
    val data : ArrayList<MyPost>
)

data class MyPost(
    val category1 : String,
    val category2 : String,
    val title : String,
    val regDate : String,
    val contentIdx : Int
)

