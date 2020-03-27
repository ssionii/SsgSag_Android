package com.icoo.ssgsag_android.data.model.review.club

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.model.base.StringResponse
import com.icoo.ssgsag_android.data.model.review.club.response.PostReviewResponse
import io.reactivex.Single
import okhttp3.MultipartBody

interface ClubReviewRepository {
    fun getClubList(curPage: Int, clubType: Int) : Single<ArrayList<ClubInfo>>
    fun getClubDetail(clubIdx: Int): Single<ClubInfo>

    fun rgstrClub(body: JsonObject) : Single<Boolean>
    fun uploadPhoto(data : MultipartBody.Part) : Single<StringResponse>

    fun likeReview(clubPostIdx: Int): Single<Int>
    fun unlikeReview(clubPostIdx: Int) :Single<Int>

    fun getSsgSagReviews(clubIdx: Int, curPage: Int) : Single<ArrayList<ClubPost>>

    fun writeClubReview(body: JsonObject): Single<PostReviewResponse>
    fun getAlreadyWrite(clubIdx: Int): Single<Boolean>

    fun writeClubBlogReview(clubIdx: Int, blogUrl: String) : Single<Int>
    fun getBlogReviews(clubIdx:Int, curPage: Int) : Single<ArrayList<BlogReview>>

    fun searchClub(clubType: Int, univOrLocation: String, keyword: String, curPage: Int) : Single<ArrayList<ClubInfo>>
    fun searchClubName(keyword: String, curPage: Int): Single<ArrayList<ClubInfo>>
}