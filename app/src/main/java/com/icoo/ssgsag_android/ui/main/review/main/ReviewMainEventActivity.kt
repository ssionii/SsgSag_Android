package com.icoo.ssgsag_android.ui.main.review.main

import android.content.Intent
import android.os.Bundle
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityReviewMainEventBinding
import com.icoo.ssgsag_android.ui.main.review.HowWriteReviewActivity
import com.icoo.ssgsag_android.ui.main.review.ReviewPageFragment
import com.icoo.ssgsag_android.ui.main.review.reviewDetail.ReviewDetailViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewMainEventActivity : BaseActivity<ActivityReviewMainEventBinding, ReviewMainViewModel>() {

    override val layoutResID: Int
        get() = R.layout.activity_review_main_event
    override val viewModel: ReviewMainViewModel by viewModel()

    lateinit var clubReviewPageFragment: ReviewPageFragment
    lateinit var actReviewPageFragment: ReviewPageFragment
    lateinit var internReviewPageFragment: ReviewPageFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        setButton()

    }

    private fun setButton(){

        val intent = Intent(this, HowWriteReviewActivity::class.java)
        intent.putExtra("from", "main")

        viewDataBinding.actReviewMainRlClub.setSafeOnClickListener {
            intent.putExtra("reviewType", "club")
            startActivity(intent)

            finish()
        }

        viewDataBinding.actReviewMainRlAct.setSafeOnClickListener {
            intent.putExtra("reviewType", "act")
            startActivity(intent)

            finish()
        }

        viewDataBinding.actReviewMainRlIntern.setSafeOnClickListener {
            intent.putExtra("reviewType", "intern")
            startActivity(intent)

            finish()

        }

        viewDataBinding.actReviewMainEventIvCancel.setSafeOnClickListener {
            finish()
        }
    }
}