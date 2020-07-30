package com.icoo.ssgsag_android.data.model.poster

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.model.poster.allPoster.AllPosterAd
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import io.reactivex.Single

interface PosterRepository {
    //poster
    //fun getAllPostersCategory(): Single<ArrayList<PosterDetail>>
    fun getAllPosterAd() : Single<AllPosterAd>
    fun getAllPostersCategory(category: Int, sortType: Int, curPage: Int) : Single<ArrayList<PosterDetail>>
    fun getAllPostersField(category: Int, interestNum:String, sortType:Int, curPage: Int): Single<ArrayList<PosterDetail>>
    fun getAllPosters(): Single<Poster>
    fun ssgSag(posterIdx: Int, like: Int): Single<Int>
    fun saveAtPosterDetail(posterIdx: Int) :Single<Int>
    fun getPosterFromMain(posterIdx: Int, type: Int): Single<PosterDetail>
    fun getPoster(posterIdx: Int): Single<PosterDetail>
    fun getSearchPosters(keyword: String, curPage: Int): Single<ArrayList<PosterDetail>>
    fun getTodaySsgSag(): Single<TodaySsgSag>
    fun getUserCnt():Single<Int>

    // pushAlarm
    fun getTodoPushAlarm(posterIdx: Int) : Single<ArrayList<Int>>
    fun postTodoPushAlarm(posterIdx: Int, ddayList: String) : Single<Int>
    fun deleteTodoPushAlarm(posterIdx: Int) : Single<Int>

    //comment
    fun writeComment(body: JsonObject): Single<Int>
    fun editComment(body: JsonObject): Single<Int>
    fun deleteComment(commentIdx: Int): Single<Int>
    fun likeComment(commentIdx: Int, like: Int): Single<Int>
    fun cautionComment(commentIdx: Int): Single<Int>
}