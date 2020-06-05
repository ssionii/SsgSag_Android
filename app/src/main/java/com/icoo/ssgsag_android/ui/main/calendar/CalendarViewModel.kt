package com.icoo.ssgsag_android.ui.main.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.data.model.schedule.ScheduleRepository
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import io.reactivex.Single
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KClass

class CalendarViewModel(
    private val repository: ScheduleRepository
    , private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {
    val scheduleByDate = ArrayList<Schedule>()

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress

    // 스케쥴
    private val _schedule = MutableLiveData<ArrayList<Schedule>>()
    val schedule: LiveData<ArrayList<Schedule>> get() = _schedule
    private val _favoriteSchedule = MutableLiveData<ArrayList<Schedule>>()
    val favoriteSchedule : LiveData<ArrayList<Schedule>> get() = _favoriteSchedule

    private var _isUpdated = MutableLiveData<Boolean>()
    val isUpdated: LiveData<Boolean> get() = _isUpdated
    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart

    private val _deleteType = MutableLiveData<Int>()
    val deleteType : LiveData<Int> get() = _deleteType

    private val _listDate = MutableLiveData<String>()
    val listDate : LiveData<String> get() = _listDate
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite : LiveData<Boolean> get() = _isFavorite

    // header에 있는 date
    private val _headerDate = MutableLiveData<String>()
    val headerDate : LiveData<String> get() = _headerDate

    init {
        setIsFavorite(false)
        getAllCalendar()
        getFavoriteSchedule()
        setDeleteType(4)
       
    }

    fun getAllCalendar() {
        addDisposable(repository.getAllCalendar()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _schedule.postValue(this)

                    Log.e("schedule", "update")
                }
            }, {

            })
        )
    }

    fun getFavoriteSchedule(){
        addDisposable(repository.getFavoriteCalendar("0000", "00", "00")
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _favoriteSchedule.postValue(this)

                    Log.e("favorite schedule", "update")
                }
            }, {

            })
        )
    }

    fun filterScheduleFromCalendar (year: String, month: String): ArrayList<Schedule> {
        val cal = Calendar.getInstance()
        cal.set(year.toInt(), month.toInt(), 1)

        scheduleByDate.clear()
        // all
        if(!isFavorite.value!!){
            schedule.value?.let {
                for (i in it.indices) {
                    if (it[i].posterEndDate.substring(0, 4) == year
                        && it[i].posterEndDate.substring(5, 7) == month
                    ) {
                        scheduleByDate.add(it[i])
                    }
                }
            }
        }else{ // favorite
            favoriteSchedule.value?.let {
                for (i in it.indices) {
                    if (it[i].posterEndDate.substring(0, 4) == year
                        && it[i].posterEndDate.substring(5, 7) == month
                    ) {
                        scheduleByDate.add(it[i])
                    }
                }
            }
        }

        return scheduleByDate
    }

    fun filterScheduleFromList(year: String, month: String): ArrayList<Schedule> {

        val cal = Calendar.getInstance()
        cal.set(year.toInt(), month.toInt(), 1)

        scheduleByDate.clear()

        // all
        if(!isFavorite.value!!){
            schedule.value?.let {
                for (i in it.indices) {
                    if (it[i].isEnded == 0) {
                        scheduleByDate.add(it[i])
                    }
                }
            }
        }else{ // favorite
            favoriteSchedule.value?.let {
                for (i in it.indices) {
                    if (it[i].isEnded == 0) {
                        scheduleByDate.add(it[i])
                    }
                }
            }
        }
        return scheduleByDate
    }

    fun bookmark(posterIdx: Int, isBookmarked: Int) {
        lateinit var response: Single<Int>

        if(isBookmarked == 0) response = repository.bookmarkSchedule(posterIdx)
        else if(isBookmarked == 1) response = repository.unbookmarkSchedule(posterIdx)

        addDisposable(response
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                if(isFavorite.value!!){
                    getFavoriteSchedule()
                }else {
                    getAllCalendar()
                }
            }, {

            })
        )
    }

    fun deleteSchedule(posterIdxList: ArrayList<Int>) {

        val jsonObject = JSONObject()
        val jsonArray = JSONArray(posterIdxList)
        jsonObject.put("posterIdxList", jsonArray)

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(repository.deleteSchedule(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                getAllCalendar()
            }, {

            })
        )

    }



    fun setIsUpdated(isUpdated: Boolean){
        _isUpdated.setValue(isUpdated)
    }

    fun setDeleteType(type : Int){
        _deleteType.setValue(type)
        // 0: 선택 x, 1: 선택 가능, 2: 선택된 항목 있음
    }

    fun setHeaderDate(date: String){
        _headerDate.postValue(date)
    }

    fun setIsFavorite(bool: Boolean){
        _isFavorite.value = bool
    }


    fun navigate(idx: Int) {
        val bundle = Bundle().apply {
            putInt("Idx", idx)
            putString("from","calendar")
        }
        _activityToStart.postValue(Pair(CalendarDetailActivity::class, bundle))
    }

    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.INVISIBLE
    }
}
