package com.icoo.ssgsag_android.data.model.schedule

import com.google.gson.JsonObject
import com.icoo.ssgsag_android.data.model.subscribe.Subscribe
import io.reactivex.Single

interface ScheduleRepository {
    fun getAllCalendar(): Single<ArrayList<Schedule>>
    fun getCalendar(year: String, month: String, date: String): Single<ArrayList<Schedule>>
    fun getFavoriteCalendar(year: String, month: String, date: String): Single<ArrayList<Schedule>>
    fun bookmarkSchedule(posterIdx: Int): Single<Int>
    fun unbookmarkSchedule(posterIdx: Int): Single<Int>
    fun getSubscribe(): Single<ArrayList<Subscribe>>
    fun deleteSchedule(body: JsonObject) : Single<Int>
}