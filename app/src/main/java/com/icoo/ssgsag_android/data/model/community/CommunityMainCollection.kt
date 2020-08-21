package com.icoo.ssgsag_android.data.model.community


data class CommunityMainCollection(
    val recruitTeamList : ArrayList<RecruitTeamMain>,
    val counselList : ArrayList<BoardMain>,
    val talkList : ArrayList<BoardMain>,
    val reviewList : ArrayList<ReviewMain>
)

data class RecruitTeamMain(
    val idx : Int,
    val title : String,
    val profileImg : String,
    val isRecruiting : Int,
    val area : String,
    val category : String
)

data class BoardMain(
    val idx : Int,
    val category : String,
    val time : String,
    val title : String,
    val description : String,
    val photo : String
)

data class ReviewMain(
    val idx : Int,
    val category: String,
    val name : String,
    val title: String,
    val score : Float,
    val description : String,
    val userName: String,
    val actDuration: String
)