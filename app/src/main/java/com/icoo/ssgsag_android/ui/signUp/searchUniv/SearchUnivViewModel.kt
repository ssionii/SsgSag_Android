package com.icoo.ssgsag_android.ui.signUp.searchUniv

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.login.LoginRepository
import com.icoo.ssgsag_android.data.model.signUp.SignupRepository
import com.icoo.ssgsag_android.data.model.signUp.University
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class SearchUnivViewModel(
    private val repository: SignupRepository,
    private val schedulerProvider: SchedulerProvider
): BaseViewModel() {

    private val _univList = MutableLiveData<ArrayList<University>>()
    val univList: LiveData<ArrayList<University>> get() = _univList

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress

    init{
        getUnivList()
    }

    fun getUnivList(){
        addDisposable(repository.getUnivList()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .doOnError {
                Log.e("getUnivList error", it.message)
            }
            .subscribe({
                _univList.value = it
            }, {
                it.printStackTrace()
            }))
    }

    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.INVISIBLE
    }

}