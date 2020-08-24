package com.icoo.ssgsag_android.ui.main.review.club.info

import android.content.Intent
import android.graphics.Paint
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentClubInfoBinding
import com.icoo.ssgsag_android.ui.main.feed.FeedWebActivity
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubManagerCheckActivity
import com.icoo.ssgsag_android.ui.main.review.photoViewPager.PhotoViewPagerActivity
import com.icoo.ssgsag_android.util.view.SpacesItemDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.icoo.ssgsag_android.ui.main.review.reviewDetail.ReviewDetailViewModel
import com.icoo.ssgsag_android.util.dataBinding.replaceAll
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.widget.TextView
import org.jetbrains.anko.support.v4.toast


class ClubInfoFragment : BaseFragment<FragmentClubInfoBinding, ReviewDetailViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_club_info
    override val viewModel: ReviewDetailViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        setPhotoRv()
        //navigator()
        setButton()
    }

    private fun setPhotoRv(){

        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x


        viewModel.photoList.observe(this, Observer {value ->

            if(value.isEmpty()){
                viewDataBinding.fragReviewDetailInfoRvPhoto.visibility = GONE
            }else {

                viewDataBinding.fragReviewDetailInfoRvPhoto.run {
                    var reviewPhotoAdapter = ClubInfoPhotoRecyclerViewAdapter(value)
                    reviewPhotoAdapter.apply {
                        setPhotoSize(width)
                        setOnReviewPhotoClickListener(onItemClickListener)
                    }

                    adapter = reviewPhotoAdapter.apply {
                        replaceAll(value)
                        notifyDataSetChanged()
                    }
                    layoutManager = GridLayoutManager(activity, 3)
                    addItemDecoration(SpacesItemDecoration(3, 24))

                }
            }
        })

    }

    private val onItemClickListener = object : ClubInfoPhotoRecyclerViewAdapter.OnReviewPhotoClickListener{
        override fun onItemClickListener(url: Uri, position: Int) {
            val intent = Intent(activity!!, PhotoViewPagerActivity::class.java)
            intent.putExtra("photoList", viewModel.photoList.value?.toTypedArray())
            Log.e("photo list size", viewModel.photoList.value?.toTypedArray()?.size.toString())
            intent.putExtra("clickedPosition", position)
            startActivity(intent)
        }

        override fun onItemRemoveClickListener(url: String) {
        }
    }

    private fun setButton(){
        viewDataBinding.fragReviewDetailInfoIvUrl.apply {
            setSafeOnClickListener {
                //viewModel.navigate(viewDataBinding.fragReviewDetailInfoIvUrl.text.toString())

                val intent = Intent(activity!!, FeedWebActivity::class.java)
                intent.putExtra("url", viewModel.reviewDetail.value?.clubWebsite)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)

            }

            paintFlags = this.paintFlags or UNDERLINE_TEXT_FLAG

        }
        viewDataBinding.fragClubInfoCvRgstrClubInfo.setSafeOnClickListener {
            val intent = Intent(activity!!, ClubManagerCheckActivity::class.java)
            intent.putExtra("isFromReviewDetail", true)
            intent.putExtra("clubIdx", viewModel.reviewDetail.value!!.clubIdx)
            startActivity(intent)

        }

        viewDataBinding.fragClubInfoClEditInfo.setSafeOnClickListener {
            toast("준비중인 기능입니다.")
        }
    }
}
