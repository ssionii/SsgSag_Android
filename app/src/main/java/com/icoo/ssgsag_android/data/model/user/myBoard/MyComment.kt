package com.icoo.ssgsag_android.data.model.user.myBoard

import com.icoo.ssgsag_android.data.model.community.board.PostInfo

data class MyComment(
    val community : PostInfo,
    val type : String,
    val parentIdx : Int,
    val contentIdx : Int,
    val content : String,
    val regDate : String,
    val likeNum : Int,
    val userIdx : Int,
    val isDeleted : Int
)