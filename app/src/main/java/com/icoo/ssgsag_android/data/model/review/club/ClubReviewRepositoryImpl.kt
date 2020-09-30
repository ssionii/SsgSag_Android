package com.icoo.ssgsag_android.data.model.review.club


import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.model.base.StringResponse
import com.icoo.ssgsag_android.data.model.review.club.response.PostReviewResponse
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Single
import okhttp3.MultipartBody

class ClubReviewRepositoryImpl (val api: NetworkService, val pref: PreferenceManager) : ClubReviewRepository{

    override fun getClubList(curPage: Int, clubType: Int): Single<ArrayList<ClubInfo>> = api
        .getClubList(pref.findPreference("TOKEN", ""), curPage, clubType)
        .map { it.data }

    override fun getClubDetail(clubIdx: Int): Single<ClubInfo> = api
        .getClubDetail(pref.findPreference("TOKEN", ""), clubIdx)
        .map { it.data }

    override fun rgstrClub(body: JsonObject): Single<Boolean> = api
        .rgstrClub("application/json", pref.findPreference("TOKEN", ""), body)
        .map { it.data }

    override fun uploadPhoto(data: MultipartBody.Part): Single<StringResponse> = api
        .postPhoto(pref.findPreference("TOKEN", ""), data)
        .map{ it }


    override fun likeReview(clubPostIdx: Int): Single<Int> = api
        .likeReview(pref.findPreference("TOKEN", ""), clubPostIdx)
        .map{ it.status }

    override fun unlikeReview(clubPostIdx: Int): Single<Int> = api
        .unlikeReview(pref.findPreference("TOKEN", ""), clubPostIdx)
        .map{ it.status }

    override fun getSsgSagReviews(clubIdx: Int, curPage: Int): Single<ArrayList<ClubPost>> = api
        .getAllSsgSagReviews(pref.findPreference("TOKEN", ""), clubIdx, curPage)
        .map { it.data }

    override fun writeClubReview(body: JsonObject): Single<PostReviewResponse> = api
        .postClubReview("application/json", pref.findPreference("TOKEN", ""), body)
        .map { it }

    override fun getAlreadyWrite(clubIdx: Int) : Single<Boolean> = api
        .getAlreadyWriteReview(pref.findPreference("TOKEN", ""), clubIdx)
        .map { it.data }


    override fun writeClubBlogReview(clubIdx: Int, blogUrl: String): Single<Int> = api
        .postClubBlogReview(clubIdx, blogUrl)
        .map { it.status }

    override fun getBlogReviews(clubIdx: Int, curPage: Int): Single<ArrayList<BlogReview>> = api
        .getBlogReviews(pref.findPreference("TOKEN", ""), clubIdx, curPage)
        .map { it.data }


    override fun searchClub(clubType: Int, univOrLocation: String, keyword: String, curPage: Int) : Single<ArrayList<ClubInfo>> = api
        .searchClub(pref.findPreference("TOKEN", ""), clubType, univOrLocation, keyword, curPage)
        .map { it.data }

    override fun searchClubName(clubType: Int, keyword: String, curPage: Int): Single<ArrayList<ClubInfo>> = api
        .searchClubName(pref.findPreference("TOKEN", ""), clubType, keyword, curPage)
        .map { it.data }

}
