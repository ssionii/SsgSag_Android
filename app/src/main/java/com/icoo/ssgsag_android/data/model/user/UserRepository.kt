package com.icoo.ssgsag_android.data.model.user

import com.icoo.ssgsag_android.data.model.community.board.PostInfo
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.data.model.user.myBoard.MyComment
import com.icoo.ssgsag_android.data.model.user.myBoard.MyPost
import com.icoo.ssgsag_android.data.model.user.userInfo.UserInfo
import com.icoo.ssgsag_android.data.model.user.userNotice.UserNotice
import io.reactivex.Single

interface UserRepository {
    fun getUserInfo(): Single<UserInfo>

    fun getMyPost(curPage : Int, pageSize : Int) : Single<ArrayList<MyPost>>
    fun getMyComment(curPage : Int, pageSize : Int) : Single<ArrayList<MyComment>>
    fun getBookmarkedPost(curPage: Int, pageSize: Int) : Single<ArrayList<PostInfo>>
    fun getBookmarkedFeed(curPage: Int) : Single<ArrayList<Feed>>

    fun getUserNotice(curPage: Int, pageSize: Int) : Single<ArrayList<UserNotice>>
    fun getUserNoticeCount() : Single<Int>
}