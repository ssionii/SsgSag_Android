package com.icoo.ssgsag_android.data.model.review

import android.util.Log
import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.model.ads.AdItem
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Single

class ReviewRepositoryImpl(val api: NetworkService, val pref: PreferenceManager) : ReviewRepository {

    override fun getMyReviews(curPage: Int): Single<ArrayList<ClubPost>> = api
            .getMyReviews(pref.findPreference("TOKEN", ""), curPage)
            .map { it.data }

    override fun updateReview(body: JsonObject, clubPostIdx: Int): Single<Int> = api
        .updateReview("application/json", pref.findPreference("TOKEN", ""), body, clubPostIdx)
        .map { it.status }

    override fun deleteReview(clubPostIdx: Int): Single<Int> = api
        .deleteReview(pref.findPreference("TOKEN", ""), clubPostIdx)
        .map { it.status }

    override fun getAds(): Single<ArrayList<AdItem>> = api
        .getAds(pref.findPreference("TOKEN", ""), "review-00")
        .doOnError { throwable ->
            Log.e("getAds API : ", throwable.message)
        }
        .map { it.data }

}