package com.icoo.ssgsag_android.ui.main.calendar.calendarPage.list

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.data.model.schedule.ScheduleRepository
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class CalendarListDeleteViewModel(
    private val repository: ScheduleRepository
    , private val schedulerProvider: SchedulerProvider)
    : BaseViewModel(){

    val scheduleByDate = ArrayList<Schedule>()

    private val _schedule = MutableLiveData<ArrayList<Schedule>>()
    val schedule: LiveData<ArrayList<Schedule>> get() = _schedule

    init{
        getAllSchedule()
    }

    fun getAllSchedule() {
        addDisposable(repository.getAllCalendar()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                it.run {
                    _schedule.postValue(this)
                }
            }, {

            })
        )
    }

    fun filterSchedule(year: String, month: String): ArrayList<Schedule> {

        val cal = Calendar.getInstance()
        cal.set(year.toInt(), month.toInt(), 1)

        scheduleByDate.clear()

        schedule.value?.let {
            for (i in it.indices) {
                if (it[i].isEnded == 0) {
                    scheduleByDate.add(it[i])
                }
            }
        }

        return scheduleByDate
    }

    fun deleteSchedule(posterIdxList: ArrayList<Int>) {

        val jsonObject = JSONObject()
        val jsonArray = JSONArray(posterIdxList)
        jsonObject.put("posterIdxList", jsonArray)

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(repository.deleteSchedule(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                if(it == 204) {
                    Toast.makeText(context, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }, {

            })
        )

    }
}