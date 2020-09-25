package com.icoo.ssgsag_android.ui.main.ssgSag

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.poster.PosterRepository
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.data.model.user.UserRepository
import com.icoo.ssgsag_android.ui.main.myPage.MyPageFragment
import com.icoo.ssgsag_android.ui.main.subscribe.SubscribeActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import kotlin.reflect.KClass

class SsgSagViewModel(
    private val posterRepository: PosterRepository,
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress
    private val _allPosters = MutableLiveData<ArrayList<PosterDetail>>()
    val allPosters: LiveData<ArrayList<PosterDetail>> get() = _allPosters
    private val _posterCount = MutableLiveData<Int>()
    val posterCount: LiveData<Int> get() = _posterCount
    private val _userCnt = MutableLiveData<Int>()
    val userCnt: LiveData<Int> get() = _userCnt


    var endImageCount = 0

    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart

    init {
        getAllPosters()
    }

    fun getAllPosters() {

        addDisposable(posterRepository.getAllPosters()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _allPosters.postValue(this.posters)
                    _posterCount.postValue(this.posters.size)
                    _userCnt.setValue(this.userCnt)
                }
            }, {

            })
        )
    }

    fun getUserCnt(){
        addDisposable(posterRepository.getUserCnt()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _userCnt.setValue(this)
                }
            }, {

            })
        )
    }


    fun ssgSag(posterIdx: Int, like: Int) {
        addDisposable(
            posterRepository.ssgSag(posterIdx, like)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({

                }, {

                })
        )
    }


    fun posterCountDown() {
        _posterCount.let {
            it.postValue(it.value!!.toInt() - 1)
        }

    }

    fun navigatePhoto(activity: KClass<*>, photoUrl: String) {
        val bundle = Bundle().apply { putString("photoUrl", photoUrl) }
        _activityToStart.postValue(Pair(activity, bundle))
    }

    fun navigatePage(idx: Int){
        when(idx) {
            0-> _activityToStart.postValue(Pair(MyPageFragment::class, null))
            1->  _activityToStart.postValue(Pair(SubscribeActivity::class, null))
        }
    }

    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.INVISIBLE
    }

    companion object {
        private val TAG = "SsgSagViewModel"
    }
}