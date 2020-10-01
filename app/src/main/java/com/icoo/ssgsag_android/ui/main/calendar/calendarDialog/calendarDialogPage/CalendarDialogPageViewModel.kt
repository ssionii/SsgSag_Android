package com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.calendarDialogPage

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.poster.PosterRepository
import com.icoo.ssgsag_android.data.model.schedule.Schedule
import com.icoo.ssgsag_android.data.model.schedule.ScheduleRepository
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import io.reactivex.Single
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KClass

class CalendarDialogPageViewModel(
    private val repository: ScheduleRepository,
    private val posterRepository: PosterRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress
    private val _schedule = MutableLiveData<ArrayList<Schedule>>()
    val schedule: LiveData<ArrayList<Schedule>> get() = _schedule
    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart

    var pushAlarmList = MutableLiveData<ArrayList<Int>>()

    fun getAllSchedule(year: String, month: String, date: String) {
        addDisposable(repository.getCalendar(year, month, date)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _schedule.postValue(this)
                }
            }, {

            })
        )
    }


    fun getFavoriteSchedule(year: String, month: String, date: String) {
        addDisposable(repository.getFavoriteCalendar(year, month, date)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _schedule.postValue(this)
                }
            }, {

            })
        )
    }


    fun deleteSchedule(posterIdxList: ArrayList<Int>, year: String, month: String, date: String) {

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
                getAllSchedule(year, month, date)
            }, {

            })
        )

    }

    fun bookmark(posterIdx: Int, isFavorite: Int, year: String, month: String, date: String, showFavorite : Boolean) {
        lateinit var response: Single<Int>

        if(isFavorite == 0) response = repository.bookmarkSchedule(posterIdx)
        else if(isFavorite == 1) response = repository.unbookmarkSchedule(posterIdx)

        addDisposable(response
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                if(showFavorite) getFavoriteSchedule(year, month, date)
                else getAllSchedule(year, month, date)
            }, {

            })
        )

    }

    fun navigate(idx: Int) {
        val bundle = Bundle().apply {
            putInt("Idx", idx)
            putString("from", "calendar")
        }
        _activityToStart.postValue(Pair(CalendarDetailActivity::class, bundle))
    }

    fun getPushAlarm(posterIdx: Int){
        addDisposable(posterRepository.getTodoPushAlarm(posterIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                pushAlarmList.value = it
            }, {

            })
        )
    }

    fun bookmarkWithAlarm(posterIdx: Int, dday : Int, showFavorite: Boolean, year : String, month : String, date : String ){
        addDisposable(posterRepository.postTodoPushAlarm(posterIdx, dday.toString())
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                if(it == 204){
                    if(showFavorite) getFavoriteSchedule(year, month, date)
                    else getAllSchedule(year, month, date)

                    Toast.makeText(context, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }, {

            })
        )
    }

    fun unBookmarkWithAlarm(posterIdx: Int, showFavorite: Boolean, year : String, month : String, date : String){
        addDisposable(posterRepository.deleteTodoPushAlarm(posterIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                if(it == 204){
                    if(showFavorite) getFavoriteSchedule(year, month, date)
                    else getAllSchedule(year, month, date)

                    Toast.makeText(context, "즐겨찾기가 해제되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }, {

            })
        )
    }




    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.INVISIBLE
    }

    companion object {
        private val TAG = "CalendarDialogPageViewModel"
    }
}