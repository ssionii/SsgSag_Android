package com.icoo.ssgsag_android.data.model.user.myBoard

import com.icoo.ssgsag_android.data.model.community.board.PostInfo

class BookmarkedResponse(
    val stats : Int,
    val message : String,
    val data : ArrayList<PostInfo>
)