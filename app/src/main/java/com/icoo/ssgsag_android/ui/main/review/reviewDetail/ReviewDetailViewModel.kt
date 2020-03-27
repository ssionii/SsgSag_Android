package com.icoo.ssgsag_android.ui.main.review.reviewDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.review.ReviewRepository
import com.icoo.ssgsag_android.data.model.review.club.BlogReview
import com.icoo.ssgsag_android.data.model.review.club.ClubInfo
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import com.icoo.ssgsag_android.data.model.review.club.ClubReviewRepository
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class ReviewDetailViewModel(
    val repository: ClubReviewRepository,
    val reviewRepository: ReviewRepository,
    val schedulerProvider: SchedulerProvider
) : BaseViewModel(){

    var mClubIdx = 0
    var isAlreadyWrite = false

    // 전체 정보
    private val _reviewDetail = MutableLiveData<ClubInfo>()
    val reviewDetail: LiveData<ClubInfo> get() = _reviewDetail

    private val _clubCategory = MutableLiveData<List<String>>()
    val clubCategory: LiveData<List<String>> get() = _clubCategory

    private val _photoList = MutableLiveData<MutableList<String>>()
    val photoList: LiveData<MutableList<String>> get() = _photoList

    // reviewDetail에서 보는 슥삭 후기
    private val _ssgsagMainReviews = MutableLiveData<ArrayList<ClubPost>>()
    val ssgSagMainReviews: LiveData<ArrayList<ClubPost>> get() = _ssgsagMainReviews

    private val _blogMainReviews = MutableLiveData<ArrayList<BlogReview>>()
    val blogMainReviews: LiveData<ArrayList<BlogReview>> get() = _blogMainReviews

    fun getClubDetail(){

        addDisposable(repository.getClubDetail(mClubIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                _reviewDetail.postValue(it)
                _ssgsagMainReviews.setValue(it.clubPostList)
                _blogMainReviews.setValue(it.clubBlogList)

                _photoList.postValue(it.clubPhotoUrlList?.split(",")?.toMutableList())
                _clubCategory.postValue(it.categoryList.split(","))

            },{
                it.printStackTrace()
            })
        )
    }

    fun getSsgSagMainReviews(){
        addDisposable(repository.getClubDetail(mClubIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                _ssgsagMainReviews.postValue(it.clubPostList)
            },{
                it.printStackTrace()
            })
        )
    }

    fun getBlogReviews(){
        addDisposable(repository.getClubDetail(mClubIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                _blogMainReviews.postValue(it.clubBlogList)
            },{
                it.printStackTrace()
            })
        )
    }


    fun clickLike(isLike: Int, idx: Int){
        if(isLike == 0){
            addDisposable(repository.likeReview(idx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    getSsgSagMainReviews()
                },{
                    it.printStackTrace()
                })
            )
        }else {
            addDisposable(repository.unlikeReview(idx)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.mainThread())
                    .subscribe({
                        getSsgSagMainReviews()
                    }, {
                        it.printStackTrace()
                    })
            )
        }

    }

    fun getAlreadyWrite(){
        addDisposable(repository.getAlreadyWrite(mClubIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                isAlreadyWrite = it
                Log.e("isAlreadyWrite", it.toString())
            },{
                it.printStackTrace()
            })
        )
    }

    fun deleteReview(idx: Int){
        addDisposable(reviewRepository.deleteReview(idx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                getSsgSagMainReviews()
            }, {
                it.printStackTrace()
            })
        )
    }

}