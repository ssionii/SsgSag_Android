package com.icoo.ssgsag_android.data.model.review.club

import java.io.Serializable

data class ClubPost(
    val clubPostIdx: Int,
    val clubStartDate: String,
    val clubEndDate: String,
    val score0: Float,
    val score1: Float,
    val score2: Float,
    val score3: Float,
    val score4: Float,
    val oneLine: String,
    val advantage: String,
    val disadvantage: String,
    val honeyTip: String?,
    val regDate: String,
    val userIdx: Int,
    val isMine :Int,
    val isLike: Int,
    val likeNum: Int,
    val userNickname: String,
    val adminAccept: Int,
    val fieldName: String,

    var clubIdx: Int?,
    var clubName: String?,
    var clubType: Int?,
    var univOrLocation: String?
) : Serializable