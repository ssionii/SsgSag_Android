package com.icoo.ssgsag_android.data.model.feed

import android.util.Log
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Single

class FeedRepositoryImpl (val api: NetworkService, val pref: PreferenceManager) : FeedRepository {
    override fun getCategoryFeeds(curPage: Int, categoryIdx: Int): Single<ArrayList<Feed>>  = api
        .getCategoryFeeds(pref.findPreference("TOKEN", ""), curPage, categoryIdx)
        .map { it.data }

    override fun getTodayFeeds(): Single<ArrayList<FeedCategory>> = api
        .getTodayFeeds(pref.findPreference("TOKEN", ""))
        .map {it.data}

    override fun getBookmarkedFeeds(curPage: Int): Single<ArrayList<Feed>> = api
        .getBookmarkedFeeds(pref.findPreference("TOKEN", ""), 1, curPage)
        .map { it.data }

    override fun getFeed(feedIdx: Int): Single<Feed> = api
        .getFeed(pref.findPreference("TOKEN", ""), feedIdx)
        .map{ it.data }

    override fun getFeedRefresh(feedIdx: Int): Single<Feed> = api
        .getFeedRefresh(pref.findPreference("TOKEN",""), feedIdx)
        .map { it.data }

    override fun bookmarkFeed(feedIdx: Int): Single<Int> = api
        .bookmarkFeed(pref.findPreference("TOKEN", ""), feedIdx)
        .map { it.status }

    override fun unbookmarkFeed(feedIdx: Int): Single<Int> = api
        .unbookmarkFeed(pref.findPreference("TOKEN", ""), feedIdx)
        .map { it.status }
}
