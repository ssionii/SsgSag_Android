package com.icoo.ssgsag_android.data.model.feed

import io.reactivex.Single

interface FeedRepository {
    fun getCategoryFeeds(curPage: Int, categoryIdx: Int): Single<ArrayList<Feed>>
    fun getTodayFeeds(curPage : Int, pageSize : Int): Single<FeedMain>
    fun getBookmarkedFeeds(curPage: Int): Single<ArrayList<Feed>>
    fun getFeed(feedIdx: Int): Single<Feed>
    fun getFeedRefresh(feedIdx: Int):Single<Feed>
    fun bookmarkFeed(feedIdx: Int): Single<Int>
    fun unbookmarkFeed(feedIdx: Int): Single<Int>
}