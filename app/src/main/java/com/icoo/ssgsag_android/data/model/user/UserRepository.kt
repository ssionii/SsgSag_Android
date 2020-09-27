package com.icoo.ssgsag_android.data.model.user

import com.icoo.ssgsag_android.data.model.community.board.PostInfo
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.data.model.user.myBoard.MyComment
import com.icoo.ssgsag_android.data.model.user.userInfo.UserInfo
import io.reactivex.Single

interface UserRepository {
    fun getUserInfo(): Single<UserInfo>

    fun getMyComment(curPage : Int, pageSize : Int) : Single<ArrayList<MyComment>>
    fun getBookmarkedPost(curPage: Int, pageSize: Int) : Single<ArrayList<PostInfo>>
    fun getBookmarkedFeed(curPage: Int) : Single<ArrayList<Feed>>
}