package com.icoo.ssgsag_android.ui.main.review.main

import android.os.Bundle
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentReviewMainBinding
import com.icoo.ssgsag_android.ui.main.review.ReviewFragment
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewMainFragment : BaseFragment<FragmentReviewMainBinding, ReviewMainViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_review_main

    override val viewModel: ReviewMainViewModel by viewModel()

    lateinit var clubReviewFragment: ReviewFragment
    lateinit var actReviewFragment: ReviewFragment
    lateinit var internReviewFragment: ReviewFragment


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        setButton()
    }

    private fun setButton(){

        viewDataBinding.fragReviewMainRlClub.setSafeOnClickListener {
            clubReviewFragment = ReviewFragment()
            clubReviewFragment.reviewType = 1

            fragmentManager!!.beginTransaction().add(R.id.frag_review_main_fl_container, clubReviewFragment, "club").commit()
            fragmentManager!!.beginTransaction().show(clubReviewFragment).commit()
        }

        viewDataBinding.fragReviewMainRlAct.setSafeOnClickListener {
            actReviewFragment = ReviewFragment()
            actReviewFragment.reviewType = 2

            fragmentManager!!.beginTransaction().add(R.id.frag_review_main_fl_container, actReviewFragment, "act").commit()
            fragmentManager!!.beginTransaction().show(actReviewFragment).commit()
        }

        viewDataBinding.fragReviewMainRlIntern.setSafeOnClickListener {
            internReviewFragment = ReviewFragment()
            internReviewFragment.reviewType = 3

            fragmentManager!!.beginTransaction().add(R.id.frag_review_main_fl_container, internReviewFragment, "intern").commit()
            fragmentManager!!.beginTransaction().show(internReviewFragment).commit()
        }
    }

    fun removeFragment(tag: String){

        fragmentManager!!.findFragmentByTag(tag)?.also {
            fragmentManager!!.beginTransaction().remove(it)
        }

    }
}
