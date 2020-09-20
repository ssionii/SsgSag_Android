package com.icoo.ssgsag_android.data.model.community.board

import com.icoo.ssgsag_android.data.model.ads.AdItem

data class BoardPostListResponse(
    val status : Int,
    val message : String,
    val data : BoardPostList
)

data class BoardPostList(
    val adList : ArrayList<AdItem>,
    val communityList : ArrayList<PostInfo>
)