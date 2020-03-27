package com.icoo.ssgsag_android.ui.main.calendar

import android.os.Bundle
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

    private var categorySet = arrayListOf(
        Category(-2, true, ""),
        Category(-1, false, ""),
        Category(0, false, ""),
        Category(1, false, ""),
        //Category(2, false),
        Category(4, false, ""),
        Category(7, false, ""),
        Category(5, false, "")
        //Category(8, false)
    )

    private var subscribeSet = ArrayList<Int>()

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress
    private val _schedule = MutableLiveData<ArrayList<Schedule>>()
    val schedule: LiveData<ArrayList<Schedule>> get() = _schedule
    private var _categorySort = MutableLiveData<ArrayList<Category>>()
    val categorySort: LiveData<ArrayList<Category>> get() = _categorySort
    private var _isUpdated = MutableLiveData<Boolean>()
    val isUpdated: LiveData<Boolean> get() = _isUpdated
    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart

    private val _deleteType = MutableLiveData<Int>()
    val deleteType : LiveData<Int> get() = _deleteType

    private val _listDate = MutableLiveData<String>()
    val listDate : LiveData<String> get() = _listDate

    init {
        _categorySort.setValue(categorySet)
        getAllCalendar()
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
                }
            }, {

            })
        )
    }


    fun getCalendarByCategory(year: String, month: String, flag : String =""): ArrayList<Schedule> {
        val cal = Calendar.getInstance()
        cal.set(year.toInt(), month.toInt(), 1)
        val endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        scheduleByDate.clear()

        if(flag == "list") {
            schedule.value?.let {
                for (i in it.indices) {
                    if (compareCate(it[i].categoryIdx)
                        && it[i].isEnded == 0
                    ) {
                        scheduleByDate.add(it[i])
                    }
                }
            }
        }else{
            schedule.value?.let {
                for (i in it.indices) {
                    if (it[i].posterEndDate.substring(0, 4) == year
                        && it[i].posterEndDate.substring(5, 7) == month
                        && compareCate(it[i].categoryIdx)
                    ) {
                        scheduleByDate.add(it[i])
                    }
                }
            }
        }

        return scheduleByDate
    }

    fun getCalendarByBookMark(year: String, month: String, flag : String = ""): ArrayList<Schedule> {
        val cal = Calendar.getInstance()
        cal.set(year.toInt(), month.toInt(), 1)
        val endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        scheduleByDate.clear()

        if(flag == "list") {
            schedule.value?.let {
                for (i in it.indices) {
                    if (it[i].isFavorite == 1
                        && it[i].isEnded == 0
                    ) {
                        scheduleByDate.add(it[i])
                    }
                }
            }
        }else{
            schedule.value?.let {
                for (i in it.indices) {
                    if (it[i].posterEndDate.substring(0, 4) == year
                        && it[i].posterEndDate.substring(5, 7) == month
                        && it[i].isFavorite == 1
                    ) {
                        scheduleByDate.add(it[i])
                    }
                }
            }
        }

        return scheduleByDate
    }

    private fun compareCate(categoryIdx: Int) : Boolean{

        if(categorySort.value!![0].isChecked)
            return true

        if(categoryIdx < 2) {
            return categorySet[categoryIdx + 2].isChecked
        }else if(categoryIdx == 4){
            return categorySet[categoryIdx].isChecked
        }else if(categoryIdx == 7){
            return categorySet[5].isChecked
        }else if(categoryIdx == 5){
            return categorySet[categorySet.size-1].isChecked
        }else
            return false
    }

    fun checkCate(categoryIdx: Int) {
        // UI 처리
        when (categoryIdx) {
            -2 -> {
                _categorySort.postValue(categorySet.apply {
                    for (i in categorySet.indices) {
                        if(i == 0)
                            this[i].isChecked = true
                        else
                            this[i].isChecked = false
                    }
                })

                /*
                if (!categorySet[0].isChecked) {
                    _categorySort.postValue(categorySet.apply {
                        for (i in categorySet.indices) {
                            this[i].isChecked = false
                            //if (this[i].categoryIdx != -1) this[i].isChecked = true else this[i].isChecked = false
                        }
                    })
                } else {
                    _categorySort.postValue(categorySet.apply {
                        for (i in categorySet.indices) {
                            this[i].isChecked = false
                        }
                    })
                }*/
            }
            -1 -> {
                if (!categorySet[1].isChecked) {
                    _categorySort.postValue(
                        categorySet.apply {
                            for (i in categorySet.indices) {
                                this[i].isChecked = false
                            }
                            categorySet[1].isChecked = true
                        })

                    // TODO: 좋아요 누른 것 띄우기
                }
            }
            else -> {
                if(categoryIdx < 3) {
                    categorySet[categoryIdx + 2].isChecked = !categorySet[categoryIdx + 2].isChecked
                }else if(categoryIdx == 4){
                    categorySet[categoryIdx].isChecked = !categorySet[categoryIdx].isChecked
                }else if(categoryIdx == 7){
                    categorySet[5].isChecked = !categorySet[5].isChecked
                } else if(categoryIdx == 5){
                    categorySet[categorySet.size -1 ].isChecked = !categorySet[categorySet.size -1].isChecked
                }

                categorySet[0].isChecked = false
                categorySet[1].isChecked = false


                /*
                for (i in 2 until categorySet.size) {
                    if (!categorySet[i].isChecked) {
                        categorySet[0].isChecked = false
                        break
                    }
                    if (i == categorySet.size - 1) categorySet[0].isChecked = true
                }*/

                _categorySort.postValue(categorySet)

            }

        }

    }

    fun navigate(idx: Int) {
        val bundle = Bundle().apply {
            putInt("Idx", idx)
            putString("from","calendar")
        }
        _activityToStart.postValue(Pair(CalendarDetailActivity::class, bundle))
    }


    fun getSchedule(year: String, month: String, date: String){
        addDisposable(repository.getCalendar(year, month, date, arrayListOf())
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


    fun getBookMarkedSchedule() {
        addDisposable(repository.getSubscribe()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                it.run {
                    for(i in 0..this.size-1){
                        if(this[i].userIdx != 0){
                            subscribeSet.add(this[i].interestIdx)
                        }
                    }
                }
            }, {

            })
        )
    }

    fun setIsUpdated(isUpdated: Boolean){
        _isUpdated.setValue(isUpdated)
    }

    fun bookmark(posterIdx: Int, isFavorite: Int) {
        lateinit var response: Single<Int>

        if(isFavorite == 0) response = repository.bookmarkSchedule(posterIdx)
        else if(isFavorite == 1) response = repository.unbookmarkSchedule(posterIdx)

        addDisposable(response
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

    fun setDeleteType(type : Int){
        _deleteType.setValue(type)

        // 0: 선택 x, 1: 선택 가능, 2: 선택된 항목 있음
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

    fun setListDate(date: String){
        _listDate.setValue(date.substring(0,4) + "년 " + date.substring(5,7) + "월")
    }

    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.INVISIBLE
    }
}
