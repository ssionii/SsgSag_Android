package com.icoo.ssgsag_android.ui.main.review.club.reviews.blogReview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.review.club.ClubReviewRepository
import com.icoo.ssgsag_android.data.model.review.club.ClubReviewRepositoryImpl
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider

class BlogReviewViewModel (
    private val repository: ClubReviewRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _postStatus = MutableLiveData<Int>()
    val postStatus : LiveData<Int> get() = _postStatus

    init{
        _postStatus.setValue(0)
    }

    fun postClubBlogReview(clubIdx: Int, blogUrl: String){

        addDisposable(repository.writeClubBlogReview(clubIdx, blogUrl)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                it.run {
                    _postStatus.setValue(this)
                }
            }, {

            })
        )
    }
}