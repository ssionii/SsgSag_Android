package com.icoo.ssgsag_android.data.model.community

import com.icoo.ssgsag_android.data.model.base.NullDataResponse
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.data.model.community.board.BoardPostList
import io.reactivex.Single

interface CommunityRepository {
    fun getBoardPost(category : String, curPage : Int, pageSize : Int) : Single<BoardPostList>
    fun getBoardPostDetail(communityIdx : Int) : Single<BoardPostDetail>

    fun likeCommunityPost(communityIdx : Int) : Single<NullDataResponse>
    fun likeCommunityComment(commentIdx : Int) : Single<NullDataResponse>
    fun likeCommunityReply(ccommunityIdx : Int) : Single<NullDataResponse>
    fun unlikeCommunityPost(communityIdx : Int) : Single<NullDataResponse>
    fun unlikeCommunityComment(commentIdx : Int) : Single<NullDataResponse>
    fun unlikeCommunityReply(ccommunityIdx : Int) : Single<NullDataResponse>
}