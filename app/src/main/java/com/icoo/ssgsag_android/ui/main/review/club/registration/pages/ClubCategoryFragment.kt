package com.icoo.ssgsag_android.ui.main.review.club.registration.pages

import android.app.Activity
import android.os.Bundle
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.icoo.ssgsag_android.databinding.FragmentClubCategoryBinding
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrActivity
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrViewModel
import com.icoo.ssgsag_android.ui.main.review.club.write.ClubReviewWriteActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ClubReviewWriteViewModel
import com.icoo.ssgsag_android.ui.main.review.photoViewPager.PhotoFragment
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class ClubCategoryFragment : BaseFragment<FragmentClubCategoryBinding, ClubRgstrViewModel>(){
    override val layoutResID: Int
        get() = R.layout.fragment_club_category
    override val viewModel: ClubRgstrViewModel by viewModel()

    val reviewWriteViewModel : ClubReviewWriteViewModel by viewModel()

    val position = 0
    var parent = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        parent =  arguments!!.getString("from")

        setButton()
    }


    private fun setButton(){

        viewDataBinding.fragClubCategoryIvBack.setSafeOnClickListener {
            activity!!.finish()
        }

        viewDataBinding.actClubManagerCheckLlCampus.setSafeOnClickListener {
            if(parent == "rgstr") {
                viewModel.setClubType(1)
                (activity as ClubRgstrActivity).toNextPage(position)
            }
            else if(parent == "write") {
                reviewWriteViewModel.setClubType(1)
                (activity as ClubReviewWriteActivity).toNextPage(position)
            }

        }

        viewDataBinding.actClubManagerCheckLlUnion.setSafeOnClickListener {
            if(parent == "rgstr") {
                viewModel.setClubType(0)
                (activity as ClubRgstrActivity).toNextPage(position)
            } else if(parent == "write") {
                reviewWriteViewModel.setClubType(0)
                (activity as ClubReviewWriteActivity).toNextPage(position)
            }
        }
    }
    companion object {
        fun newInstance(from: String): ClubCategoryFragment {
            val fragment = ClubCategoryFragment()
            val bundle = Bundle()
            bundle.putString("from", from)
            fragment.arguments = bundle
            return fragment
        }
    }

}