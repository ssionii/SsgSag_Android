package com.icoo.ssgsag_android.ui.main.allPosters

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.ads.AdItem
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.data.model.poster.PosterRepository
import com.icoo.ssgsag_android.data.model.poster.allPoster.AdPosterCollection
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.data.model.schedule.ScheduleRepository
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KClass

class AllPostersViewModel(
    private val posterRepository: PosterRepository,
    private val scheduleRepository : ScheduleRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private var categorySet = arrayListOf(
        Category(0, true, ""),
        Category(1, false, ""),
        //Category(2, false),
        Category(4, false, ""),
        Category(7, false, ""),
        Category(5, false, "")
        //Category(8, false)
    )

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress
    private var _categorySort = MutableLiveData<ArrayList<Category>>()
    val categorySort: LiveData<ArrayList<Category>> get() = _categorySort
    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart

    private var _mainAdList = MutableLiveData<ArrayList<AdPosterCollection>>()
    val mainAdList : LiveData<ArrayList<AdPosterCollection>> = _mainAdList
    private var _posterList = MutableLiveData<ArrayList<AdPosterCollection>>()
    val posterList : LiveData<ArrayList<AdPosterCollection>> = _posterList
    private var _eventList = MutableLiveData<ArrayList<AdPosterCollection>>()
    val eventList : LiveData<ArrayList<AdPosterCollection>> = _eventList

    private val _category = MutableLiveData<Int>()
    val category: LiveData<Int> get() = _category
    private val _sortType = MutableLiveData<Int>()
    val sortType: LiveData<Int> get() = _sortType

    init{
        _category.setValue(0)
        _sortType.setValue(0)
        _categorySort.setValue(categorySet)


        getAdPosterCollection()

    }

    fun getAdPosterCollection(){
        addDisposable(posterRepository.getAllPosterAd()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("getAllPosterAd error", it.message)
            }
            .subscribe({
//                _mainAdList.value = it.mainAdList
                _mainAdList.value = null
                _posterList.value = it.posterList
                _eventList.value = it.eventList
            }, {
                it.printStackTrace()
            }))
    }


    fun managePoster(posterIdx: Int, isSave: Int) {
        if(isSave == 0) {
            addDisposable(
                posterRepository.saveAtPosterDetail(posterIdx)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.mainThread())
                    .subscribe({
//                        getAdPosterCollection()
                        Toast.makeText(
                            SsgSagApplication.getGlobalApplicationContext(),
                            "캘린더에 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    }, {

                    })
            )
        }else if(isSave == 1){
            val posterIdxList = arrayListOf(posterIdx)

            val jsonObject = JSONObject()
            val jsonArray = JSONArray(posterIdxList)
            jsonObject.put("posterIdxList", jsonArray)

            val body = JsonParser().parse(jsonObject.toString()) as JsonObject

            addDisposable(scheduleRepository.deleteSchedule(body)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    Toast.makeText(
                        SsgSagApplication.getGlobalApplicationContext(),
                        "캘린더에서 삭제되었습니다!", Toast.LENGTH_SHORT).show()
                }, {
                    it.printStackTrace()
                })
            )

        }else{
            Toast.makeText(
                SsgSagApplication.getGlobalApplicationContext(),
                "포스터 정보가 없습니다", Toast.LENGTH_SHORT).show();
        }
    }

    fun navigate(idx: Int) {
        val bundle = Bundle().apply {
            putInt("Idx", idx)
            putString("from","main")
            putString("from","what")
        }
        _activityToStart.postValue(Pair(CalendarDetailActivity::class, bundle))
    }

}