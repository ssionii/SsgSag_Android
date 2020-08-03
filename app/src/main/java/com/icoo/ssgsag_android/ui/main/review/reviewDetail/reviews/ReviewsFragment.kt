package com.icoo.ssgsag_android.ui.main.review.club.reviews


import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import com.icoo.ssgsag_android.databinding.FragmentClubReviewsBinding
import com.icoo.ssgsag_android.ui.main.feed.FeedWebActivity
import com.icoo.ssgsag_android.ui.main.review.HowWriteReviewActivity
import com.icoo.ssgsag_android.ui.main.review.club.edit.ClubReviewEditActivity
import com.icoo.ssgsag_android.ui.main.review.club.edit.ClubReviewEditDialogFragment
import com.icoo.ssgsag_android.ui.main.review.club.reviews.blogReview.RgstrBlogReviewActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.icoo.ssgsag_android.ui.main.review.reviewDetail.ReviewDetailViewModel
import com.icoo.ssgsag_android.ui.main.review.reviewDetail.BlogReviewRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.review.reviewDetail.SsgSagReviewRecyclerViewAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.jetbrains.anko.support.v4.toast


class ReviewsFragment : BaseFragment<FragmentClubReviewsBinding, ReviewDetailViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_club_reviews
    override val viewModel: ReviewDetailViewModel by viewModel()

    private var mClubIdx = 0
    private var reviewType = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        viewModel.mClubIdx = this.mClubIdx
        viewModel.reviewType = this.reviewType

        viewModel.getClubDetail()
        //viewModel.getSsgSagMainReviews()
        setSsgSagReviewRv()
        setBlogReviewRv()
        setButton()
        //navigator()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSsgSagMainReviews()
        viewModel.getBlogReviews()
    }

    fun setClubIdx(clubIdx: Int){
        mClubIdx = clubIdx
    }

    fun setReviewType(reviewType: String){
        this.reviewType = reviewType
    }

    private fun setSsgSagReviewRv(){

        viewModel.ssgSagMainReviews.observe(this, Observer { value ->

            viewDataBinding.fragReviewDetailReviewRvSsgsag.run{
                if(adapter != null){
                    (adapter as SsgSagReviewRecyclerViewAdapter).apply{
                        replaceAll(value)
                        notifyDataSetChanged()
                    }
                }else{
                    adapter = SsgSagReviewRecyclerViewAdapter(value)
                    (adapter as SsgSagReviewRecyclerViewAdapter).setOnSsgSagReviewClickListener(onSsgSagReviewClickListener)

                    layoutManager = WrapContentLinearLayoutManager()

                }
            }

        })
    }

    private var onSsgSagReviewClickListener
    = object :
        SsgSagReviewRecyclerViewAdapter.OnSsgSagReviewClickListener {
        override fun onHelpClickListener(isLike: Int, idx: Int, position: Int) {
            viewModel.clickLike(isLike, idx)
        }
        override fun onCautionClicked(commentIdx: Int) {
            toast("준비중인 기능입니다.")
        }

        override fun onDeleteClicked(idx: Int, position: Int) {
            val editDialog = ClubReviewEditDialogFragment()
            editDialog.apply{
                setOnDialogDismissedListener(dialogDismissedListener)
                usage = "delete"
                this.idx = idx
                this.position = position
            }
            editDialog.show(fragmentManager!!, null)
        }

        override fun onEditClicked(item: ClubPost, position: Int) {
            val editDialog = ClubReviewEditDialogFragment()
            editDialog.apply{
                setOnDialogDismissedListener(dialogDismissedListener)
                usage = "edit"
                this.item = item
                this.position = position
            }
            editDialog.show(fragmentManager!!, null)
        }
    }

    val dialogDismissedListener
            = object : ClubReviewEditDialogFragment.OnDialogDismissedListener{
        override fun onDeleteDialogDismissed(idx: Int, position : Int) {
            viewModel.deleteReview(idx)
        }

        override fun onEditDialogDismissed(item: ClubPost?, position: Int) {
            val intent = Intent(activity!!, ClubReviewEditActivity::class.java)
            item?.apply{
                clubName = viewModel.reviewDetail.value?.clubName
                clubType = viewModel.reviewDetail.value?.clubType
                clubIdx = viewModel.reviewDetail.value?.clubIdx
                univOrLocation = viewModel.reviewDetail.value?.univOrLocation
            }
            intent.putExtra("clubReview", item)

            startActivity(intent)
        }
    }

    private fun setBlogReviewRv(){

        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x

        viewModel.blogMainReviews.observe(this, Observer {value ->
            viewDataBinding.fragReviewDetailReviewRvBlog.run{

                var blogReviewAdapter =
                    BlogReviewRecyclerViewAdapter(value)
                blogReviewAdapter.setOnBlogReviewClickListener(onBlogReviewClickListener)
                blogReviewAdapter.setPhotoSize(width)

                adapter =  blogReviewAdapter
                layoutManager = WrapContentLinearLayoutManager()
            }
        })
    }

    private var onBlogReviewClickListener
            = object :
        BlogReviewRecyclerViewAdapter.OnBlogReviewClickListener {
        override fun onItemClickListener(url: String, title: String) {
            val intent = Intent(activity!!, FeedWebActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", title)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }

    private fun setButton(){
        val moreReviewIntent = Intent(context, MoreReviewActivity::class.java)

        viewDataBinding.fragReviewDetailReviewClMoreSsgsag.setSafeOnClickListener {
            moreReviewIntent.putExtra("clubIdx", viewModel.mClubIdx)
            moreReviewIntent.putExtra("from", "ssgsag")
            viewModel.reviewDetail.value.let{
                if(it != null) {
                    moreReviewIntent.putExtra("reviewType", viewModel.reviewType)
                    moreReviewIntent.putExtra("clubName", viewModel.reviewDetail.value!!.clubName)
                    startActivity(moreReviewIntent)
                }
            }
        }

        viewDataBinding.fragReviewDetailReviewClMoreBlog.setSafeOnClickListener {
            moreReviewIntent.putExtra("clubIdx", viewModel.mClubIdx)
            moreReviewIntent.putExtra("from", "blog")
            viewModel.reviewDetail.value.let{
                if(it != null) {
                    moreReviewIntent.putExtra("reviewType", viewModel.reviewType)
                    moreReviewIntent.putExtra("clubName", viewModel.reviewDetail.value!!.clubName)
                    startActivity(moreReviewIntent)
                }
            }
        }

        viewDataBinding.fragReviewDetailReviewCvSubmitSsgsagReview.setSafeOnClickListener {
            val intent = Intent(activity!!, HowWriteReviewActivity::class.java)
            intent.apply {
                putExtra("from", "reviewDetail")
                putExtra("reviewType", viewModel.reviewType)
                putExtra("clubName", viewModel.reviewDetail.value!!.clubName)
                putExtra("clubIdx", viewModel.mClubIdx)
            }

            startActivity(intent)

        }

        viewDataBinding.fragReviewDetailReviewCvSubmitBlogReview.setSafeOnClickListener {

            val intent = Intent(activity!!, RgstrBlogReviewActivity::class.java)
            intent.putExtra("clubIdx", viewModel.mClubIdx)

            startActivity(intent)
        }
    }


}