package com.icoo.ssgsag_android.ui.main.myPage.myReview

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.review.ReviewRepository
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import com.icoo.ssgsag_android.data.model.review.club.ClubReviewRepository
import com.icoo.ssgsag_android.ui.main.community.review.ReviewType
import com.icoo.ssgsag_android.SsgSagApplication.Companion.getGlobalApplicationContext
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class MyReviewViewModel(
    private val reviewRepository : ReviewRepository,
    private val clubReviewRepository: ClubReviewRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    val clubQuestions = arrayListOf("Q1. 재미 | 동아리 활동이 얼마나 재미있었나요?", "Q2. 전문성 | 모임 주제에 대해 전문성이 있나요?"
        , "Q3. 강도 | 동아리 활동의 강도는 어떤가요?" ,"Q4. 친목 | 사람을 많이 얻을 수 있나요?")
    val actQuestions = arrayListOf("Q1. 혜택 | 활동 혜택이 얼마나 좋았나요?", "Q2. 재미 | 대외활동이 얼마나 재미있었나요?"
        , "Q3. 강도 | 대외활동의 활동강도는 어떤가요?", "Q4. 친목 | 사람을 많이 얻을 수 있나요?")
    val internQuestions = arrayListOf("Q1. 성장 | 인턴 기간동안 얼마나 성장했나요?", "Q2. 급여 | 급여는 만족스러웠나요?"
        , "Q3. 강도 | 근무 강도는 어땠나요?", "Q4. 사내문화 | 사내문화는 및 분위기는 어땠나요?")

    var reviewType = 0

    private val _myReviews = MutableLiveData<ArrayList<ClubPost>>()
    val myReviews : LiveData<ArrayList<ClubPost>> get() = _myReviews

    private val _myReviewDetail = MutableLiveData<ClubPost>()
    val myReviewDetail : LiveData<ClubPost> get() = _myReviewDetail

    private val _updateStatus = MutableLiveData<Int>()
    val updateStatus : LiveData<Int> get() = _updateStatus

    private var _questions = MutableLiveData<ArrayList<String>>()
    val questions : LiveData<ArrayList<String>> get() = _questions
    private var _rateLabels = MutableLiveData<ArrayList<Array<String>>>()
    val rateLabels : LiveData<ArrayList<Array<String>>> get() = _rateLabels

    init {
        _updateStatus.value = 0
    }

    fun getMyReviews(curPage: Int){

        addDisposable(reviewRepository.getMyReviews(curPage)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                _myReviews.postValue(it)
            }, {
                it.printStackTrace()
            }))
    }

    fun clickLike(){
        myReviewDetail.value?.let{
            val isLike = it.isLike
            var prevNum = it.likeNum
            val idx = it.clubPostIdx

            if(isLike == 0){
                addDisposable(clubReviewRepository.likeReview(idx)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.mainThread())
                    .subscribe({
                        _myReviewDetail.postValue(myReviewDetail.value!!.copy(isLike = 1, likeNum = prevNum + 1))
                    },{
                        it.printStackTrace()
                    })
                )
            }else {
                addDisposable(clubReviewRepository.unlikeReview(idx)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.mainThread())
                    .subscribe({
                        _myReviewDetail.postValue(myReviewDetail.value!!.copy(isLike = 0, likeNum = prevNum - 1))
                    }, {
                        it.printStackTrace()
                    })
                )
            }
        }

    }

    fun setMyReviewDetail(review: ClubPost){
        _myReviewDetail.value = review
        reviewType = review.clubType!!
    }

    fun updateReview(body: JsonObject){
        addDisposable(reviewRepository.updateReview(body, myReviewDetail.value!!.clubPostIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
               _updateStatus.setValue(it)

                Log.e("updateReview status", it.toString())
            }) {
                Toast.makeText(context, "오류가 발생하였습니다.",Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            })
    }

    fun deleteReview(idx: Int){
        addDisposable(reviewRepository.deleteReview(idx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                if(it == 200)
                    Toast.makeText(context, "삭제 완료.",Toast.LENGTH_SHORT).show()
            }, {
                it.printStackTrace()
            })
        )
    }

    fun setScoreQuestion(){
        when(reviewType){
            ReviewType.UNION_CLUB, ReviewType.UNIV_CLUB -> {
                _questions.postValue(clubQuestions)
                _rateLabels.postValue(arrayListOf(context.resources.getStringArray(R.array.fun_label), context.resources.getStringArray(
                    R.array.degree_label)
                    ,context.resources.getStringArray(R.array.intense_label), context.resources.getStringArray(
                        R.array.basic_label)))
            }
            ReviewType.ACT -> {
                _questions.postValue(actQuestions)
                _rateLabels.postValue(arrayListOf(context.resources.getStringArray(R.array.basic_label), context.resources.getStringArray(
                    R.array.fun_label)
                    ,context.resources.getStringArray(R.array.intense_label), context.resources.getStringArray(
                        R.array.basic_label)))
            }
            ReviewType.INTERN -> {
                _questions.postValue(internQuestions)
                _rateLabels.postValue(arrayListOf(context.resources.getStringArray(R.array.growth_label), context.resources.getStringArray(
                    R.array.basic_label)
                    ,context.resources.getStringArray(R.array.intense_label), context.resources.getStringArray(
                        R.array.basic_label)))
            }
        }
    }
}