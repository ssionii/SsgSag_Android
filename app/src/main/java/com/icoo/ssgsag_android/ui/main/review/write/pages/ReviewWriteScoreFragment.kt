package com.icoo.ssgsag_android.ui.main.review.club.write.pages

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.data.model.review.ReviewWriteRelam
import com.icoo.ssgsag_android.databinding.FragmentReviewWriteScoreBinding
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import io.realm.Realm
import org.jetbrains.anko.backgroundColor
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewWriteScoreFragment :  BaseFragment<FragmentReviewWriteScoreBinding, ReviewWriteViewModel>() {
    override val layoutResID: Int
        get() = R.layout.fragment_review_write_score
    override val viewModel: ReviewWriteViewModel by viewModel()

    val realm = Realm.getDefaultInstance()
    lateinit var reviewWriteRealm : ReviewWriteRelam

    var position = -1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.vm = viewModel

        reviewWriteRealm = realm.where(ReviewWriteRelam::class.java).equalTo("id", 1 as Int).findFirst()!!

        position = arguments!!.getInt("position", -1)

        viewModel.setScoreQuestion()
        setButton()
        setRatingBar()

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser)
            viewDataBinding.fragReviewWriteScoreTvTitle.text = reviewWriteRealm.clubName
    }


    private fun setRatingBar(){

        viewDataBinding.fragReviewWriteScoreRb0.setOnRatingChangeListener { ratingBar, rating ->
            viewDataBinding.fragReviewWriteScoreLlBottomContainer.apply{
                if(this.visibility == GONE)
                    visibility = VISIBLE
            }
            (activity as ReviewWriteActivity).setReviewWriteIntRealm(0, rating.toInt())
            onDataCheck()
        }

        viewDataBinding.fragReviewWriteScoreRb1.setOnRatingChangeListener { ratingBar, rating ->
            (activity as ReviewWriteActivity).setReviewWriteIntRealm(1, rating.toInt())
            onDataCheck()
        }

        viewDataBinding.fragReviewWriteScoreRb2.setOnRatingChangeListener { ratingBar, rating ->
            (activity as ReviewWriteActivity).setReviewWriteIntRealm(2, rating.toInt())
            onDataCheck()
        }
        viewDataBinding.fragReviewWriteScoreRb3.setOnRatingChangeListener { ratingBar, rating ->
            (activity as ReviewWriteActivity).setReviewWriteIntRealm(3, rating.toInt())
            onDataCheck()
        }
        viewDataBinding.fragReviewWriteScoreRb4.setOnRatingChangeListener { ratingBar, rating ->
            (activity as ReviewWriteActivity).setReviewWriteIntRealm(4, rating.toInt())
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
        if(reviewWriteRealm.score0 == 0 || reviewWriteRealm.score1 == 0
            || reviewWriteRealm.score2 == 0 || reviewWriteRealm.score3 == 0 ||reviewWriteRealm.score4 == 0){
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
    companion object {
        fun newInstance(position: Int): ReviewWriteScoreFragment {
            val fragment = ReviewWriteScoreFragment()
            val bundle = Bundle()
            bundle.putInt("position", position)
            fragment.arguments = bundle
            return fragment
        }
    }
}