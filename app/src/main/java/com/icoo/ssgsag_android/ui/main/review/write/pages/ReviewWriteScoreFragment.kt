package com.icoo.ssgsag_android.ui.main.review.club.write.pages

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentReviewWriteScoreBinding
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteActivity.ClubReviewWriteData
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.backgroundColor
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewWriteScoreFragment :  BaseFragment<FragmentReviewWriteScoreBinding, ReviewWriteViewModel>() {
    override val layoutResID: Int
        get() = R.layout.fragment_review_write_score
    override val viewModel: ReviewWriteViewModel by viewModel()

    val position = 2

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.vm = viewModel

        viewModel.setScoreQuestion()
        setButton()
        setRatingBar()

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser)
            viewDataBinding.fragReviewWriteScoreTvTitle.text = ClubReviewWriteData.clubName
    }


    private fun setRatingBar(){

        viewDataBinding.fragReviewWriteScoreRb0.setOnRatingChangeListener { ratingBar, rating ->
            viewDataBinding.fragReviewWriteScoreLlBottomContainer.apply{
                if(this.visibility == GONE)
                    visibility = VISIBLE
            }
            ClubReviewWriteData.score0 = rating.toInt()
            onDataCheck()
        }

        viewDataBinding.fragReviewWriteScoreRb1.setOnRatingChangeListener { ratingBar, rating ->
            ClubReviewWriteData.score1 = rating.toInt()
            onDataCheck()
        }

        viewDataBinding.fragReviewWriteScoreRb2.setOnRatingChangeListener { ratingBar, rating ->
            ClubReviewWriteData.score2 = rating.toInt()
            onDataCheck()
        }
        viewDataBinding.fragReviewWriteScoreRb3.setOnRatingChangeListener { ratingBar, rating ->
            ClubReviewWriteData.score3 = rating.toInt()
            onDataCheck()
        }
        viewDataBinding.fragReviewWriteScoreRb4.setOnRatingChangeListener { ratingBar, rating ->
            ClubReviewWriteData.score4 = rating.toInt()
            onDataCheck()
        }
    }

    private fun setButton(){
        viewDataBinding.fragReviewWriteScoreNext.setSafeOnClickListener {
            (activity as ReviewWriteActivity).toNextPage(position)
        }
        viewDataBinding.fragWriteReviewScoreIvBack.setSafeOnClickListener {
            (activity as ReviewWriteActivity).toPrevPage(position)
        }

    }

    private fun onDataCheck(){
        if(ClubReviewWriteData.score0 == 0 || ClubReviewWriteData.score1 == 0
            || ClubReviewWriteData.score2 == 0 || ClubReviewWriteData.score3 == 0 ||ClubReviewWriteData.score4 == 0){
            viewDataBinding.fragReviewWriteScoreNext.apply{
                isClickable = false
                backgroundColor = context.resources.getColor(R.color.grey_2)
            }
        }else{
            viewDataBinding.fragReviewWriteScoreNext.apply{
                isClickable = true
                backgroundColor = context.resources.getColor(R.color.ssgsag)
            }
        }
    }
}