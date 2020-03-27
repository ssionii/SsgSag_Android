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

    /*
    fun navigate(idx: Int) {
        when (idx) {
            0 -> _activityToStart.postValue(Pair(AccountMgtActivity::class, null))
            1 -> _activityToStart.postValue(Pair(CareerActivity::class, null)) // 나의 이력
            2 -> _activityToStart.postValue(Pair(PushAlarmActivity::class, null)) // 푸시 알림 설정
            3 -> _activityToStart.postValue(Pair(NoticeActivity::class, null)) // 공지사항
            4 -> _activityToStart.postValue(Pair(ContactActivity::class, null)) // 문의하기
            else ->
                Log.d(TAG, idx.toString())
        }
    }*/

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