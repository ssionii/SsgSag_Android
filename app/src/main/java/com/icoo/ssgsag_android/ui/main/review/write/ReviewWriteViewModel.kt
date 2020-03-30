package com.icoo.ssgsag_android.ui.main.review.club.write

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.review.club.ClubInfo
import com.icoo.ssgsag_android.data.model.review.club.ClubReviewRepository
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import org.json.JSONObject

class ReviewWriteViewModel(
    private val repository: ClubReviewRepository
, private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    val clubQuestions = arrayListOf("Q1. 재미 | 동아리 활동이 얼마나 재미있었나요?", "Q2. 전문성 | 모임 주제에 대해 전문성이 있나요?"
        , "Q3. 강도 | 동아리 활동의 강도는 어떤가요?" ,"Q4. 친목 | 사람을 많이 얻을 수 있나요?")
    val actQuestions = arrayListOf("Q1. 혜택 | 활동 혜택이 얼마나 좋았나요?", "Q2. 재미 | 대외활동이 얼마나 재미있었나요?"
        , "Q3. 강도 | 대외활동의 활동강도는 어떤가요?", "Q4. 친목 | 사람을 많이 얻을 수 있나요?")
    val internQuestions = arrayListOf("Q1. 성장 | 인턴 기간동안 얼마나 성장했나요?", "Q2. 급여 | 급여는 만족스러웠나요?"
        , "Q3. 강도 | 근무 강도는 어땠나요?", "Q4. 사내문화 | 사내문화는 및 분위기는 어땠나요?")


    private var _clubType = MutableLiveData<Int>()
    val clubType : LiveData<Int> get() = _clubType

    private var _isNotRgstr = MutableLiveData<Boolean>()
    val isNotRgstr : LiveData<Boolean> get() = _isNotRgstr

    private var _clubSearchResult = MutableLiveData<ArrayList<ClubInfo>>()
    val clubSearchResult : LiveData<ArrayList<ClubInfo>> get() = _clubSearchResult

    private var _postStatus = MutableLiveData<Int>()
    val postStatus : LiveData<Int> get() = _postStatus

    private var _questions = MutableLiveData<ArrayList<String>>()
    val questions : LiveData<ArrayList<String>> get() = _questions
    private var _rateLabels = MutableLiveData<ArrayList<Array<String>>>()
    val rateLabels : LiveData<ArrayList<Array<String>>> get() = _rateLabels

    var reviewType = ""
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

    fun setScoreQuestion(){
        when(reviewType){
            "club" -> {
                _questions.postValue(clubQuestions)
                _rateLabels.postValue(arrayListOf(context.resources.getStringArray(R.array.fun_label), context.resources.getStringArray(R.array.degree_label)
                ,context.resources.getStringArray(R.array.intense_label), context.resources.getStringArray(R.array.basic_label)))
            }
            "act" -> {
                _questions.postValue(actQuestions)
                _rateLabels.postValue(arrayListOf(context.resources.getStringArray(R.array.basic_label), context.resources.getStringArray(R.array.fun_label)
                    ,context.resources.getStringArray(R.array.intense_label), context.resources.getStringArray(R.array.basic_label)))
            }
            "intern" -> {
                _questions.postValue(internQuestions)
                _rateLabels.postValue(arrayListOf(context.resources.getStringArray(R.array.growth_label), context.resources.getStringArray(R.array.basic_label)
                    ,context.resources.getStringArray(R.array.intense_label), context.resources.getStringArray(R.array.basic_label)))
            }
        }
    }

    fun setRateLabels(){

    }

}
