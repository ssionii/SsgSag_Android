package com.icoo.ssgsag_android.ui.main.ssgSag.todaySwipePoster

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.poster.Poster
import com.icoo.ssgsag_android.data.model.poster.PosterRepository
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import kotlin.reflect.KClass

class TodaySwipePosterViewModel(
    private val repository : PosterRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel()
{

    var clickYes = false
    var clickNo = false

    private val _sagPosters = MutableLiveData<ArrayList<PosterDetail>>()
    val sagPosters : LiveData<ArrayList<PosterDetail>> get() = _sagPosters

    private val _ssgPosters = MutableLiveData<ArrayList<PosterDetail>>()
    val ssgPosters : LiveData<ArrayList<PosterDetail>> get() = _ssgPosters

    private val _totalSize = MutableLiveData<Int>()
    val totalSize : LiveData<Int> get() = _totalSize

    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart
    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress

    init {
        _totalSize.setValue(0)
        getTodaySwipePosters()
    }

    fun getTodaySwipePosters(){

        addDisposable(repository.getTodaySsgSag()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _sagPosters.postValue(this.sagSummaryPosterList)
                    _ssgPosters.postValue(this.ssgSummaryPosterList)

                }
            }, {

            })
        )
    }

    fun getTodaySwipePosterSize(){

        addDisposable(repository.getTodaySsgSag()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                it.run {
                    _totalSize.setValue(this.sagSummaryPosterList.size + this.ssgSummaryPosterList.size)
                }
            }, {

            })
        )

    }

    fun navigate(idx: Int) {
        val bundle = Bundle().apply {
            putInt("Idx", idx)
            putString("from","main")
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
