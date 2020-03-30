package com.icoo.ssgsag_android.ui.main.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.review.club.ClubInfo
import com.icoo.ssgsag_android.data.model.review.club.ClubReviewRepository
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider


class ReviewViewModel(
    private val repository: ClubReviewRepository
    , private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private var _reviewList = MutableLiveData<ArrayList<ClubInfo>>()
    val reviewList : LiveData<ArrayList<ClubInfo>> get() = _reviewList

    fun getClubReviews(curPage: Int, clubType : Int){

        addDisposable(repository.getClubList(curPage, clubType)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                _reviewList.postValue(it)

            }, {
                it.printStackTrace()
            })
        )
    }

    fun getActReviews(curPage: Int){
        addDisposable(repository.getClubList(curPage, 0)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                _reviewList.postValue(it)

            }, {
                it.printStackTrace()
            })
        )
    }

    fun getInternReviews(curPage: Int){
        addDisposable(repository.getClubList(curPage, 0)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                _reviewList.postValue(it)

            }, {
                it.printStackTrace()
            })
        )
    }

}

