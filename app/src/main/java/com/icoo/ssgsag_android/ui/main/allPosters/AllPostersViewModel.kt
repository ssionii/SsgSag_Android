package com.icoo.ssgsag_android.ui.main.allPosters

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.data.model.poster.PosterRepository
import com.icoo.ssgsag_android.data.model.poster.allPoster.AdPosterCollection
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import kotlin.reflect.KClass

class AllPostersViewModel(
    private val repository: PosterRepository,
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

    private var _posterList = MutableLiveData<ArrayList<AdPosterCollection>>()
    val posterList : LiveData<ArrayList<AdPosterCollection>> = _posterList

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

    private fun getAdPosterCollection(){
        addDisposable(repository.getAllPosterAd()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("getAllPosterAd error", it.message)
            }
            .subscribe({
                _posterList.value = it.posterList
            }, {
                it.printStackTrace()
            }))
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