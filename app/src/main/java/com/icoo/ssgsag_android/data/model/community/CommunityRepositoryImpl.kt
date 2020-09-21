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

    override fun getBoardPost(category: String, curPage: Int, pageSize: Int): Single<BoardPostList>
        = api.getBoardPost(pref.findPreference("TOKEN", "") ,category, curPage, pageSize)
        .map { it.data }

    override fun getBoardPostDetail(communityIdx: Int): Single<BoardPostDetail>
        = api.getPostDetail(pref.findPreference("TOKEN", ""), communityIdx)
        .map { it.data }

    override fun likeCommunityPost(communityIdx: Int): Single<NullDataResponse>
            = api.likeCommunityPost(pref.findPreference("TOKEN", ""), communityIdx)
            .map { it }

    override fun likeCommunityComment(commentIdx: Int): Single<NullDataResponse>
            = api.likeCommunityComment(pref.findPreference("TOKEN", ""), commentIdx)
            .map { it }

    override fun likeCommunityReply(ccommunityIdx: Int): Single<NullDataResponse>
            = api.likeCommunityReply(pref.findPreference("TOKEN", ""), ccommunityIdx)
        .doOnError{
            Log.e("like reply", it.message)
        }
            .map { it }

    override fun unlikeCommunityPost(communityIdx: Int): Single<NullDataResponse>
            = api.unlikeCommunityPost(pref.findPreference("TOKEN", ""), communityIdx)
        .map { it }

    override fun unlikeCommunityComment(commentIdx: Int): Single<NullDataResponse>
            = api.unlikeCommunityComment(pref.findPreference("TOKEN", ""), commentIdx)
        .map { it }

    override fun unlikeCommunityReply(ccommunityIdx: Int): Single<NullDataResponse>
            = api.unlikeCommunityReply(pref.findPreference("TOKEN", ""), ccommunityIdx)
        .doOnError{
            Log.e("unlike reply", it.message)
        }
        .map { it }
}