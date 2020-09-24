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

//    fun getCategoryFeeds(curPage: Int, categoryIndex : Int){
//        addDisposable(repository.getCategoryFeeds(curPage, categoryIndex)
//            .subscribeOn(schedulerProvider.io())
//            .observeOn(schedulerProvider.mainThread())
//            .doOnSubscribe { showProgress() }
//            .doOnTerminate { hideProgress() }
//            .subscribe({
//                it.run {
//                    _tmpFeeds.setValue(this) // 10개씩 쪼개서 가져온 데이터
//                }
//            }, {
//            })
//        )
//    }

//    fun getBookmarkedFeeds(curPage: Int){
//        addDisposable(repository.getBookmarkedFeeds(curPage)
//            .subscribeOn(schedulerProvider.io())
//            .observeOn(schedulerProvider.mainThread())
//            .doOnSubscribe { showProgress() }
//            .doOnTerminate { hideProgress() }
//            .subscribe({
//                it.run {
//                    _bookmarkedFeeds.setValue(this)
//                    if(curPage == 0){
//                        tmpBookmarkedFeeds.clear()
//                    }
//                        tmpBookmarkedFeeds.addAll(this)
//                }
//            }, {
//
//            })
//        )
//    }
//
//    fun getFeed(feedIdx: Int){
//        addDisposable(repository.getFeed(feedIdx)
//            .subscribeOn(schedulerProvider.io())
//            .observeOn(schedulerProvider.mainThread())
//            .doOnSubscribe { showProgress() }
//            .doOnTerminate { hideProgress() }
//            .subscribe({
//                it.run{
//                    _feed.setValue(this)
//                    _refreshedFeed.postValue(this)
//                }
//
//            }, {
//
//            })
//        )
//    }
//
//    // 조회수 안 올라감
//    fun refreshFeed(){
//        if(refreshedFeedIdx != 0) {
//            addDisposable(repository.getFeedRefresh(refreshedFeedIdx)
//                .subscribeOn(schedulerProvider.io())
//                .observeOn(schedulerProvider.mainThread())
//                .doOnSubscribe { showProgress() }
//                .doOnTerminate { hideProgress() }
//                .subscribe({
//                    it.run {
//                        _refreshedFeed.setValue(this)
//                        tmpRefreshedFeed = this
//                    }
//
//                }, {
//
//                })
//            )
//        }
//    }
//
//
//    fun navigate(url: String, idx:Int, position: Int) {
//        val bundle = Bundle().apply {
//            putString("url", url)
//            putString("from", "feed")
//            putInt("idx", idx)
//        }
//
//        _activityToStart.postValue(Pair(FeedWebActivity::class, bundle))
//        refreshedFeedPosition = position
//        refreshedFeedIdx = idx
//
//    }
//
    fun bookmark(idx: Int, isSave: Int, position: Int, from: String = ""){

        if(isSave == 0) {
            addDisposable(repository.bookmarkFeed(idx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnTerminate { hideProgress() }
                .subscribe({
                    if(it == 200) {
                        Toast.makeText(SsgSagApplication.getGlobalApplicationContext(),
                            "북마크에 추가되었습니다.",Toast.LENGTH_SHORT).show()
                        if (from == "web") {
                            if (feed.value!!.isSave == 0) {
                                tmpFeed = feed.value!!.copy(isSave = 1)
                            } else {
                                tmpFeed = feed.value!!.copy(isSave = 0)
                            }
                            _feed.postValue(tmpFeed)
                        } else if(from == "today"){
                            getTodayFeeds()
                        }
                        else{
                            refreshedFeedPosition = position
                            refreshedFeedIdx = idx
                            refreshFeed()
                        }
                    }
                }, {

                })
            )
        }else if(isSave == 1){
            addDisposable(repository.unbookmarkFeed(idx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnTerminate { hideProgress() }
                .subscribe({
                    if(it == 200) {
                        Toast.makeText(SsgSagApplication.getGlobalApplicationContext(),
                            "북마크에서 삭제되었습니다.",Toast.LENGTH_SHORT).show()

                        if (from == "web") {
                            if (feed.value!!.isSave == 0) {
                                tmpFeed = feed.value!!.copy(isSave = 1)
                            } else {
                                tmpFeed = feed.value!!.copy(isSave = 0)
                            }
                            _feed.postValue(tmpFeed)
                            //unBookmarkedFeed(refreshedFeedPosition)

                        }else if(from == "today"){
                            getTodayFeeds()
                        } else{
                            refreshedFeedPosition = position
                            refreshedFeedIdx = idx
                            refreshFeed()
                        }

                    }

                }, {

                })
            )
        }else {
            return
        }
    }

//    fun setCategory(categoryIdx: Int){
//        _category.postValue(categoryIdx)
//        getCategoryFeeds(0, categoryIdx)
//    }



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