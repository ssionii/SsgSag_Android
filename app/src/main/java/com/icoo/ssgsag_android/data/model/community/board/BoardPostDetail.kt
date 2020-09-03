package com.icoo.ssgsag_android.data.model.community.board

data class BoardPostDetail(
    val communityIdx : Int,
    val category : String,
    val userIdx : Int,
    val title : String,
    val content : String,
    val photoUrlList : String?,
    val regDate : String,
    val likeNum : Int?,
    val commentNum : Int,
    val showNum : Int,
    val isBest : Int,
    val userNickname : String
)