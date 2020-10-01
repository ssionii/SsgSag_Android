package com.icoo.ssgsag_android.ui.main.userNotice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.user.UserRepository
import com.icoo.ssgsag_android.data.model.user.userNotice.UserNotice
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class UserNoticeViewModel(
    private val repository: UserRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){


    private val _noticeList = MutableLiveData<ArrayList<UserNotice>>()
    val noticeList : LiveData<ArrayList<UserNotice>> get() = _noticeList

    fun getUserNotice(curPage : Int, pageSize : Int){
        addDisposable(repository.getUserNotice(curPage, pageSize)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnError {
                Log.e("get user notice error", it.message)
            }
            .subscribe({
                _noticeList.value = it
            }) {
                Log.e("get user notice error", it.message)
            })
    }
}