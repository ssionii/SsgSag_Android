package com.icoo.ssgsag_android.ui.main.review.reviewDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseViewModel
import com.icoo.ssgsag_android.data.model.review.ReviewGrade
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
    var reviewType = ""

    // 전체 정보
    private val _reviewDetail = MutableLiveData<ClubInfo>()
    val reviewDetail: LiveData<ClubInfo> get() = _reviewDetail

    private val _reviewGradeList = MutableLiveData<ArrayList<ReviewGrade>>()
    val reviewGradeList : LiveData<ArrayList<ReviewGrade>> get() = _reviewGradeList

    private val _clubCategory = MutableLiveData<List<String>>()
    val clubCategory: LiveData<List<String>> get() = _clubCategory

    private val _photoList = MutableLiveData<MutableList<String>>()
    val photoList: LiveData<MutableList<String>> get() = _photoList

    // reviewDetail에서 보는 슥삭 후기
    private val _ssgsagMainReviews = MutableLiveData<ArrayList<ClubPost>>()
    val ssgSagMainReviews: LiveData<ArrayList<ClubPost>> get() = _ssgsagMainReviews

    private val _blogMainReviews = MutableLiveData<ArrayList<BlogReview>>()
    val blogMainReviews: LiveData<ArrayList<BlogReview>> get() = _blogMainReviews

    init{

    }

    fun getClubDetail(){

        addDisposable(repository.getClubDetail(mClubIdx)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .subscribe({
                _reviewDetail.setValue(it)
                _ssgsagMainReviews.setValue(it.clubPostList)
                _blogMainReviews.setValue(it.clubBlogList)

                _photoList.postValue(it.clubPhotoUrlList?.split(",")?.toMutableList())
                _clubCategory.postValue(it.categoryList.split(","))

                setReviewGradeList()

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

    fun setReviewGradeList(){


        val reviewGradeTempList = arrayListOf(ReviewGrade(reviewType, "",0.0 ), ReviewGrade(reviewType, "",0.0 )
            , ReviewGrade(reviewType, "",0.0), ReviewGrade(reviewType, "",0.0))

        val clubCategory = arrayListOf("전문성", "재미", "강도", "친목")
        val actCategory = arrayListOf("혜택", "재미", "강도", "친목")
        val internCategory = arrayListOf("성장", "급여", "강도", "사내문화")

        when(reviewType){
            "club" -> {
               for(i in 0 until reviewGradeTempList.size){
                   reviewGradeTempList[i].categoryName = clubCategory[i]
               }
            }

            "act" -> {
                for(i in 0 until reviewGradeTempList.size){
                    reviewGradeTempList[i].categoryName = actCategory[i]
                }
            }

            "intern" ->{
                for(i in 0 until reviewGradeTempList.size){
                    reviewGradeTempList[i].categoryName = internCategory[i]
                }
            }
        }

        reviewDetail.value!!.apply{
            reviewGradeTempList[0].score = this.aveScore1
            reviewGradeTempList[1].score = this.aveScore2
            reviewGradeTempList[2].score = this.aveScore3
            reviewGradeTempList[3].score = this.aveScore4
        }

        _reviewGradeList.postValue(reviewGradeTempList)


    }

}