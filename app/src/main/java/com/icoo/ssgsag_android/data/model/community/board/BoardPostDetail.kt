package com.icoo.ssgsag_android.data.model.community.board

data class BoardPostDetail(
    val boardIndex : Int,
    val category : String,
    val type : Int,
    val title : String,
    val description : String,
    val viewCount : Int,
    val commentCount : Int,
    val time : String,
    val profileUrl : String?,
    val isBest : Int,
    val userName : String?,
    val photoUrl : String?,
    val likeNum : Int?
    )