package com.icoo.ssgsag_android.data.model.community

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.model.base.NullDataResponse
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.data.model.community.board.BoardPostList
import io.reactivex.Single
import okhttp3.MultipartBody

interface CommunityRepository {

    fun getCommunityMain() : Single<CommunityMainCollection>

    fun getBoardPost(category : String, curPage : Int, pageSize : Int) : Single<BoardPostList>
    fun writeBoardPost(body: JsonObject) : Single<NullDataResponse>
    fun editBoardPost(body: JsonObject) : Single<NullDataResponse>

    fun getBoardPostDetail(communityIdx : Int) : Single<BoardPostDetail>
    fun refreshPostDetail(communityIdx: Int) : Single<BoardPostDetail>
    fun deleteBoardPost(communityIdx : Int) : Single<NullDataResponse>
    fun likeCommunityPost(communityIdx : Int) : Single<NullDataResponse>
    fun unlikeCommunityPost(communityIdx : Int) : Single<NullDataResponse>
    fun bookmarkCommunityPost(communityIdx: Int) : Single<NullDataResponse>
    fun unbookmarkCommunityPost(communityIdx: Int) : Single<NullDataResponse>

    fun writePostComment(body: JsonObject) : Single<NullDataResponse>
    fun deletePostComment(commentIdx : Int) : Single<NullDataResponse>
    fun likeCommunityComment(commentIdx : Int) : Single<NullDataResponse>
    fun unlikeCommunityComment(commentIdx : Int) : Single<NullDataResponse>

    fun writePostReply(body: JsonObject) : Single<NullDataResponse>
    fun deletePostReply(ccommentIdx : Int) : Single<NullDataResponse>
    fun likeCommunityReply(ccommunityIdx : Int) : Single<NullDataResponse>
    fun unlikeCommunityReply(ccommunityIdx : Int) : Single<NullDataResponse>

    fun getPhotoUrl(data: MultipartBody.Part) : Single<String>
}