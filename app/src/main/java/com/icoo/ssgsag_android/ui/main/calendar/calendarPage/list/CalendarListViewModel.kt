package com.icoo.ssgsag_android.ui.main.calendar.calendarPage.list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.data.model.schedule.ScheduleRepository
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import io.reactivex.Single
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KClass

class CalendarListViewModel (
    private val repository: ScheduleRepository
    , private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {
    val scheduleByDate = ArrayList<Schedule>()

    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart

    // 스케쥴
    private val _schedule = MutableLiveData<ArrayList<Schedule>>()
    val schedule: LiveData<ArrayList<Schedule>> get() = _schedule

    private val _favoriteSchedule = MutableLiveData<ArrayList<Schedule>>()
    val favoriteSchedule : LiveData<ArrayList<Schedule>> get() = _favoriteSchedule

    private var _isUpdated = MutableLiveData<Boolean>()
    val isUpdated: LiveData<Boolean> get() = _isUpdated

    var isFavorite = MutableLiveData<Boolean>()
    var isLastSaveFilter = MutableLiveData<Boolean>()

    init {
        isFavorite.value = false
        isLastSaveFilter.value = true
        getAllCalendar()
        getFavoriteSchedule()
    }

    fun getAllCalendar() {
        var sortType = 1
        if(!isLastSaveFilter.value!!) sortType = 0

        addDisposable(repository.getAllCalendar(sortType)
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

    fun getFavoriteSchedule(){
        var sortType = 1
        if(!isLastSaveFilter.value!!) sortType = 0

        addDisposable(repository.getFavoriteCalendar("0000", "00", "00", sortType)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                it.run {
                    _favoriteSchedule.postValue(this)
                }
            }, {

            })
        )
    }

    fun filterScheduleFromList(year: String, month: String, isFavorite: Boolean): ArrayList<Schedule> {

        val cal = Calendar.getInstance()
        cal.set(year.toInt(), month.toInt(), 1)

        scheduleByDate.clear()

        // all
        if(!isFavorite){
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

    fun bookmark(posterIdx: Int, isBookmarked: Int, position: Int) {
        lateinit var response: Single<Int>

        if(isBookmarked == 0) response = repository.bookmarkSchedule(posterIdx)
        else if(isBookmarked == 1) response = repository.unbookmarkSchedule(posterIdx)

        addDisposable(response
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                getFavoriteSchedule()
            }, {

            })
        )
    }


    fun navigate(idx: Int) {
        val bundle = Bundle().apply {
            putInt("Idx", idx)
            putString("from","calendar")
        }
        _activityToStart.postValue(Pair(CalendarDetailActivity::class, bundle))
    }

}