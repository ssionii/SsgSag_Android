package com.icoo.ssgsag_android.data.model.poster.posterDetail.comment

data class Comment(
    val commentIdx: Int,
    val userIdx: Int,
    val userNickname: String,
    val userProfileUrl: String,
    val commentContent: String,
    val commentRegDate: String,
    val likeNum: Int, // 좋아요 개수
    val isLike: Int, // 자신이 좋아요 했으면 1, 안했으면 0
    val isMine: Int // 자신의 댓글이면 1(신고하기, 수정하기, 삭제하기 가능), 아니면 0(신고하기만 가능)
)