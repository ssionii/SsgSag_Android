package com.icoo.ssgsag_android.data.model.community

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
}