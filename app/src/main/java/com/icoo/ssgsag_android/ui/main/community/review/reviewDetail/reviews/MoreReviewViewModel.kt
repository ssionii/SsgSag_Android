package com.icoo.ssgsag_android.ui.main.review.club.reviews

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.review.ReviewRepository
import com.icoo.ssgsag_android.data.model.review.club.BlogReview
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import com.icoo.ssgsag_android.data.model.review.club.ClubReviewRepository
import com.icoo.ssgsag_android.ui.main.community.feed.FeedWebActivity
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import kotlin.reflect.KClass

class MoreReviewViewModel (
    val repository: ClubReviewRepository,
    val reviewRepository: ReviewRepository,
    val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    var clubIdx = -1
    // Review 전체보기에서 보는 슥삭 후기
    private val _ssgsagReviews = MutableLiveData<ArrayList<ClubPost>>()
    val ssgSagReviews: LiveData<ArrayList<ClubPost>> get() = _ssgsagReviews

    private val _blogReviews = MutableLiveData<ArrayList<BlogReview>>()
    val blogReviews: LiveData<ArrayList<BlogReview>> get() = _blogReviews

    private var totalSsgSagReviews = arrayListOf<ClubPost>()
    private var totalBlogReviews = arrayListOf<BlogReview>()

    private val _refeshedSsgsagReview = MutableLiveData<ClubPost>()
    val refreshedSsgsagReview : LiveData<ClubPost> get() = _refeshedSsgsagReview

    private val _deletedPosition = MutableLiveData<Int>()
    val deletedPosition : LiveData<Int> get() = _deletedPosition

    var refreshedPosition = 0

    private val _activityToStart = MutableLiveData<Pair<KClass<*>, Bundle?>>()
    val activityToStart: LiveData<Pair<KClass<*>, Bundle?>> get() = _activityToStart

    init {
        _deletedPosition.value = -1
    }


    fun getSsgSagReviews(curPage: Int){
        addDisposable(repository.getSsgSagReviews(clubIdx, curPage)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                _ssgsagReviews.postValue(it)
                totalSsgSagReviews.addAll(it)
            },{
                it.printStackTrace()
            })
        )
    }

    fun getBlogReviews(curPage: Int){
        addDisposable(repository.getBlogReviews(clubIdx, curPage)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                _blogReviews.postValue(it)
                totalBlogReviews.addAll(it)
            },{
                it.printStackTrace()
            })
        )
    }

    fun clickLike(isLike: Int, idx: Int, position: Int){
        refreshedPosition = position
        val prevNum = totalSsgSagReviews[position].likeNum

        if(isLike == 0){
            addDisposable(repository.likeReview(idx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    if(it == 200){
                        totalSsgSagReviews[position] = totalSsgSagReviews[position].copy(isLike = 1, likeNum =  prevNum + 1)
                        _refeshedSsgsagReview.postValue(totalSsgSagReviews[position])
                        }

                },{
                    it.printStackTrace()
                })
            )
        }else {
            addDisposable(repository.unlikeReview(idx)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe({
                    if(it == 200){
                        totalSsgSagReviews[position] = totalSsgSagReviews[position].copy(isLike = 0, likeNum =  prevNum - 1)
                        _refeshedSsgsagReview.postValue( totalSsgSagReviews[position])

                    }
                }, {
                    it.printStackTrace()
                })
            )
        }
    }

    fun navigate(url: String) {
        val bundle = Bundle().apply {
            putString("url", url)
        }

        _activityToStart.postValue(Pair(FeedWebActivity::class, bundle))
    }

    fun deleteReview(idx: Int, position: Int){
        addDisposable(reviewRepository.deleteReview(idx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                if(it == 200)
                    _deletedPosition.postValue(position)

                Log.e("delete review status", it.toString())
            }, {
                it.printStackTrace()
            })
        )
    }


}