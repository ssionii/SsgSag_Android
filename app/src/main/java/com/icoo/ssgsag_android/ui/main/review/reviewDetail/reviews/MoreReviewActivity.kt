package com.icoo.ssgsag_android.ui.main.review.club.reviews

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.model.review.club.ClubPost
import com.icoo.ssgsag_android.databinding.ActivityReviewMoreBinding
import com.icoo.ssgsag_android.ui.main.feed.FeedWebActivity
import com.icoo.ssgsag_android.ui.main.review.HowWriteReviewActivity
import com.icoo.ssgsag_android.ui.main.review.club.edit.ClubReviewEditActivity
import com.icoo.ssgsag_android.ui.main.review.club.edit.ClubReviewEditDialogFragment
import com.icoo.ssgsag_android.ui.main.review.club.reviews.blogReview.RgstrBlogReviewActivity
import com.icoo.ssgsag_android.ui.main.review.reviewDetail.BlogReviewRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.review.reviewDetail.SsgSagReviewRecyclerViewAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class MoreReviewActivity : BaseActivity<ActivityReviewMoreBinding, MoreReviewViewModel>() {

    override val layoutResID: Int
        get() = R.layout.activity_review_more
    override val viewModel: MoreReviewViewModel by viewModel()

    var ssgsagRvAdapter : SsgSagReviewRecyclerViewAdapter? = null
    var blogRvAdatper : BlogReviewRecyclerViewAdapter? = null
    private var curPage = 0
    private var clubName = ""
    private var clubIdx = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel

        init()
        setButton(intent.getStringExtra("from"))
        //navigator()

        if(intent.getStringExtra("from") == "ssgsag") {
            viewModel.getSsgSagReviews(curPage)
            setSsgSagReviewRv()
            refreshSsgSagRv()
        } else {
            viewModel.getBlogReviews(curPage)
            setBlogReviewRv()
        }
    }


    override fun onResume() {
        super.onResume()


    }

    private fun init(){
        clubIdx = intent.getIntExtra("clubIdx", -1)
        clubName =  intent.getStringExtra("clubName")

        viewModel.clubIdx = clubIdx
        viewDataBinding.actReviewMoreTvTitle.text = clubName
    }

    private fun setSsgSagReviewRv(){

        viewModel.ssgSagReviews.observe(this, Observer { value ->
            viewDataBinding.actReviewMoreTvCount.text = "후기 총 " + value?.size.toString() + "개"
            if(ssgsagRvAdapter != null){
                ssgsagRvAdapter!!.apply{
                    addItem(value)
                    notifyDataSetChanged()
                }
            }else{
                ssgsagRvAdapter = SsgSagReviewRecyclerViewAdapter(value)

                ssgsagRvAdapter?.run {
                    setOnSsgSagReviewClickListener(onSsgSagReviewClickListener)
                    setHasStableIds(true)
                    isMore = true
                }
                viewDataBinding.actReviewMoreRv.run {
                    adapter = ssgsagRvAdapter

                    (itemAnimator as SimpleItemAnimator).run {
                        changeDuration = 0
                        supportsChangeAnimations = false
                    }

                    layoutManager = WrapContentLinearLayoutManager()
                }
            }

        })

        viewDataBinding.actReviewMoreRv.run{
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE) {
                        var position =
                            (layoutManager as WrapContentLinearLayoutManager).findLastVisibleItemPosition()

                        adapter!!.apply {

                            if (itemCount > 0 && (10 * (curPage+1) - 2 < position)) {
                                curPage = (position + 1) / 10

                                viewModel.getSsgSagReviews(curPage)
                            }
                        }

                    }
                }

            })
        }
    }

    private fun refreshSsgSagRv(){
        viewModel.refreshedSsgsagReview.observe(this, Observer { value ->
            ssgsagRvAdapter!!.run{
                replaceItem(value, viewModel.refreshedPosition)
            }
        })

        viewModel.deletedPosition.observe(this, Observer {
            if(it != -1){
                ssgsagRvAdapter!!.run{
                    deleteItem(it)
                    notifyItemRemoved(it)
                }
            }
        })


    }


    private var onSsgSagReviewClickListener
            = object :
        SsgSagReviewRecyclerViewAdapter.OnSsgSagReviewClickListener {
        override fun onHelpClickListener(isLike: Int, idx: Int, position: Int) {
            viewModel.clickLike(isLike, idx, position)
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
            editDialog.show(supportFragmentManager!!, null)
        }

        override fun onEditClicked(item: ClubPost, position: Int) {
            val editDialog = ClubReviewEditDialogFragment()
            editDialog.apply{
                setOnDialogDismissedListener(dialogDismissedListener)
                usage = "edit"
                this.item = item
                this.position = position
            }
            editDialog.show(supportFragmentManager!!, null)
        }
    }

    val dialogDismissedListener
            = object : ClubReviewEditDialogFragment.OnDialogDismissedListener{
        override fun onDeleteDialogDismissed(idx: Int, position: Int) {
            viewModel.deleteReview(idx, position)
        }

        override fun onEditDialogDismissed(item: ClubPost?, position: Int) {
            val intent = Intent(this@MoreReviewActivity, ClubReviewEditActivity::class.java)
            intent.putExtra("clubReview", item)

            startActivity(intent)

            finish()
        }
    }


    private fun setBlogReviewRv(){

        viewDataBinding.actReviewMoreTvWrite.text = "블로그 후기 등록"

        val display = this!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x


        viewModel.blogReviews.observe(this, Observer {value ->

            viewDataBinding.actReviewMoreTvCount.text = "블로그 후기 총 " + value?.size.toString() + "개"
            if(blogRvAdatper != null){
                blogRvAdatper!!.apply{
                    addItem(value)
                    notifyDataSetChanged()
                }
            }else{
                blogRvAdatper = BlogReviewRecyclerViewAdapter(value)

                blogRvAdatper?.run {
                    setOnBlogReviewClickListener(onBlogReviewClickListener)
                    setHasStableIds(true)
                }
                viewDataBinding.actReviewMoreRv.run {
                    adapter = blogRvAdatper

                    (itemAnimator as SimpleItemAnimator).run {
                        changeDuration = 0
                        supportsChangeAnimations = false
                    }

                    layoutManager = WrapContentLinearLayoutManager()
                }
            }

        })

        viewDataBinding.actReviewMoreRv.run{
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE) {
                        var position =
                            (layoutManager as WrapContentLinearLayoutManager).findLastVisibleItemPosition()

                        adapter!!.apply {

                            if (itemCount > 0 && (10 * (curPage+1) - 2 < position)) {
                                curPage = (position + 1) / 10

                                viewModel.getBlogReviews(curPage)
                            }
                        }

                    }
                }

            })
        }
    }


    private var onBlogReviewClickListener
            = object :
        BlogReviewRecyclerViewAdapter.OnBlogReviewClickListener {
        override fun onItemClickListener(url: String) {
            val intent = Intent(this@MoreReviewActivity, FeedWebActivity::class.java)
            intent.putExtra("clubWebsite", url)
            intent.putExtra("from", "review")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }

    private fun setButton(from: String){
        viewDataBinding.actReviewMoreIvBack.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actReviewMoreCvWrite.setSafeOnClickListener {
            if(from == "ssgsag") {
                val intent = Intent(this, HowWriteReviewActivity::class.java)
                intent.apply {
                    putExtra("from", "reviewDetail")
                    putExtra("clubName", clubName)
                    putExtra("clubIdx", clubIdx)
                    startActivity(intent)
                }
            }else{
                val intent = Intent(this, RgstrBlogReviewActivity::class.java)
                intent.putExtra("clubIdx", clubIdx)

                startActivity(intent)
            }
        }
    }

    private fun navigator() {
        viewModel.activityToStart.observe(this, Observer { value ->
            val intent = Intent(this, value.first.java)
            value.second?.let {
                intent.putExtras(it)
            }
            startActivity(intent)
        })
    }

}