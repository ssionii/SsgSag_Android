package com.icoo.ssgsag_android.ui.main.allPosters.search

import android.os.Bundle
import com.icoo.ssgsag_android.data.model.poster.PosterRepository
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.review.club.ClubInfo
import com.icoo.ssgsag_android.data.model.review.club.ClubReviewRepository
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailActivity
import com.icoo.ssgsag_android.ui.main.review.club.ReviewDetailActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import kotlin.reflect.KClass


class SearchViewModel(
    private val posterRepository: PosterRepository,
    private val reviewRepository: ClubReviewRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress

    private val _posterResult = MutableLiveData<ArrayList<PosterDetail>>()
    val posterResult : LiveData<ArrayList<PosterDetail>> get() = _posterResult
    private val _clubResult = MutableLiveData<ArrayList<ClubInfo>>()
    val clubResult : LiveData<ArrayList<ClubInfo>> get() = _clubResult

    private val _resultSize = MutableLiveData<Int>()
    val resultSize : LiveData<Int> get() = _resultSize

    private val _keyword = MutableLiveData<String>()
    val keyword : LiveData<String> get() = _keyword

    private val _refreshedPoster = MutableLiveData<PosterDetail>()
    val refreshedPoster : LiveData<PosterDetail> get() = _refreshedPoster

    var refreshedPosterIdx = 0
    var refreshedPosterPosition = 0

    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart


    fun getSearchedPosters(keyword:String, curPage:Int){
        addDisposable(posterRepository.getSearchPosters(keyword, curPage)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _posterResult.postValue(this)
                    _keyword.setValue(keyword)

                    _resultSize.postValue(this.size)
                }
            }, {

            })
        )
    }

    fun getRefreshedPoster(){
        addDisposable(posterRepository.getPoster(refreshedPosterIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                it.run {
                    _refreshedPoster.postValue(this)
                }
            }, {

            })
        )
    }

    fun getSearchedClubs(keyword: String, curPage: Int){
        addDisposable(reviewRepository.searchClubName(keyword, curPage)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _clubResult.postValue(this)
                    _keyword.setValue(keyword)

                    _resultSize.postValue(this.size)
                }
            }, {

            })
        )
    }


    fun navigate(idx: Int, position :Int, from: String) {
        if(from == "main") {
            val bundle = Bundle().apply {
                putInt("Idx", idx)
                putString("from", "main")
            }
            _activityToStart.postValue(Pair(CalendarDetailActivity::class, bundle))
            refreshedPosterPosition = position
            refreshedPosterIdx = idx
        }else if(from == "club"){
            val bundle = Bundle().apply {
                putInt("clubIdx", idx)
            }
            _activityToStart.postValue(Pair(ReviewDetailActivity::class, bundle))
        }
    }

    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.INVISIBLE
    }

    companion object {
        private val TAG = "FeedViewModel"
    }
}