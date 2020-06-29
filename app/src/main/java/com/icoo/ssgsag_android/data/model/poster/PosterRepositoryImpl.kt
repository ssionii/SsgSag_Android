package com.icoo.ssgsag_android.data.model.poster

import android.util.Log
import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Single

class PosterRepositoryImpl(val api: NetworkService, val pref: PreferenceManager) : PosterRepository {

    override fun getAllPostersCategory(category: Int, sortType: Int, curPage: Int): Single<ArrayList<PosterDetail>> = api
        .allPosterCategoryResponse(pref.findPreference("TOKEN",""), category, sortType, curPage)
        .doOnError { throwable ->
            Log.e("getAllPostersC API : ", throwable.message)
        }
        .map { it.data}

    override fun getAllPostersField( category: Int, interestNum: String, sortType: Int, curPage: Int): Single<ArrayList<PosterDetail>>  = api
        .allPosterFieldResponse(pref.findPreference("TOKEN", ""), category, interestNum, sortType, curPage)
        .doOnError { throwable ->
            Log.e("getAllPostersF API : ", throwable.message)
        }
        .map {
            Log.e("all posters 분야 status", it.status.toString())
            it.data}

    override fun getWhatPosters(category: Int): Single<ArrayList<PosterDetail>> = api
        .allPosterResponse(pref.findPreference("TOKEN", ""), category)
        .doOnError { throwable ->
            Log.e("getWhatPosters API : ", throwable.message)
        }
        .map { it.data }

    override fun getAllPosters(): Single<Poster> = api
        .posterResponse(pref.findPreference("TOKEN", ""))
        .doOnError { throwable ->
            Log.e("getAllPosters API : ", throwable.message)
        }
        .map { it.data }

    override fun ssgSag(posterIdx: Int, like: Int): Single<Int> = api
        .posterSsgSagResponse(pref.findPreference("TOKEN", ""), posterIdx, like, 0)
        .map { it.status }

    override fun saveAtPosterDetail(posterIdx: Int): Single<Int> = api
        .posterSsgSagResponse(pref.findPreference("TOKEN", ""), posterIdx, 1, 1)
        .map { it.status }

    // 포스터 상세 조회
    override fun getPosterFromMain(posterIdx: Int, type: Int): Single<PosterDetail> = api
        .posterDetailResponse(pref.findPreference("TOKEN", ""), posterIdx, type)
        .map { it.data }

    override fun getPoster(posterIdx: Int): Single<PosterDetail> = api
        .posterDetailResponseEtc(pref.findPreference("TOKEN", ""), posterIdx)
        .map { it.data }


    override fun getSearchPosters(keyword: String, curPage: Int): Single<ArrayList<PosterDetail>> = api
        .posterSearchResponse(pref.findPreference("TOKEN", ""), keyword, curPage)
        .map{it.data}

    override fun getTodaySsgSag(): Single<TodaySsgSag> = api
        .getTodaySsgSag(pref.findPreference("TOKEN", ""))
        .map { it.data }

    override fun getUserCnt(): Single<Int> = api
        .posterResponse(pref.findPreference("TOKEN", ""))
        .map { it.data.userCnt }

    override fun getTodoPushAlarm(posterIdx: Int): Single<ArrayList<Int>> = api
        .getTodoPushAlarm(pref.findPreference("TOKEN", ""), posterIdx)
        .map { it.data }

    override fun postTodoPushAlarm(posterIdx: Int, ddayList: ArrayList<Int>): Single<Int> = api
        .postTodoPushAlarm(pref.findPreference("TOKEN", ""), posterIdx, ddayList)
        .map { it.data }

    override fun deleteTodoPushAlarm(posterIdx: Int): Single<Int> = api
        .deleteTodoPushAlarm(pref.findPreference("TOKEN", ""), posterIdx)
        .map { it.data }

    //comment
    override fun writeComment(body: JsonObject): Single<Int> = api
        .writeComment("application/json", pref.findPreference("TOKEN", ""), body)
        .map { it.status }

    override fun editComment(body: JsonObject): Single<Int> = api
        .editComment("application/json", pref.findPreference("TOKEN", ""), body)
        .map { it.status }

    override fun deleteComment(commentIdx: Int): Single<Int> = api
        .deleteComment(pref.findPreference("TOKEN", ""), commentIdx)
        .map { it.status }

    override fun likeComment(commentIdx: Int, like: Int): Single<Int> = api
        .likeComment(pref.findPreference("TOKEN", ""), commentIdx, like)
        .map { it.status }

    override fun cautionComment(commentIdx: Int): Single<Int> = api
        .cautionComment(pref.findPreference("TOKEN", ""), commentIdx)
        .map { it.status }


}