package com.icoo.ssgsag_android.data.model.review

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.model.ads.AdItem
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import io.reactivex.Single

interface ReviewRepository {

    fun getMyReviews(curPage: Int) : Single<ArrayList<ClubPost>>

    fun updateReview(body: JsonObject, clubPostIdx: Int) : Single<Int>
    fun deleteReview(clubPostIdx: Int) : Single<Int>

    fun getAds() : Single<ArrayList<AdItem>>
}