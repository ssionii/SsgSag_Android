package com.icoo.ssgsag_android.ui.main.myPage.myReview

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.review.ReviewRepository
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import com.icoo.ssgsag_android.data.model.review.club.ClubReviewRepository
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class MyReviewViewModel(
    private val reviewRepository : ReviewRepository,
    private val clubReviewRepository: ClubReviewRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    private val _myReviews = MutableLiveData<ArrayList<ClubPost>>()
    val myReviews : LiveData<ArrayList<ClubPost>> get() = _myReviews

    private val _myReviewDetail = MutableLiveData<ClubPost>()
    val myReviewDetail : LiveData<ClubPost> get() = _myReviewDetail

    private val _updateStatus = MutableLiveData<Int>()
    val updateStatus : LiveData<Int> get() = _updateStatus

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
}