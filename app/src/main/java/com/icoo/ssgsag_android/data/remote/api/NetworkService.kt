package com.icoo.ssgsag_android.data.remote.api

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.SsgSagApplication.Companion.globalApplication
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.base.*
import com.icoo.ssgsag_android.data.model.feed.FeedResponse
import com.icoo.ssgsag_android.data.model.feed.FeedTodayResponse
import com.icoo.ssgsag_android.data.model.feed.FeedsResponse
import com.icoo.ssgsag_android.data.model.interest.InterestResponse
import com.icoo.ssgsag_android.data.model.login.LoginResponse
import com.icoo.ssgsag_android.data.model.poster.allPoster.AllPosterResponse
import com.icoo.ssgsag_android.data.model.schedule.ScheduleResponse
import com.icoo.ssgsag_android.data.model.poster.PosterResponse
import com.icoo.ssgsag_android.data.model.poster.TodaySsgSagResponse
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetailResponse
import com.icoo.ssgsag_android.data.model.ads.AdsDataResponse
import com.icoo.ssgsag_android.data.model.community.CommunityMainResponse
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetailResponse
import com.icoo.ssgsag_android.data.model.community.board.BoardPostListResponse
import com.icoo.ssgsag_android.data.model.poster.allPoster.AllPosterAd
import com.icoo.ssgsag_android.data.model.poster.allPoster.AllPosterAdResponse
import com.icoo.ssgsag_android.data.model.review.club.response.*
import com.icoo.ssgsag_android.data.model.signUp.SignUpResponse
import com.icoo.ssgsag_android.data.model.signUp.UniversityListResponse
import com.icoo.ssgsag_android.data.model.subscribe.SubscribeResponse
import com.icoo.ssgsag_android.data.model.user.myBoard.BookmarkedResponse
import com.icoo.ssgsag_android.data.model.user.myBoard.MyCommentResponse
import com.icoo.ssgsag_android.data.model.user.userInfo.UserInfoResponse
import com.icoo.ssgsag_android.data.model.user.userNotice.UserNoticeResponse
import io.reactivex.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface NetworkService {

    //region 로그인
    //로그인
    @POST("/login")
    fun  postLoginResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Observable<LoginResponse>

    //자동 로그인
    @POST("/autoLogin")
    fun postAutoLoginResponse(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Query("registrationCode") registrationCode : String
    ): Single<NullDataResponse>
    //endregion

    //region 회원
    //이메일 중복확인
    //닉네임 중복확인
    @GET("/user/validateNickname")
    fun validateNickname(
        @Query("userNickname") userNickname: String
    ): Single<BooleanResponse>
    //회원 가입
    @POST("/user")
    fun postSignUpResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Single<SignUpResponse>
    //회원 조회
    @GET("/user")
    fun userInfoResponse(
        @Header("Authorization") token : String
    ): Single<UserInfoResponse>
    // 대학교 리스트 가져오기
    @GET("/v2/validUnivList")
    fun getUnivList(
    ): Single<UniversityListResponse>
    //회원 관심분야 및 관심직무 조회
    //회원 관심직무 재등록
    //회원 관심분야 재등록
    //회원 정보 수정
    @Multipart
    @POST("/user/update")
    fun editUserInfoResponse(
        @Header("Authorization") token: String,
        @Part("userNickname") userNickname: RequestBody,
        @Part("userUniv") userUniv: RequestBody,
        @Part("userMajor") userMajor: RequestBody,
        @Part("userStudentNum") userStudentNum: RequestBody,
        @Part("userGrade") userGrade: RequestBody,
        @Part("userProfileUrl") userProfileUrl: RequestBody,
        @Part profile: MultipartBody.Part?
    ): Single<NullDataResponse>
    //회원 탈퇴
    //마이페이지 사진등록
    //구독 조회
    @GET("/user/subscribe")
    fun getSubscribeResponse(
        @Header("Authorization") token : String
    ): Single<SubscribeResponse>
    //구독 등록
    @POST("/user/subscribe/{interestIdx}")
    fun subscribeResponse(
        @Header("Authorization") token : String,
        @Path("interestIdx") interestIdx : Int
    ): Single<NullDataResponse>
    //구독 취소
    @DELETE("/user/subscribe/{interestIdx}")
    fun unsubscribeResponse(
        @Header("Authorization") token : String,
        @Path("interestIdx") interestIdx : Int
    ): Single<NullDataResponse>

    // 인턴
    //회원 관심분야 및 관심직무 조회
    @GET("/user/interest")
    fun getInterestResponse(
        @Header("Authorization") token: String
    ): Single<InterestResponse>

    //회원 관심직무기업형태 재등록
    @POST("/user/v2/reInterest")
    fun reInterest(
        @Header("Authorization") token: String,
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Single<NullDataResponse>

    // 알림 조회
    @GET("/user/notify")
    fun getUserNotice(
        @Header("Authorization") token: String,
        @Query("curPage") curPage: Int,
        @Query("pageSize") pageSize: Int
    ) : Single<UserNoticeResponse>

    // 알림 조회
    @GET("/user/notify/count")
    fun getUserNoticeCount(
        @Header("Authorization") token: String
    ) : Single<IntResponse>

    // 내 댓글 조회
    @GET("/user/myComment")
    fun getMyComment(
        @Header("Authorization") token: String,
        @Query("curPage") curPage: Int,
        @Query("pageSize") pageSize: Int
    ): Single<MyCommentResponse>
    // 저장한 게시글 조회
    @GET("/community/save")
    fun getBookmarkedPost(
        @Header("Authorization") token: String,
        @Query("curPage") curPage: Int,
        @Query("pageSize") pageSize: Int
    ): Single<BookmarkedResponse>

    //저장한 피드 조회
    @GET("/feed")
    fun getBookmarkedFeeds(
        @Header("Authorization") token: String,
        @Query("save") isSave: Int,
        @Query("curPage") curPage: Int
    ):Single<FeedsResponse>

    //region 유저 로그 수집
    //포스터 상세보기에서 웹사이트 버튼 클릭
    @POST("/user/log")
    fun recordClickHistory(
        @Header("Authorization") token: String,
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Single<NullDataResponse>
    //endregion
   //endregion

    //region 포스터
    //포스터 조회
    @GET("/poster/v2")
    fun posterResponse(
        @Header("Authorization") token: String
    ): Single<PosterResponse>
    // 전체 광고 포스터 조회
    @GET("/ads/v2")
    fun getAllPosterAds(
        @Header("Authorization") token: String,
        @Query("viewName") viewName: String
    ): Single<AllPosterAdResponse>
    //전체 포스터 카테고리 조회(전체 -> 카테고리)
    @GET("/poster/v2/all")
    fun allPosterCategoryResponse(
        @Header("Authorization") token: String,
        @Query("category") category : Int,
        @Query("sortType") sortType : Int,
        @Query("curPage") curPage: Int
    ): Single<AllPosterResponse>
    //전체 포스터 분야별 조회(카테고리 -> 분야별)
    @GET("/poster/v2/all")
    fun allPosterFieldResponse(
        @Header("Authorization") token: String,
        @Query("category") category : Int,
        @Query("interestNumList") interestNum : String,
        @Query("sortType") sortType: Int,
        @Query("curPage") curPage: Int
    ): Single<AllPosterResponse>
    //포스터 상세 정보 조회
    @GET("/poster/{posterIdx}")
    fun posterDetailResponse(
        @Header("Authorization") token: String,
        @Path("posterIdx") posterIdx : Int,
        @Query("type") type: Int
    ): Single<PosterDetailResponse>
    //포스터 상세 정보 조회
    @GET("/poster/{posterIdx}")
    fun posterDetailResponseEtc(
        @Header("Authorization") token: String,
        @Path("posterIdx") posterIdx : Int
    ): Single<PosterDetailResponse>
    //포스터 좋아요/싫어요
    @POST("/poster/like")
    fun posterSsgSagResponse(
        @Header("Authorization") token: String,
        @Query("posterIdx") posterIdx: Int,
        @Query("like") like: Int,
        @Query("type") type: Int
    ): Single<IntResponse>
    // 포스터 검색
    @GET("/poster/search")
    fun posterSearchResponse(
        @Header("Authorization") token: String,
        @Query("keyword") keyword: String,
        @Query("curPage") curPage: Int
    ): Single<AllPosterResponse>
    // 오늘 슥삭 조회
    @GET("/poster/today/ssgsag")
    fun getTodaySsgSag(
        @Header("Authorization") token: String
    ): Single<TodaySsgSagResponse>

    //endregion

    //region 캘린더
    //일정 조회
    @GET("/todo/v2")
    fun calendarResponse(
        @Header("Authorization") token: String,
        @Query("year") year: String,
        @Query("month") month: String,
        @Query("day") day: String,
        @Query("sortType") sortType: Int
    ): Single<ScheduleResponse>
    //즐겨찾는 일정 조회
    @GET("/todo/v2")
    fun calendarFavoriteResponse(
        @Header("Authorization") token: String,
        @Query("year") year: String,
        @Query("month") month: String,
        @Query("day") day: String,
        @Query("favorite") favorite: Int,
        @Query("sortType") sortType: Int
    ): Single<ScheduleResponse>
    //일정 지원 완료
    //일정 삭제
    @HTTP(method = "DELETE",path ="/todo", hasBody = true)
    fun deletePoster(
        @Header("Authorization") token: String,
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Single<NullDataResponse>


//    @DELETE("/todo")
//    fun deletePoster(
//        @Header("Authorization") token: String,
//        @Header("Content-Type") content_type: String,
//        @Body() body: JsonObject
//    ): Single<NullDataResponse>
    //일정 지원 완료 조회
    //일정 지원 즐겨찾기
    @POST("/todo/favorite/{posterIdx}")
    fun bookmarkPoster(
        @Header("Authorization") token: String,
        @Path("posterIdx") posterIdx : Int
    ): Single<ScheduleResponse>
    //일정 지원 즐겨찾기 해제
    @DELETE("/todo/favorite/{posterIdx}")
    fun unbookmarkPoster(
        @Header("Authorization") token: String,
        @Path("posterIdx") posterIdx : Int
    ): Single<ScheduleResponse>
    // 일정 푸시 조회
    @GET("todo/push")
    fun getTodoPushAlarm(
        @Header("Authorization") token: String,
        @Query("posterIdx") posterIdx : Int
    ):Single<IntArrayListResponse>
    // 일정 푸시 등록
    @POST("todo/push")
    fun postTodoPushAlarm(
        @Header("Authorization") token: String,
        @Query("posterIdx") posterIdx : Int,
        @Query("ddayList") ddayList : String
    ):Single<IntResponse>
    // 일정 푸시 및 즐겨찾기 동시 삭제
    @DELETE("todo/push")
    fun deleteTodoPushAlarm(
        @Header("Authorization") token: String,
        @Query("posterIdx") posterIdx : Int
    ):Single<IntResponse>

    //endregion

    //region 이력
    //이력 조회
    //이력 추가
    //이력 수정
    //이력 삭제
    //endregion

    //region 공지사항
    //공지사항 조회
    //endregion

    //region 업데이트
    //업데이트 확인
    @GET("/update")
    fun getUpdateResponse(
        @Query("osType") osType: String,
        @Query("version") version: String
    ): Single<IntResponse>
    //endregion

    //region 댓글
    //댓글 추가
    @POST("/comment")
    fun writeComment(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Body() body: JsonObject
    ): Single<NullDataResponse>
    //댓글 수정
    @PUT("/comment")
    fun editComment(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Body() body: JsonObject
    ): Single<NullDataResponse>
    //댓글 삭제
    @DELETE("/comment/{commentIdx}")
    fun deleteComment(
        @Header("Authorization") token: String,
        @Path("commentIdx") commentIdx : Int
    ): Single<NullDataResponse>
    //댓글 좋아요 또는 좋아요 취소
    @POST("/comment/like/{commentIdx}/{like}")
    fun likeComment(
        @Header("Authorization") token: String,
        @Path("commentIdx") commentIdx : Int,
        @Path("like") like : Int
    ): Single<NullDataResponse>
    //댓글 신고
    @POST("/comment/caution/{commentIdx}")
    fun cautionComment(
        @Header("Authorization") token: String,
        @Path("commentIdx") commentIdx : Int
    ): Single<NullDataResponse>
    //endregion

    //region
    // 오늘의 피드 조회
    @GET("/feed/v2/today")
    fun getTodayFeeds(
        @Header("Authorization") token: String,
        @Query("curPage") curPage: Int,
        @Query("categoryIdx") category: Int
    ):Single<FeedTodayResponse>
    //카테고리별 피드 조회(최신순)
    @GET("/feed")
    fun getCategoryFeeds(
        @Header("Authorization") token: String,
        @Query("curPage") curPage: Int,
        @Query("categoryIdx") category: Int
    ):Single<FeedsResponse>
    //피드 한개 조회(조회수 안 올라감)
    @GET("/feed/{feedIdx}")
    fun getFeed(
        @Header("Authorization") token:String,
        @Path("feedIdx") feedIdx: Int
    ):Single<FeedResponse>
    //피드 한개 조회(조회수 올라감)
    @GET("/feed/refresh/{feedIdx}")
    fun getFeedRefresh(
        @Header("Authorization") token:String,
        @Path("feedIdx") feedIdx: Int
    ):Single<FeedResponse>

    //피드 저장
    @POST("/feed/{feedIdx}")
    fun bookmarkFeed(
        @Header("Authorization") token: String,
        @Path("feedIdx") feedIdx : Int
    ): Single<NullDataResponse>
    //피드 저장 취소
    @DELETE("/feed/{feedIdx}")
    fun unbookmarkFeed(
        @Header("Authorization") token: String,
        @Path("feedIdx") feedIdx : Int
    ): Single<NullDataResponse>
    //endregion


    @Multipart
    @POST("/user/photo")
    fun postPhotoResponse(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part?
    ): Single<StringResponse>


    // region
    // 동아리 등록
    @POST("/club")
    fun rgstrClub(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Body() body: JsonObject
    ): Single<BooleanResponse>

    // 동아리 목록 조회
    @GET("/club")
    fun getClubList(
        @Header("Authorization") token: String,
        @Query("curPage") curPage: Int,
        @Query("clubType") clubType : Int
    ): Single<ClubsResponse>

    // 동아리 상세 조회
    @GET("/club/{clubIdx}")
    fun getClubDetail(
        @Header("Authorization") token: String,
        @Path("clubIdx") clubIdx: Int
    ): Single<ClubDetailResponse>

    // 리뷰 좋아요
    @POST("/club/post/like/{clubPostIdx}")
    fun likeReview(
        @Header("Authorization") token: String,
        @Path("clubPostIdx") clubPostIdx: Int
    ): Single<NullDataResponse>

    // 리뷰 좋아요 취소
    @DELETE("/club/post/like/{clubPostIdx}")
    fun unlikeReview(
        @Header("Authorization") token: String,
        @Path("clubPostIdx") clubPostIdx: Int
    ): Single<NullDataResponse>

    // 후기 전체 조회
    @GET("/club/{clubIdx}/post")
    fun getAllSsgSagReviews(
        @Header("Authorization") token: String,
        @Path("clubIdx") clubIdx: Int,
        @Query("curPage") curPage: Int
    ): Single<ClubPostsResponse>

    // 동아리 후기 등록
    @POST("/club/post")
    fun postClubReview(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Body() body: JsonObject
    ): Single<PostReviewResponse>

    // 동아리 clubTpye, 대학교 또는 지역, 검색어로 검색
    @GET("/club/search")
    fun searchClub(
        @Header("Authorization") token: String,
        @Query("clubType") clubType: Int,
        @Query("univOrLocation") univOrLocation: String,
        @Query("keyword") keyword: String,
        @Query("curPage") curPage: Int
    ): Single<ClubsResponse>

    // 동아리 clubType과 검색어로 검색
    @GET("/club/search")
    fun searchClubName(
        @Header("Authorization") token: String,
        @Query("clubType") clubType: Int,
        @Query("keyword") keyword: String,
        @Query("curPage") curPage: Int
    ): Single<ClubsResponse>

    // 동아리 사진 등록
    @Multipart
    @POST("/upload/photo")
    fun postClubRgstrPhoto(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part?
    ): Single<StringResponse>

    // 동아리 후기 등록 여부 조회
    @GET("/club/{clubIdx}/post/already")
    fun getAlreadyWriteReview(
        @Header("Authorization") token: String,
        @Path("clubIdx") clubIdx: Int
    ):Single<BooleanResponse>

    // 블로그 후기 등록
    @POST("/club/{clubIdx}/blog")
    fun postClubBlogReview(
        @Path("clubIdx") clubIdx: Int,
        @Query("blogUrl") blogUrl: String
    ): Single<NullDataResponse>

    // 블로그 후기 조회
    @GET("/club/{clubIdx}/blog")
    fun getBlogReviews(
        @Header("Authorization") token: String,
        @Path("clubIdx") clubIdx: Int,
        @Query("curPage") curPage: Int
    ): Single<ClubBlogReviewsResponse>

    // 후기 등록 이벤트
    @POST("/event")
    fun postEvent(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Body() body: JsonObject
    ): Single<NullDataResponse>


    // 나의 후기
    @GET("/club/mypost")
    fun getMyReviews(
        @Header("Authorization") token: String,
        @Query("curPage") curPage: Int
    ): Single<ClubPostsResponse>

    @PUT("/club/post/{clubPostIdx}")
    fun updateReview(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Body() body: JsonObject,
        @Path("clubPostIdx") clubPostIdx: Int
    ): Single<NullDataResponse>

    @DELETE("/club/post/{clubPostIdx}")
    fun deleteReview(
        @Header("Authorization") token: String,
        @Path("clubPostIdx") clubPostIdx: Int
    ): Single<NullDataResponse>


    // 커뮤니티
    // 메인 화면 조회
    @GET("/community/mainview")
    fun getCommunityMain(
        @Header("Authorization") token: String
    ): Single<CommunityMainResponse>


    // 게시글 조회
    @GET("/community")
    fun getBoardPost(
        @Header("Authorization") token: String,
        @Query("category") category: String,
        @Query("curPage") curPage: Int,
        @Query("pageSize") pageSize: Int
    ): Single<BoardPostListResponse>
    // 게시글 작성
    @POST("/community")
    fun writeBoardPost(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Body() body : JsonObject
    ): Single<NullDataResponse>
    // 게시글 수정
    @PUT("/community")
    fun editBoardPost(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Body() body : JsonObject
    ): Single<NullDataResponse>
    // 게시글 상세보기
    @GET("/community/{communityIdx}")
    fun getPostDetail(
        @Header("Authorization") token: String,
        @Path("communityIdx") communityIdx: Int
    ): Single<BoardPostDetailResponse>
    // 게시글 상세보기 (조회수 반영 X)
    @GET("/community/{communityIdx}")
    fun refreshPostDetail(
        @Header("Authorization") token: String,
        @Path("communityIdx") communityIdx: Int,
        @Query("isReset") isReset : Int
    ): Single<BoardPostDetailResponse>
    // 게시글 삭제
    @DELETE("/community/{communityIdx}")
    fun deleteBoardPost(
        @Header("Authorization") token: String,
        @Path("communityIdx") communityIdx : Int
    ): Single<NullDataResponse>
    // 게시글 좋아요
    @POST("/community/like")
    fun likeCommunityPost(
        @Header("Authorization") token: String,
        @Query("communityIdx") communityIdx: Int
    ): Single<NullDataResponse>
    // 게시글 좋아요 취소
    @DELETE("/community/like")
    fun unlikeCommunityPost(
        @Header("Authorization") token: String,
        @Query("communityIdx") communityIdx: Int
    ): Single<NullDataResponse>
    // 게시글 북마크
    @POST("/community/save")
    fun bookmarkCommunityPost(
        @Header("Authorization") token: String,
        @Query("communityIdx") communityIdx: Int
    ): Single<NullDataResponse>
    // 게시글 북마크 취소
    @DELETE("/community/save")
    fun unbookmarkCommunityPost(
        @Header("Authorization") token: String,
        @Query("communityIdx") communityIdx: Int
    ): Single<NullDataResponse>


    // 댓글 작성
    @POST("/community/comment")
    fun writePostComment(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Body() body : JsonObject
    ):Single<NullDataResponse>
    // 댓글 삭제
    @DELETE("/community/comment/{commentIdx}")
    fun deletePostComment(
        @Header("Authorization") token: String,
        @Path("commentIdx") commentIdx : Int
    ): Single<NullDataResponse>
    // 댓글 좋아요
    @POST("/community/comment/like")
    fun likeCommunityComment(
        @Header("Authorization") token: String,
        @Query("commentIdx") communityIdx: Int
    ): Single<NullDataResponse>
    // 댓글 좋아요 취소
    @DELETE("/community/comment/like")
    fun unlikeCommunityComment(
        @Header("Authorization") token: String,
        @Query("commentIdx") communityIdx: Int
    ): Single<NullDataResponse>


    // 답글 작성
    @POST("/community/ccomment")
    fun writePostReply(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Body() body : JsonObject
    ):Single<NullDataResponse>
    // 답글 삭제
    @DELETE("/community/ccomment/{ccommentIdx}")
    fun deletePostReply(
        @Header("Authorization") token: String,
        @Path("ccommentIdx") ccommentIdx : Int
    ): Single<NullDataResponse>
    // 답글 좋아요
    @POST("/community/ccomment/like")
    fun likeCommunityReply(
        @Header("Authorization") token: String,
        @Query("ccommentIdx") ccommunityIdx: Int
    ): Single<NullDataResponse>
    // 답글 좋아요 취소
    @DELETE("/community/ccomment/like")
    fun unlikeCommunityReply(
        @Header("Authorization") token: String,
        @Query("ccommentIdx") ccommunityIdx: Int
    ): Single<NullDataResponse>





    // 광고
    @GET("/ads/v2")
    fun getAds(
        @Header("Authorization") token: String,
        @Query("viewName") viewName: String
    ): Single<AdsDataResponse>

    companion object {
        fun create(): NetworkService {
            return Retrofit.Builder()
                .baseUrl(globalApplication.getString(R.string.base_url))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NetworkService::class.java)
        }
    }
}