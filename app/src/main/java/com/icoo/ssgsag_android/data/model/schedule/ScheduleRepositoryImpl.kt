package com.icoo.ssgsag_android.data.model.schedule

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.model.subscribe.Subscribe
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import io.reactivex.Single

class ScheduleRepositoryImpl(val api: NetworkService, val pref: PreferenceManager): ScheduleRepository {
    override fun getAllCalendar(sortType: Int?): Single<ArrayList<Schedule>> = api
        .calendarResponse(pref.findPreference("TOKEN", ""), "0000", "00", "00", sortType!!)
        .map { it.data }

    override fun getCalendar(year: String, month: String, date: String, sortType: Int?): Single<ArrayList<Schedule>> = api
        .calendarResponse(pref.findPreference("TOKEN", ""), year, month, date, sortType!!)
        .map { it.data }

    override fun getFavoriteCalendar(year: String, month: String, date: String, sortType: Int?): Single<ArrayList<Schedule>> = api
        .calendarFavoriteResponse(pref.findPreference("TOKEN", ""), year, month, date, 1, sortType!!)
        .map { it.data }

    override fun bookmarkSchedule(posterIdx: Int): Single<Int> = api
        .bookmarkPoster(pref.findPreference("TOKEN", ""), posterIdx)
        .map { it.status }

    override fun unbookmarkSchedule(posterIdx: Int): Single<Int> = api
        .unbookmarkPoster(pref.findPreference("TOKEN", ""), posterIdx)
        .map { it.status }

    override fun getSubscribe(): Single<ArrayList<Subscribe>> = api
        .getSubscribeResponse(pref.findPreference("TOKEN", ""))
        .map { it.data }

    override fun deleteSchedule(body: JsonObject): Single<Int> = api
        .deletePoster(pref.findPreference("TOKEN", ""), "application/json", body)
        .map { it.status }
}