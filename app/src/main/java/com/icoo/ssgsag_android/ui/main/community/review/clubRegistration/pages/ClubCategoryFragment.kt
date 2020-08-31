package com.icoo.ssgsag_android.ui.main.review.club.registration.pages

import android.os.Bundle
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.icoo.ssgsag_android.databinding.FragmentClubCategoryBinding
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrActivity
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrViewModel
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteActivity
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteViewModel
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class ClubCategoryFragment : BaseFragment<FragmentClubCategoryBinding, ClubRgstrViewModel>(){
    override val layoutResID: Int
        get() = R.layout.fragment_club_category
    override val viewModel: ClubRgstrViewModel by viewModel()

    val ReviewWriteViewModel : ReviewWriteViewModel by viewModel()

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
                ReviewWriteViewModel.setClubType(1)
                (activity as ReviewWriteActivity).toNextPage(position)
            }

        }

        viewDataBinding.actClubManagerCheckLlUnion.setSafeOnClickListener {
            if(parent == "rgstr") {
                viewModel.setClubType(0)
                (activity as ClubRgstrActivity).toNextPage(position)
            } else if(parent == "write") {
                ReviewWriteViewModel.setClubType(0)
                (activity as ReviewWriteActivity).toNextPage(position)
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