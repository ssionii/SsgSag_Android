package com.icoo.ssgsag_android.ui.main.community.feed

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.data.model.feed.FeedCategory
import com.icoo.ssgsag_android.data.model.feed.FeedRepository
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import kotlin.reflect.KClass


class FeedViewModel(
    private val repository: FeedRepository
    , private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress

    private val _bestFeedList = MutableLiveData<ArrayList<Feed>>()
    val bestFeedList :  LiveData<ArrayList<Feed>>  = _bestFeedList
    private val _feedList = MutableLiveData<ArrayList<Feed>>()
    val feedList : LiveData<ArrayList<Feed>> get() = _feedList

    private val _feed = MutableLiveData<Feed>()
    val feed : LiveData<Feed> get() = _feed

    var bestFeedBookmarkStatus = MutableLiveData<Int>()
    var feedBookmarkStatus = MutableLiveData<Int>()
    lateinit var refreshedFeed : Feed
    var refreshedFeedPosition = 0


   init {

   }

    fun getTodayFeeds(curPage: Int, pageSize: Int){
        addDisposable(repository.getTodayFeeds(curPage, pageSize)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                if(it.bestFeedList != null) _bestFeedList.value = it.bestFeedList!!
                _feedList.value = it.feedList!!
            },{

            })
        )
    }


    fun getFeed(idx: Int){
        addDisposable(repository.getFeed(idx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run{
                    _feed.setValue(this)
                }

            }, {
                Log.e("get feed error", it.message)
            })
        )
    }

    // 조회수 안 올라감
    fun refreshFeed(idx : Int){
        addDisposable(repository.getFeedRefresh(idx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                it.run {
                    _feed.value = this
                }

            }, {
                Log.e("refresh feed error", it.message)
            })
        )

    }


    fun bookmarkFromWeb(feedIdx : Int){

        if(feed.value!!.isSave == 0) {
            addDisposable(repository.bookmarkFeed(feedIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    if(it == 200) {
                        Toast.makeText(SsgSagApplication.getGlobalApplicationContext(),
                            "[내 정보 > 스크랩한 글] 에 보관되었습니다.",Toast.LENGTH_SHORT).show()
                        feedBookmarkStatus.value = it
                        refreshFeed(feedIdx)
                    }
                }, {

                })
            )
        }else {
            addDisposable(repository.unbookmarkFeed(feedIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    if(it == 200) {

                        feedBookmarkStatus.value = it
                        refreshFeed(feedIdx)
                    }
                }, {

                })
            )
        }
    }

    fun bookmark(feedItem : Feed, position : Int, isBest : Boolean){

        if(feedItem.isSave == 0) {
            addDisposable(repository.bookmarkFeed(feedItem.feedIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    if(it == 200) {
                        Toast.makeText(SsgSagApplication.getGlobalApplicationContext(),
                            "[내 정보 > 스크랩한 글] 에 보관되었습니다.",Toast.LENGTH_SHORT).show()

                        refreshedFeed = feedItem
                        refreshedFeed.isSave = 1
                        refreshedFeedPosition = position

                        if(isBest) bestFeedBookmarkStatus.value = it
                        else feedBookmarkStatus.value = it

                    }
                }, {

                })
            )
        }else {
            addDisposable(repository.unbookmarkFeed(feedItem.feedIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    if(it == 200) {
                        refreshedFeed = feedItem
                        refreshedFeed.isSave = 0
                        refreshedFeedPosition = position

                        if(isBest) bestFeedBookmarkStatus.value = it
                        else feedBookmarkStatus.value = it
                    }
                }, {

                })
            )
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