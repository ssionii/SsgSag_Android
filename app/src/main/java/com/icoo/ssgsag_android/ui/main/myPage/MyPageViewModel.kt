package com.icoo.ssgsag_android.ui.main.myPage

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.user.userInfo.UserInfo
import com.icoo.ssgsag_android.data.model.user.UserRepository
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import android.os.Bundle
import android.util.Log
import com.icoo.ssgsag_android.data.model.community.board.PostInfo
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.data.model.user.myBoard.MyComment
import com.icoo.ssgsag_android.ui.main.myPage.accountMgt.AccountMgtActivity
import com.icoo.ssgsag_android.ui.main.myPage.career.CareerActivity
import com.icoo.ssgsag_android.ui.main.myPage.contact.ContactActivity
import com.icoo.ssgsag_android.ui.main.myPage.notice.NoticeActivity
import com.icoo.ssgsag_android.ui.main.myPage.pushAlarm.PushAlarmActivity
import com.icoo.ssgsag_android.ui.main.myPage.serviceInfo.ServiceInfoActivity
import com.icoo.ssgsag_android.ui.main.subscribe.SubscribeActivity
import kotlin.reflect.KClass

class MyPageViewModel(
    private val repository: UserRepository
    , private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress
    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> get() = _userInfo

    private val _myCommentList = MutableLiveData<ArrayList<MyComment>>()
    val myCommentList: LiveData<ArrayList<MyComment>> get() = _myCommentList

    private val _bookmarkedPostList = MutableLiveData<ArrayList<PostInfo>>()
    val bookmarkedPostList: LiveData<ArrayList<PostInfo>> get() = _bookmarkedPostList
    private val _bookmarkedFeedList = MutableLiveData<ArrayList<Feed>>()
    val bookmarkedFeedList: LiveData<ArrayList<Feed>> get() = _bookmarkedFeedList

    var dataList = MutableLiveData<ArrayList<*>>()


    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart


    fun getUserInfo() {
        addDisposable(repository.getUserInfo()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _userInfo.postValue(this)
                }
            }, {

            })
        )
    }

    fun getMyComment(curPage : Int, pageSize : Int){
        addDisposable(repository.getMyComment(curPage, pageSize)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    if(this.size > 0 ) {
                        _myCommentList.postValue(this)
                        dataList.value = this
                    }
                }
            }, {

            })
        )
    }

    fun getBookmarkedPost(curPage : Int, pageSize : Int){
        addDisposable(repository.getBookmarkedPost(curPage, pageSize)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    if(this.size > 0 ) {
                        _bookmarkedPostList.value = it
                        dataList.value = it
                    }
                }
            }, {

            })
        )
    }

    fun getBookmarkedFeed(curPage : Int){
        addDisposable(repository.getBookmarkedFeed(curPage)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    if(this.size > 0 ) {
                        _bookmarkedFeedList.value = it
                        dataList.value = it
                    }
                }
            }, {

            })
        )
    }


    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.INVISIBLE
    }

    companion object {
        private val TAG = "MyPageViewModel"
    }
}