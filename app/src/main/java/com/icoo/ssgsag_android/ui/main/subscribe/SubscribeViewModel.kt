package com.icoo.ssgsag_android.ui.main.subscribe

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.subscribe.Subscribe
import com.icoo.ssgsag_android.data.model.subscribe.SubscribeRepository
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class SubscribeViewModel(
    private val repository: SubscribeRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress
    private val _subscribe = MutableLiveData<ArrayList<Subscribe>>()
    val subscribe: LiveData<ArrayList<Subscribe>> get() = _subscribe

    init {
        getSubscribe()
    }

    fun getSubscribe() {
        addDisposable(repository.getSubscribe()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    var orderList = ArrayList<Subscribe>()
                    lateinit var etcSubscribe: Subscribe

                    for (i in 0..this.size - 1) {
                        if (this[i].interestIdx == 5) {
                            etcSubscribe = this[i]
                        } else if (this[i].interestIdx != 2 &&
                                this[i].interestIdx !=8){
                            orderList.add(this[i])
                        }

                    }
                    orderList.add(etcSubscribe)
                    _subscribe.setValue(orderList)
                }
            }, {
            })
        )

    }

    fun subscribe(interestIdx: Int, userIdx: Int) {
        if (userIdx == 0) {
            //subscribe
            addDisposable(repository.subscribe(interestIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnTerminate { hideProgress() }
                .subscribe({
                    getSubscribe()
                }, {

                })
            )
        } else {
            //unsubscribe
            addDisposable(repository.unsubscribe(interestIdx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnTerminate { hideProgress() }
                .subscribe({
                    getSubscribe()
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
        private val TAG = "SubscribeViewModel"
    }
}