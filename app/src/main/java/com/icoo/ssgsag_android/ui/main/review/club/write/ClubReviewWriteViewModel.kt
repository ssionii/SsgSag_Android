package com.icoo.ssgsag_android.ui.main.review.club.write

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.review.club.ClubInfo
import com.icoo.ssgsag_android.data.model.review.club.ClubReviewRepository
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import org.json.JSONObject

class ClubReviewWriteViewModel(
    private val repository: ClubReviewRepository
, private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    private var _clubType = MutableLiveData<Int>()
    val clubType : LiveData<Int> get() = _clubType

    private var _isNotRgstr = MutableLiveData<Boolean>()
    val isNotRgstr : LiveData<Boolean> get() = _isNotRgstr

    private var _clubSearchResult = MutableLiveData<ArrayList<ClubInfo>>()
    val clubSearchResult : LiveData<ArrayList<ClubInfo>> get() = _clubSearchResult

    private var _postStatus = MutableLiveData<Int>()
    val postStatus : LiveData<Int> get() = _postStatus


    var clubIdx = -1
    var isRgstrClub = false
    var showEvent = false

    init {
        _isNotRgstr.postValue(false)
        iniPostStatus()
    }

    fun setClubType(type: Int){
        _clubType.postValue(type)
    }

    fun setIsNotRgstr(boolean: Boolean){
        _isNotRgstr.postValue(boolean)
    }


    fun postClubReview(jsonObject : JSONObject){

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

        addDisposable(repository.writeClubReview(body)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                if(it.status == 200){
                    Log.e("write review", "success")
                    clubIdx = it.data.clubIdx
                    showEvent = it.data.event
                }

                _postStatus.value = it.status
                Log.e("write review status", it.toString())
            }, {
                it.printStackTrace()
            })
        )
    }

    fun searchClub(univOrLocation: String, keyword: String){
        addDisposable(repository.searchClub(clubType.value!!, univOrLocation, keyword, 0)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                _clubSearchResult.postValue(it)
                Log.e("search club status", it.toString())
            }, {
                it.printStackTrace()
            })
        )
    }

    fun iniPostStatus(){
        _postStatus.setValue(0)
    }
}
