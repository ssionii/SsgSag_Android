package com.icoo.ssgsag_android.ui.main.review.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.review.Banner
import com.icoo.ssgsag_android.data.model.review.ReviewRepository
import com.icoo.ssgsag_android.ui.main.review.ReviewViewModel
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class ReviewMainViewModel(
    val repository: ReviewRepository,
    val schedulerProvider: SchedulerProvider
): BaseViewModel(){

    private var _adList = MutableLiveData<ArrayList<Banner>>()
    val adList : LiveData<ArrayList<Banner>> get() = _adList

    init {

      getAds()
    }

    fun getAds(){
        addDisposable(repository.getAds()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                it.run {

                    _adList.postValue(this)
                }
            }, {
            })
        )
    }
}