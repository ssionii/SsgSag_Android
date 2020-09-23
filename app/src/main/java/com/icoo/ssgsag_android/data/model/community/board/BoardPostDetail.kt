package com.icoo.ssgsag_android.data.model.community.board

data class BoardPostDetailResponse(
    val status : Int,
    val message : String,
    val data : BoardPostDetail
)

data class BoardPostDetail(
    val community : PostInfo,
    val userNickname : String,
    val userProfileUrl : String,
    val communityCommentList : ArrayList<PostComment>,
    val save : Boolean,
    val like : Boolean,
    val mine: Boolean
)

data class PostInfo(
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
    val isBest : Int
)

data class PostComment(
    val commentIdx : Int,
    val ccommentIdx : Int,
    val content : String,
    val regDate : String,
    var likeNum : Int,
    val userIdx : Int,
    val communityIdx : Int,
    val isDelete : Int,
    val userNickname: String,
    val userProfileUrl: String,
    var communityCCommentList : ArrayList<PostComment>?,
    val commentName : String?,
    var like : Boolean,
    val mine: Boolean,
    var type : Int,
    var isSelected : Boolean

)