package com.icoo.ssgsag_android.util.service.network

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.data.model.base.IntResponse
import com.icoo.ssgsag_android.data.model.base.StringResponse
import com.icoo.ssgsag_android.data.model.career.CareerResponse
import com.icoo.ssgsag_android.data.model.interest.InterestResponse
import com.icoo.ssgsag_android.data.model.login.LoginResponse
import com.icoo.ssgsag_android.data.model.notice.NoticeResponse
import com.icoo.ssgsag_android.data.model.poster.PosterResponse
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetailResponse
import com.icoo.ssgsag_android.data.model.schedule.ScheduleResponse
import com.icoo.ssgsag_android.data.model.signUp.SignUpResponse
import com.icoo.ssgsag_android.data.model.subscribe.SubscribeResponse
import com.icoo.ssgsag_android.data.model.user.userInfo.UserInfoResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.*
import retrofit2.converter.gson.GsonConverterFactory

interface NetworkService {

    //region 로그인
    //로그인
    /*@POST("/login")
    fun  postLoginResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Observable<LoginResponse>*/

    //자동 로그인

    /*
    @POST("/autoLogin")
    fun postAutoLoginResponse(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String
    ): Call<StringResponse>*/

    //endregion
    //region 회원
    //회원 가입

    @POST("/user")
    fun postSignUpResponse(
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<SignUpResponse>

    //회원 조회
    @GET("/user")
    fun getInfoResponse(
        @Header("Authorization") token: String
    ): Observable<UserInfoResponse>

    //회원 정보 수정
    @PUT("/user")
    fun putInfoResponse(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Body() body: JsonObject
    ): Call<StringResponse>

    //마이페이지 사진 등록
    @Multipart
    @POST("/user/photo")
    fun postPhotoResponse(
        @Header("Authorization") token: String,
        @Part profile: MultipartBody.Part?
    ): Call<StringResponse>

    //회원 관심분야 및 관심직무 조회
    @GET("/user/interest")
    fun getInterestResponse(
        @Header("Authorization") token: String
    ): Call<InterestResponse>

    //회원 관심직무기업형태 재등록
    @POST("/user/reInterest")
    fun reInterest(
        @Header("Authorization") token: String,
        @Header("Content-Type") content_type: String,
        @Body() body: JsonObject
    ): Call<StringResponse>

    //회원 탈퇴
    @DELETE("/user")
    fun deleteWithdrawalResponse(
        @Header("Authorization") token: String
    ): Call<StringResponse>

    //구독 조회
    @GET("/user/subscribe")
    fun getSubscribeResponse(
        @Header("Authorization") token: String
    ): Call<SubscribeResponse>

    //구독 등록
    @POST("/user/subscribe/{interestIdx}")
    fun postSubscribeResponse(
        @Header("Authorization") token: String,
        @Path("interestIdx") interestIdx: Int
    ): Call<StringResponse>

    //구독 취소
    @DELETE("/user/subscribe/{interestIdx}")
    fun deleteSubscribeResponse(
        @Header("Authorization") token: String,
        @Path("interestIdx") interestIdx: Int
    ): Call<StringResponse>

    //endregion
    //region 포스터
    //포스터 조회
    @GET("/poster")
    fun getPosterShowResponse(
        @Header("Authorization") token: String
    ): Call<PosterResponse>

    //포스터 상세 정보 조회
    @GET("/poster/{posterIdx}")
    fun getPosterDetailResponse(
        @Header("Authorization") token: String,
        @Path("posterIdx") posterIdx: Int
    ): Call<PosterDetailResponse>

    //포스터 등록
    //포스터 1개 상세 정보 조회
    //메뉴얼 1개 상세 정보 조회
    //포스터 좋아요/싫어요
    @POST("/poster/like")
    fun postPosterLikeResponse(
        @Header("Authorization") token: String,
        @Query("posterIdx") posterIdx: String,
        @Query("like") like: String
    ): Call<IntResponse>

    //endregion
    //region 캘린더
    //일정 조회
    @GET("/todo")
    fun getTodoResponse(
        @Header("Authorization") token: String,
        @Query("year") year: String,
        @Query("month") month: String,
        @Query("day") day: String
    ): Call<ScheduleResponse>

    //일정 지원 완료
    @POST("/todo/complete/{posterIdx}")
    fun postCompleteTodoResponse(
        @Header("Authorization") token: String,
        @Path("posterIdx") posterIdx: Int
    ): Call<StringResponse>

    //일정 삭제
    @DELETE("/todo/{posterIdx}")
    fun deleteDeleteTodoResponse(
        @Header("Authorization") token: String,
        @Path("posterIdx") posterIdx: Int
    ): Call<StringResponse>

    //일정 지원 완료 조회
    @GET("/todo/completed")
    fun getCompleteTodoResponse(
        @Header("Authorization") token: String
    ): Call<ScheduleResponse>

    //일정 지원 즐겨찾기
    @POST("/todo/favorite/{posterIdx}")
    fun postBookMarkResponse(
        @Header("Authorization") token: String,
        @Path("posterIdx") posterIdx: Int
    ): Call<StringResponse>

    //일정 지원 즐겨찾기 해제
    @DELETE("/todo/favorite/{posterIdx}")
    fun deleteBookMarkResponse(
        @Header("Authorization") token: String,
        @Path("posterIdx") posterIdx: Int
    ): Call<StringResponse>

    //endregion
    //region 이력
    //이력 조회
    @GET("/career/{careerType}")
    fun getCareerReferenceResponse(
        @Header("Authorization") token: String,
        @Path("careerType") career_type: Int
    ): Call<CareerResponse>

    //이력 추가
    @POST("/career")
    fun postCareerAddResponse(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Body() body: JsonObject
    ): Call<StringResponse>

    //이력 수정
    @PUT("/career")
    fun putCareerUpdateResponse(
        @Header("Content-Type") content_type: String,
        @Header("Authorization") token: String,
        @Body() body: JsonObject
    ): Call<StringResponse>

    //이력 삭제
    @DELETE("/career/{careerIdx}")
    fun deleteCareerResponse(
        @Header("Authorization") token: String,
        @Path("careerIdx") career_idx: Int
    ): Call<StringResponse>

    //endregion
    //region 공지사항
    //공지사항 조회
    @GET(" /notice")
    fun getNoticeResponse(
    ): Call<NoticeResponse>

    //endregion
    //region 투두리스트 클릭 기록
    //투두리스트 클릭 기록
    @POST("/todo/click/{posterIdx}/{type}")
    fun postTodoClickResponse(
        @Header("Authorization") token: String,
        @Path("posterIdx") posterIdx: Int,
        @Path("type") type: Int
    ): Call<StringResponse>

    //endregion
    //업데이트 조회
    @GET("/update")
    fun getUpdateResponse(
    ): Call<IntResponse>


    companion object {
        fun create(): NetworkService {
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(SsgSagApplication.getGlobalApplicationContext().getString(R.string.test_url))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(NetworkService::class.java)
        }
    }
}