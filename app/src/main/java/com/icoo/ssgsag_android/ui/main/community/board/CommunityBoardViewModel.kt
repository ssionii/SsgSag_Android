package com.icoo.ssgsag_android.ui.main.community.board

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.data.model.ads.AdItem
import com.icoo.ssgsag_android.data.model.community.CommunityRepository
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.data.model.community.board.PostInfo
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.SsgSagApplication.Companion.globalApplication
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class CommunityBoardViewModel(
    private val repository: CommunityRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    private var _postList = MutableLiveData<ArrayList<PostInfo>>()
    val postList : LiveData<ArrayList<PostInfo>> = _postList

    private var _adList = MutableLiveData<ArrayList<AdItem>>()
    val adList : LiveData<ArrayList<AdItem>> = _adList

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress

    fun getCounselList(category : String, curPage : Int, pageSize : Int){
        addDisposable(repository.getBoardPost(category, curPage,pageSize)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { if(curPage == 0) showProgress()}
            .doOnTerminate { hideProgress() }
            .doOnError {
                Log.e("get counsel list error", it.message)
            }
            .subscribe({
                if(curPage == 0)
                    _adList.value = it.adList
                _postList.value = it.communityList
            }) {
                Toast.makeText(globalApplication, "네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                Log.e("get counsel list error:", it.message)
            })
    }

    fun getTalkList(curPage: Int, pageSize: Int){
        addDisposable(repository.getBoardPost("FREE", curPage,pageSize)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { if(curPage == 0) showProgress()}
            .doOnTerminate { hideProgress() }
            .doOnError {
                Log.e("get counsel list error", it.message)
            }
            .subscribe({
                if(curPage == 0)
                    _adList.value = it.adList
                _postList.value = it.communityList
            }) {
                Toast.makeText(context, "네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                Log.e("get counsel list error:", it.message)
            })
    }

    fun refreshCounselList(category : String, curPage : Int, pageSize : Int){
        addDisposable(repository.getBoardPost(category, curPage,pageSize)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { if(curPage == 0) showProgress()}
            .doOnTerminate { hideProgress() }
            .doOnError {
                Log.e("get counsel list error", it.message)
            }
            .subscribe({
                _postList.value = it.communityList
            }) {
                Toast.makeText(globalApplication, "네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                Log.e("get counsel list error:", it.message)
            })
    }

    fun refreshTalkList(curPage: Int, pageSize: Int){
        addDisposable(repository.getBoardPost("FREE", curPage,pageSize)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { if(curPage == 0) showProgress()}
            .doOnTerminate { hideProgress() }
            .doOnError {
                Log.e("get counsel list error", it.message)
            }
            .subscribe({
                _postList.value = it.communityList
            }) {
                Toast.makeText(context, "네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                Log.e("get counsel list error:", it.message)
            })
    }


    private fun showProgress() {
        _isProgress.value = View.VISIBLE
    }

    private fun hideProgress() {
        _isProgress.value = View.INVISIBLE
    }

}