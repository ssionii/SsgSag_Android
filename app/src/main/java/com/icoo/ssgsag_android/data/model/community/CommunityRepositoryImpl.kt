package com.icoo.ssgsag_android.data.model.community

import android.util.Log
import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.model.base.NullDataResponse
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.data.model.community.board.BoardPostList
import com.icoo.ssgsag_android.data.model.event.EventRepository
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Single

class CommunityRepositoryImpl (val api: NetworkService, val pref: PreferenceManager) : CommunityRepository {

    override fun getCommunityMain(): Single<CommunityMainCollection>
         = api.getCommunityMain(pref.findPreference("TOKEN", ""))
        .map { it.data }

    override fun getBoardPost(category: String, curPage: Int, pageSize: Int): Single<BoardPostList>
        = api.getBoardPost(pref.findPreference("TOKEN", "") ,category, curPage, pageSize)
        .map { it.data }

    override fun writeBoardPost(body: JsonObject): Single<NullDataResponse>
            =api.writeBoardPost("application/json", pref.findPreference("TOKEN", ""), body )
        .map { it }

    override fun editBoardPost(body: JsonObject): Single<NullDataResponse>
            = api.editBoardPost("application/json", pref.findPreference("TOKEN", ""), body)
        .map{ it }

    override fun getBoardPostDetail(communityIdx: Int): Single<BoardPostDetail>
            = api.getPostDetail(pref.findPreference("TOKEN", ""), communityIdx)
        .map { it.data }

    override fun refreshPostDetail(communityIdx: Int): Single<BoardPostDetail>
        = api.refreshPostDetail(pref.findPreference("TOKEN", ""), communityIdx ,1)
        .map{it.data}

    override fun deleteBoardPost(communityIdx: Int): Single<NullDataResponse>
            = api.deleteBoardPost(pref.findPreference("TOKEN", ""), communityIdx)
        .map { it }

    override fun likeCommunityPost(communityIdx: Int): Single<NullDataResponse>
            = api.likeCommunityPost(pref.findPreference("TOKEN", ""), communityIdx)
        .map { it }

    override fun unlikeCommunityPost(communityIdx: Int): Single<NullDataResponse>
            = api.unlikeCommunityPost(pref.findPreference("TOKEN", ""), communityIdx)
        .map { it }

    override fun bookmarkCommunityPost(communityIdx: Int): Single<NullDataResponse>
        = api.bookmarkCommunityPost(pref.findPreference("TOKEN", ""), communityIdx)
        .map{ it }

    override fun unbookmarkCommunityPost(communityIdx: Int): Single<NullDataResponse>
            = api.unbookmarkCommunityPost(pref.findPreference("TOKEN", ""), communityIdx)
        .map{ it }


    override fun writePostComment(body: JsonObject): Single<NullDataResponse>
            = api.writePostComment("application/json", pref.findPreference("TOKEN", ""), body)
        .map { it }

    override fun unlikeCommunityComment(commentIdx: Int): Single<NullDataResponse>
            = api.unlikeCommunityComment(pref.findPreference("TOKEN", ""), commentIdx)
        .map { it }

    override fun deletePostComment(commentIdx: Int): Single<NullDataResponse>
            = api.deletePostComment(pref.findPreference("TOKEN", ""), commentIdx)
        .map { it }

    override fun likeCommunityComment(commentIdx: Int): Single<NullDataResponse>
            = api.likeCommunityComment(pref.findPreference("TOKEN", ""), commentIdx)
        .map { it }



    override fun writePostReply(body: JsonObject): Single<NullDataResponse>
            = api.writePostReply("application/json", pref.findPreference("TOKEN", ""), body)
        .map { it }

    override fun deletePostReply(ccommentIdx: Int): Single<NullDataResponse>
            = api.deletePostReply(pref.findPreference("TOKEN", ""), ccommentIdx)
        .map { it }


    override fun likeCommunityReply(ccommunityIdx: Int): Single<NullDataResponse>
            = api.likeCommunityReply(pref.findPreference("TOKEN", ""), ccommunityIdx)
        .doOnError{
            Log.e("like reply", it.message)
        }
            .map { it }

    override fun unlikeCommunityReply(ccommunityIdx: Int): Single<NullDataResponse>
            = api.unlikeCommunityReply(pref.findPreference("TOKEN", ""), ccommunityIdx)
        .doOnError{
            Log.e("unlike reply", it.message)
        }
        .map { it }
}