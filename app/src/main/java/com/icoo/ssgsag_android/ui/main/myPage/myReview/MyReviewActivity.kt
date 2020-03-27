package com.icoo.ssgsag_android.ui.main.myPage.myReview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import com.icoo.ssgsag_android.databinding.ActivityMyReviewBinding
import com.icoo.ssgsag_android.databinding.ItemMyReviewBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyReviewActivity : BaseActivity<ActivityMyReviewBinding, MyReviewViewModel>() {

    override val layoutResID: Int
        get() = R.layout.activity_my_review

    override val viewModel: MyReviewViewModel by viewModel()

    var curPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButton()
        setRv()
        refreshRv()
    }

    override fun onResume() {
        super.onResume()

        (viewDataBinding.actMyReviewRv.adapter as BaseRecyclerViewAdapter<ClubPost, ItemMyReviewBinding>)?.apply{
            clearAll()
            notifyDataSetChanged()
        }

        curPage = 0
        viewModel.getMyReviews(curPage)

    }

    private fun setRv() {
        viewDataBinding.actMyReviewRv.apply {
            adapter = object : BaseRecyclerViewAdapter<ClubPost, ItemMyReviewBinding>() {
                override val layoutResID: Int
                    get() = R.layout.item_my_review
                override val bindingVariableId: Int
                    get() = BR.clubPost
                override val listener: OnItemClickListener?
                    get() = onItemClickListener
            }

            layoutManager = WrapContentLinearLayoutManager()


            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    recyclerView: RecyclerView,
                    newState: Int
                ) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE) {
                        var position =
                            (layoutManager as WrapContentLinearLayoutManager).findLastVisibleItemPosition()

                        adapter!!.apply {
                            if (this.itemCount > 0 && (10 * (curPage + 1) - 2 < position)) {
                                curPage = (position + 1) / 10
                                viewModel.getMyReviews(curPage)
                            }
                        }

                    }
                }

            })
        }

    }

    val onItemClickListener
     = object : BaseRecyclerViewAdapter.OnItemClickListener{
        override fun onItemClicked(item: Any?, position: Int?) {

            val intent = Intent(this@MyReviewActivity, MyReviewDetailActivity::class.java)
            intent.putExtra("myReview", item as ClubPost)
            startActivity(intent)
        }
    }

    private fun refreshRv() {
        viewModel.myReviews.observe(this, Observer { value ->

            if(value.size > 0){
                viewDataBinding.actMyReviewTvEmpty.visibility = GONE
            }else{
                viewDataBinding.actMyReviewTvEmpty.visibility = VISIBLE
            }

            (viewDataBinding.actMyReviewRv.adapter as BaseRecyclerViewAdapter<ClubPost, ItemMyReviewBinding>).apply {
                addItem(value)
                notifyDataSetChanged()

                Log.e("myReview", "refresh")
            }

        })

    }

    private fun setButton(){
        viewDataBinding.actMyReviewBack.setSafeOnClickListener {
            finish()
        }
    }
}