package com.icoo.ssgsag_android.ui.main.feed

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

    private var categorySet = arrayListOf(
        Category(100, true, ""),
        Category(101, false, ""),
        Category(102, false, ""),
        Category(103, false, "")
    )

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress

    // feed 상단 anchor
    private var _feedCategoryList = MutableLiveData<ArrayList<Category>>()
    val feedCategoryList : LiveData<ArrayList<Category>> get() = _feedCategoryList

    // best feeds
    private var _bestFeeds = MutableLiveData<ArrayList<Feed>>()
    val bestFeeds : LiveData<ArrayList<Feed>> get() = _bestFeeds

    // category별 feed들 (대학생활, 취업뉴스)
    private var _categoryFeeds = MutableLiveData<ArrayList<FeedCategory>>()
    val categoryFeeds : LiveData<ArrayList<FeedCategory>> get() = _categoryFeeds

    // category별 feed들
    private var _careerFeeds = MutableLiveData<ArrayList<FeedCategory>>()
    val careerFeeds : LiveData<ArrayList<FeedCategory>> get() = _careerFeeds

    // 선택된 categoryList
    private var _category = MutableLiveData<Int>()
    val category : LiveData<Int> get() = _category

    // 더보기 눌렀을 때 10개씩 받아오는 feed들
    private var _tmpFeeds = MutableLiveData<ArrayList<Feed>>()
    val tmpFeeds: LiveData<ArrayList<Feed>> get() = _tmpFeeds

    // 북마크 activity에서 보는 feed들
    private val _bookmarkedFeeds = MutableLiveData<ArrayList<Feed>>()
    val bookmarkedFeeds : LiveData<ArrayList<Feed>> get() = _bookmarkedFeeds

    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart


    private val _feed = MutableLiveData<Feed>()
    val feed : LiveData<Feed> get() = _feed

    private val _refreshedFeed = MutableLiveData<Feed>()
    val refreshedFeed : LiveData<Feed> get() = _refreshedFeed

    var tmpBookmarkedFeeds = ArrayList<Feed>()
    var refreshedFeedPosition = 0
    var refreshedFeedIdx = 0

    lateinit var tmpFeed: Feed
    lateinit var tmpRefreshedFeed: Feed


    init{
        _feedCategoryList.setValue(categorySet)
        getTodayFeeds()
    }

    fun checkCate(position: Int){
        for(i in  0..categorySet.size-1){
            categorySet[i].isChecked = false
        }

        categorySet[position].isChecked = true
        _feedCategoryList.postValue(categorySet)
    }

    fun getTodayFeeds(){
        addDisposable(repository.getTodayFeeds()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                if(it != null) {
                    _bestFeeds.setValue(it[0].feedList)
                    var tmpFeeds = arrayListOf(it[1], it[2])
                    _categoryFeeds.setValue(tmpFeeds)
                    tmpFeeds = arrayListOf(it[3], it[4], it[5])
                    _careerFeeds.setValue(tmpFeeds)
                }
            },{

            })
        )
    }

    fun getCategoryFeeds(curPage: Int, categoryIndex : Int){
        addDisposable(repository.getCategoryFeeds(curPage, categoryIndex)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _tmpFeeds.setValue(this) // 10개씩 쪼개서 가져온 데이터
                }
            }, {
            })
        )
    }

    fun getBookmarkedFeeds(curPage: Int){
        addDisposable(repository.getBookmarkedFeeds(curPage)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _bookmarkedFeeds.setValue(this)
                    if(curPage == 0){
                        tmpBookmarkedFeeds.clear()
                    }
                        tmpBookmarkedFeeds.addAll(this)
                }
            }, {

            })
        )
    }

    fun getFeed(feedIdx: Int){
        addDisposable(repository.getFeed(feedIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run{
                    _feed.setValue(this)
                    _refreshedFeed.postValue(this)
                }

            }, {

            })
        )
    }

    // 조회수 안 올라감
    fun refreshFeed(){
        if(refreshedFeedIdx != 0) {
            addDisposable(repository.getFeedRefresh(refreshedFeedIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnTerminate { hideProgress() }
                .subscribe({
                    it.run {
                        _refreshedFeed.setValue(this)
                        tmpRefreshedFeed = this
                    }

                }, {

                })
            )
        }
    }


    fun navigate(url: String, idx:Int, position: Int) {
        val bundle = Bundle().apply {
            putString("clubWebsite", url)
            putInt("idx", idx)
        }

        _activityToStart.postValue(Pair(FeedWebActivity::class, bundle))
        refreshedFeedPosition = position
        refreshedFeedIdx = idx

    }

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

    fun setCategory(categoryIdx: Int){
        _category.postValue(categoryIdx)
        getCategoryFeeds(0, categoryIdx)
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