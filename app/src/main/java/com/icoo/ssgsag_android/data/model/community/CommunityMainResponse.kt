package com.icoo.ssgsag_android.data.model.community

import com.icoo.ssgsag_android.data.model.community.board.PostInfo
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.data.model.review.club.ClubPost

data class CommunityMainResponse(
    val status : Int,
    val message : String,
    val data : CommunityMainCollection
)

data class CommunityMainCollection(
    val feedList : ArrayList<Feed>,
    val recruitTeamList : ArrayList<RecruitTeamMain>?, // 임시로 넣어둠
    val worryCommunityList : ArrayList<PostInfo>?,
    val freeCommunityList : ArrayList<PostInfo>?,
    val clubAndClubPostList : ArrayList<ClubPost>?
)

data class RecruitTeamMain(
    val idx : Int,
    val title : String,
    val profileImg : String,
    val isRecruiting : Int,
    val area : String,
    val category : String
)
