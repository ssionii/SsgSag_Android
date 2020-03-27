package com.icoo.ssgsag_android.ui.main.subscribe.subscribeDialog

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.subscribe.SubscribeFilter
import com.icoo.ssgsag_android.data.model.subscribe.SubscribeRepository
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import org.json.JSONArray
import org.json.JSONObject

class SubscribeDialogViewModel(
    private val repository: SubscribeRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private var defaultAreaData = arrayListOf(
        SubscribeFilter(101, "경영/사무" ,false),
        SubscribeFilter(102, "영업/고객상담" ,false),
        SubscribeFilter(103, "생산/제조" ,false),
        SubscribeFilter(104, "IT/인터넷" ,false),
        SubscribeFilter(105, "전문직" ,false),
        SubscribeFilter(106, "교육" ,false),
        SubscribeFilter(107, "미디어" ,false),
        SubscribeFilter(108, "특수계층/공공" ,false),
        SubscribeFilter(109, "건설" ,false),
        SubscribeFilter(110, "유통/무역" ,false),
        SubscribeFilter(111, "서비스" ,false),
        SubscribeFilter(112, "디자인" ,false),
        SubscribeFilter(113, "의료" ,false),
        SubscribeFilter(114, "기타" ,false)
    )

    private var defaultIndustryData = arrayListOf(
        SubscribeFilter(10000, "대기업" ,false),
        SubscribeFilter(20000, "중견기업" ,false),
        SubscribeFilter(30000, "중소기업" ,false),
        SubscribeFilter(40000, "외국계기업" ,false),
        SubscribeFilter(50000, "공사/공기업" ,false),
        SubscribeFilter(60000, "스타트업" ,false)
    )

    private var defaultContestData = arrayListOf(
        SubscribeFilter(1, "기획/아이디어", false),
        SubscribeFilter(2, "마케팅/브랜딩", false),
        SubscribeFilter(3, "영상/콘텐츠", false),
        SubscribeFilter(4, "디자인", false),
        SubscribeFilter(5, "IT/공학", false),
        SubscribeFilter(6, "문학/시나리오", false),
        SubscribeFilter(7, "스타트업/창업", false),
        SubscribeFilter(8, "기타", false)

    )

    var selectedAllArea = false
    var selectedAllIndustry = false
    var selectedAllContest = false

    private val _isProgress = MutableLiveData<Int>()
    val isProgress: LiveData<Int> get() = _isProgress
    private val _interest = MutableLiveData<ArrayList<Int>>()
    val interest: LiveData<ArrayList<Int>> get() = _interest

    private val _areaFilter = MutableLiveData<ArrayList<SubscribeFilter>>()
    val areaFilter: LiveData<ArrayList<SubscribeFilter>> get() = _areaFilter
    private val _industryFilter = MutableLiveData<ArrayList<SubscribeFilter>>()
    val industryFilter: LiveData<ArrayList<SubscribeFilter>> get() = _industryFilter
    private val _contestFilter = MutableLiveData<ArrayList<SubscribeFilter>>()
    val contestFilter : LiveData<ArrayList<SubscribeFilter>> get() = _contestFilter

    init {
        getInterest()
        _areaFilter.setValue(defaultAreaData)
        _industryFilter.setValue(defaultIndustryData)
        _contestFilter.setValue(defaultContestData)
    }

    // 처음 통신
    fun getInterest() {
        addDisposable(repository.getInterest()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .doOnSubscribe { showProgress() }
            .doOnTerminate { hideProgress() }
            .subscribe({
                it.run {
                    _interest.setValue(this.interests)
                }
            }, {
            })
        )

        divideInterested()
    }

    private fun divideInterested(){
        if(_interest.value != null) {
            for (i in 0.._interest.value!!.size - 1) {
                if(_interest.value!![i] < 10000 && _interest.value!![i]>=101){
                    _areaFilter.value!![interest.value!![i]-101].isInterested = true
                }
                else if(_interest.value!![i] >= 10000 )
                    _industryFilter.value!![interest.value!![i]/10000 -1].isInterested = true
            }
        }
    }

    // 확인 누르면
    fun combineInterest() : Boolean {

        var temp = ArrayList<Int>()
        var areaFlag = false
        var industryFlag = false

        for(i in 0.. _areaFilter.value!!.size -1){
            if(_areaFilter.value!![i].isInterested){
                temp.add(_areaFilter.value!![i].idx)
                areaFlag = true
            }
        }

        for(i in 0.. _industryFilter.value!!.size -1){
            if(_industryFilter.value!![i].isInterested) {
                temp.add(_industryFilter.value!![i].idx)
                industryFlag = true
            }
        }

        _interest.setValue(temp)
        for(i in 0.._interest.value!!.size-1){
            Log.e("interest :",_interest.value!![i].toString())
        }

        if(!(areaFlag && industryFlag))
            return false
        else {
            reInterest()
            return true
        }
    }

    private fun reInterest() {

        val jsonObject = JSONObject()
        val jsonArray = JSONArray(interest.value!!)
        jsonObject.put("userInterest", jsonArray)

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject

            addDisposable(repository.reInterest(body)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .doOnSubscribe { showProgress() }
                .doOnTerminate { hideProgress() }
                .subscribe({
                    Log.e("status:",it.toString())
                }, {

                })
            )
    }


    fun clickInterest(idx: Int) {
        if(idx > 100 && idx < 200){
            if(defaultAreaData[idx-101].isInterested) {
                defaultAreaData[idx - 101].isInterested = false
            }
            else
                defaultAreaData[idx-101].isInterested = true

            _areaFilter.postValue(defaultAreaData)
        }else if(idx >= 10000){
            if(defaultIndustryData[(idx/10000) -1].isInterested) {
                defaultIndustryData[(idx/10000) -1].isInterested = false
            }
            else
                defaultIndustryData[(idx/10000) -1].isInterested = true

            _industryFilter.postValue(defaultIndustryData)
        }else if(idx > 0 && idx <10){
            if(defaultContestData[idx-1].isInterested) {
                defaultContestData[idx-1].isInterested = false
            }
            else
                defaultContestData[idx-1].isInterested = true

            _contestFilter.postValue(defaultContestData)
        }

    }

    fun selectAll(cate: String){

        if(cate == "area") {
            if (!selectedAllArea) {
                for (i in 0..defaultAreaData.size - 1)
                    defaultAreaData[i].isInterested = true
                _areaFilter.postValue(defaultAreaData)
                selectedAllArea = true
            } else {
                for (i in 0..defaultAreaData.size - 1)
                    defaultAreaData[i].isInterested = false
                _areaFilter.postValue(defaultAreaData)
                selectedAllArea = false
            }
        }else if(cate == "industry"){
            if (!selectedAllIndustry) {
                for (i in 0..defaultIndustryData.size - 1)
                    defaultIndustryData[i].isInterested = true
                _industryFilter.postValue(defaultIndustryData)
                selectedAllIndustry = true
            } else {
                for (i in 0..defaultIndustryData.size - 1)
                    defaultIndustryData[i].isInterested = false
                _industryFilter.postValue(defaultIndustryData)
                selectedAllIndustry = false
            }
        }else if(cate == "contest"){
            if (!selectedAllContest) {
                for (i in 0..defaultContestData.size - 1)
                    defaultContestData[i].isInterested = true
                _contestFilter.postValue(defaultContestData)
                selectedAllContest = true
            } else {
                for (i in 0..defaultContestData.size - 1)
                    defaultContestData[i].isInterested = false
                _contestFilter.postValue(defaultContestData)
                selectedAllContest = false
            }
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