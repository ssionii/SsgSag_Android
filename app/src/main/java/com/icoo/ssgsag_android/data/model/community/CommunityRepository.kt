package com.icoo.ssgsag_android.data.model.community

import com.icoo.ssgsag_android.data.model.community.board.BoardPostList
import io.reactivex.Single

interface CommunityRepository {
    fun getBoardPost(category : String, curPage : Int, pageSize : Int) : Single<BoardPostList>
}