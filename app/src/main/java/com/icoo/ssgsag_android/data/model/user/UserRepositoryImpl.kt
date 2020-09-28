package com.icoo.ssgsag_android.data.model.user

import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.model.community.board.PostInfo
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.data.model.user.myBoard.MyComment
import com.icoo.ssgsag_android.data.model.user.userInfo.UserInfo
import com.icoo.ssgsag_android.data.model.user.userNotice.UserNotice
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Single

class UserRepositoryImpl(val api: NetworkService, val pref: PreferenceManager) :
    UserRepository {

    override fun getUserInfo(): Single<UserInfo> = api
        .userInfoResponse(pref.findPreference("TOKEN", ""))
        .map { it.data }

    override fun getMyComment(curPage: Int, pageSize: Int): Single<ArrayList<MyComment>> = api
        .getMyComment(pref.findPreference("TOKEN", ""), curPage, pageSize)
        .map { it.data }

    override fun getBookmarkedPost(curPage: Int, pageSize: Int): Single<ArrayList<PostInfo>>  = api
        .getBookmarkedPost(pref.findPreference("TOKEN", ""), curPage, pageSize)
        .map { it.data }

    override fun getBookmarkedFeed(curPage: Int): Single<ArrayList<Feed>> = api
        .getBookmarkedFeeds(pref.findPreference("TOKEN", ""), 1 , curPage)
        .map { it.data }

    override fun getUserNotice(curPage: Int, pageSize: Int): Single<ArrayList<UserNotice>> = api
        .getUserNotice(pref.findPreference("TOKEN", ""), curPage, pageSize)
        .map { it.data }

    override fun getUserNoticeCount(): Single<Int> = api
        .getUserNoticeCount(pref.findPreference("TOKEN", ""))
        .map { it.data }
}