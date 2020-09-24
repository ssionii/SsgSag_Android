package com.icoo.ssgsag_android.ui.main.community

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.community.*
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.data.model.poster.allPoster.AdPosterCollection
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class CommunityViewModel(
    private val repository: CommunityRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){


    private var _collectionItem = MutableLiveData<CommunityMainCollection>()
    val collectionItem : LiveData<CommunityMainCollection> = _collectionItem

    private var _feedList = MutableLiveData<ArrayList<Feed>>()
    val feedList : LiveData<ArrayList<Feed>> = _feedList

    init{
        getCommunityMain()
    }

    fun getCommunityMain(){
        addDisposable(repository.getCommunityMain()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("get community error", it.message)
            }
            .subscribe({
                _collectionItem.value = it
                _feedList.value = it.feedList

            }) {
                Log.e("get community error", it.message)
            })
    }
}